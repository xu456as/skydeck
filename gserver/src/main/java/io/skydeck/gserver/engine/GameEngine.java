package io.skydeck.gserver.engine;

import io.skydeck.gserver.domain.card.CardBase;
import io.skydeck.gserver.domain.card.DynamicCard;
import io.skydeck.gserver.domain.card.GearCardBase;
import io.skydeck.gserver.domain.dto.CardDiscardDTO;
import io.skydeck.gserver.domain.dto.CardReframeDTO;
import io.skydeck.gserver.domain.dto.CardSacrificeDTO;
import io.skydeck.gserver.domain.dto.CardUseDTO;
import io.skydeck.gserver.domain.player.Player;
import io.skydeck.gserver.domain.player.PlayerStageState;
import io.skydeck.gserver.domain.settlement.CardSettlement;
import io.skydeck.gserver.domain.settlement.SettlementBase;
import io.skydeck.gserver.domain.skill.AbilityBase;
import io.skydeck.gserver.enums.*;
import io.skydeck.gserver.impl.settlement.*;
import io.skydeck.gserver.util.PositionUtil;
import jakarta.annotation.Resource;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Data
@Log4j2
@Component
@Scope("prototype")
public class GameEngine {
    private String id = UUID.randomUUID().toString().replace("-", "");
    @Resource
    private CardSetReader cardSetReader;
    @Resource
    private VisibilityManager visibilityManager;
    @Resource
    private AbilityFactory abilityFactory;
    @Resource
    private CardFilterFactory cardFilterFactory;
    @Resource
    private PublicCardResManager pcrManager;
    @Resource
    private QueryManager queryManager;
    @Resource
    private DynamicCardManager dynamicCardManager;
    @Resource
    private NetworkInterface network;
    private Player currentPlayer;
    private List<Player> players;
    private volatile boolean activeEnd = false;
    private Phase currentPhase;
    private Stack<SettlementBase> settlementQueue = new Stack<>();
    private Queue<CsBufferItem> csBuffer = new LinkedList<>();
    private Player gainStagePlayer = null;
    private Player roundPlayer = null;
    private AtomicLong eventIncrementer = new AtomicLong(0);
//    private volatile long currentEventId;
    public GameEngine() {
    }

//    private Queue<SettlementBase> settlementQueue;

    public int aliveTeamMember(Player player) {
        if (player.isDead()) {
            return 0;
        }
        Kingdom kingdom = player.getKingdom();
        if (kingdom == Kingdom.Unknown) {
            return 1;
        } else if (kingdom == Kingdom.Am) {
            return 1;
        } else {
            int count = 0;
            for (Player p : players) {
                if (p.isDead()) {
                    continue;
                }
                if (p.getKingdom() == kingdom) {
                    ++count;
                }
            }
            return count;
        }
    }

    public int sameKingCount(Player player) {
        int count = 1;
        for (Player p : players) {
            if (p.isDead() || p == player) {
                continue;
            }
            if (p.getKingdom() != player.getKingdom()) {
                continue;
            }
            count += 1;
        }
        return player.kingdomVolMod(this, count);
    }
    public int distance(Player offender, Player defender) {
        return PositionUtil.distance(offender, defender, players);
    }

    @SuppressWarnings("DuplicateBranchesInSwitch")
    public boolean canSelectAsCardTarget(Player offender, Player defender, CardBase card) {
        boolean val = offender.canSelectAsCardTarget(this, defender, card);
        if (val) {
            return true;
        }
        switch (card.nameType()) {
            case Slash:
                if (offender == currentPlayer && currentPhase == Phase.ActivePhase) {
                    PlayerStageState stageState = offender.getStageState();
                    if (stageState.getUseSlashCount() >= offender.slashQuota(this)) {
                        return false;
                    }
                }
                return offender.attackRange() >= distance(offender, defender) || offender.ignoreDistance(defender, card);
            case Cure:
                return offender == defender || defender.getHealth() <= 0;
            case Liquor:
                if (offender == currentPlayer) {
                    PlayerStageState stageState = offender.getStageState();
                    if (stageState.getUseLiquorCount() >= stageState.getLiquorQuota()) {
                        return false;
                    }
                }
                return offender == defender;
            case DuelPloy:
            case DismantlePloy:
            case EnhancedDismantlePloy:
            case SendLimboPloy:
            case SkipActivePloy:
                return offender != defender;
            case StealPloy:
            case SkipDrawPloy:
                return offender != defender && distance(offender, defender) <= 1 || offender.ignoreDistance(defender, card);
            case ThrivePloy:
            case LightningPloy:
            case GainStagePloy:
                return offender == defender && inGiantKingdom(offender);
            case StealWeaponPloy:
                return offender != defender
                        && defender.getEquips().stream().anyMatch(equip -> equip.subType() == CardSubType.Weapon);
            case AllianceCheerPloy:
            case MutualThrivePloy:
                return offender != defender
                        && offender.getKingdom() != Kingdom.Unknown && defender.getKingdom() != Kingdom.Unknown
                        && offender.getKingdom() != defender.getKingdom();
            case FirePloy:
                return CollectionUtils.isNotEmpty(defender.getHands());
        }
        if (card.type() == CardType.Ploy) {
            return true;
        }
        return false;
    }

    //API
    public void initPlayers(List<Player> players) {
        this.players = players;
        this.currentPlayer = players.get(0);
    }

    public void mainLoop() {
        for (; ; currentPlayer = nextPlayer()) {
            if (!this.preStartCheck()) {
                continue;
            }
            this.resolveStage();
            this.postYieldCheck();
        }
    }
    //API

    private void resolveStage() {
        activeEnd = false;
        this.onStart();
        this.onPreparePhase();
        this.onPreEnterJudgePhase();
        this.onJudgePhase();
        this.onPreEnterDrawPhase();
        this.onEnteringDrawPhase();
        this.onDrawPhase();
        this.onLeavingDrawPhase();
        this.onPreEnterActivePhase();
        this.onEnteringActivePhase();
        this.onActivePhase();
        this.onLeavingActivePhase();
        this.onPreEnterDiscardPhase();
        this.onEnteringDiscardPhase();
        this.onDiscardPhase();
        this.onLeavingDiscardPhase();
        this.onEndPhase();
        this.onYield();
    }

    private boolean preStartCheck() {
        if (currentPlayer.isExpelled()) {
            currentPlayer.setExpelled(false);
            return false;
        }
        for (Player player : players) {
            player.onPreStart(this);
            try {
                player.getStageState().resetDefault();
                for (AbilityBase ab : player.allAbilities()) {
                    ab.getStageState().resetDefault();
                }
            } catch (Exception e) {
                log.error("reset stageState error, player:{}", player.getId(), e);
            }
        }
        return true;
    }

    private void postYieldCheck() {
        for (Player player : players) {
            player.onPostYield(this);
            try {
                player.getStageState().resetDefault();
                for (AbilityBase ab : player.allAbilities()) {
                    ab.getStageState().resetDefault();
                }
            } catch (Exception e) {
                log.error("reset stageState error, player:{}", player.getId(), e);
            }
        }
    }

    public void runSettlement(SettlementBase settlement) {
        settlementQueue.push(settlement);
        settlement.resolve(this);
        settlementQueue.pop();
        purgeCsBuffer();
    }
    public SettlementBase currentSettlement() {
        if (settlementQueue.isEmpty()) {
            return null;
        }
        return settlementQueue.peek();
    }

    public void addToCsBuffer(Player player, List<CardBase> cards, CardLostType type) {
        if (CollectionUtils.isEmpty(cards)) {
            return;
        }
        csBuffer.offer(CsBufferItem.newOne(player, cards, type));
    }

    public boolean checkCsBuffer(CardBase card) {
        return csBuffer.stream().anyMatch(item -> item.cards.contains(card));
    }

    public List<CardBase> recycleCsBuffer(CardBase card) {
        return csBuffer.stream()
                .filter(item -> item.cards.contains(card))
                .map(item -> {
                    item.cards.remove(card);
                    List<CardBase> cardList = null;
                    if (card instanceof DynamicCard dCard) {
                        if (dCard.virtual()) {
                            cardList = Collections.emptyList();
                        } else {
                            cardList = new ArrayList<>(dCard.originCards());
                        }
                    } else {
                        cardList = new ArrayList<>();
                        cardList.add(card);
                    }
                    return cardList;
                })
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private void purgeCsBuffer() {
        while (!csBuffer.isEmpty()) {
            CsBufferItem item = csBuffer.poll();
            onCardBurying(item.owner, item.cards, item.lostType);
            pcrManager.addToGrave(this, item.cards);
            onCardBuried(item.owner, item.cards, item.lostType);
        }

    }

    /* Events Begin*/
    public void onCardUsing(CardUseDTO dto, CardSettlement settlement) {
        Player user = dto.getPlayer();
        CardBase card = dto.getCard();
        user.incStageCount("useCardCount");
        if (card.subType() == CardSubType.Slash) {
            user.incStageCount("useSlashCount");
        }
    }


    public void onOCardTargeting(CardUseDTO dto, CardSettlement settlement) {
    }

    public void onDCardTargeting(CardUseDTO dto, CardSettlement settlement) {
    }

    public void onOCardTargeted(CardUseDTO dto, CardSettlement settlement) {
    }

    public void onDCardTargeted(CardUseDTO dto, CardSettlement settlement) {
    }

    public boolean onCardEffecting(CardUseDTO dto, CardSettlement settlement, Player target) {
        return true;
    }

    public void onCardEffected(CardUseDTO dto, CardSettlement settlement, Player target) {
    }

    public void onCardEffectFinish(CardUseDTO dto, CardSettlement settlement) {
    }

    public void onCardUsed(CardUseDTO dto, CardSettlement settlement) {
    }

    public void onCardSacrificing(CardSacrificeDTO dto, CardSacrificeSettlement settlement) {
        dto.getPlayer().incStageCount("sacrificeCardCount");
    }

    public void onCardSacrificed(CardSacrificeDTO dto, CardSacrificeSettlement settlement) {
    }
    public void onCardReframing(CardReframeDTO dto, CardReframeSettlement settlement) {
        dto.getPlayer().incStageCount("reframeCardCount");
    }

    public void onCardDiscarding(CardDiscardDTO dto, CardDiscardSettlement settlement) {
        if (CollectionUtils.isNotEmpty(dto.getCard())) {
            dto.getOffender().incStageCount("discardCardCount", dto.getCard().size());
        }
    }
    public void onCardDiscarded(CardDiscardDTO dto, CardDiscardSettlement settlement) {

    }

    public List<CardBase> onCardLosing(Player player, Enum type, List<CardBase> cards) {
        //TODO
        return cards;
    }

    public void onCardLost(Player player, Enum type, List<CardBase> cards) {
        if (CollectionUtils.isNotEmpty(cards)) {
            player.incStageCount("lostCardCount", cards.size());
        }
    }
    public boolean onCardEquipping(Player player, GearEquipType type, List<GearCardBase> cards) {
        //todo
        return true;
    }
    public void onCardEquipped(Player player, GearEquipType type, List<GearCardBase> cards) {
        //todo
    }
    public void onCardJudgeEffecting(CardJudgeSettlement judgeSettlement) {
    }
    public void onCardJudgeEffected(CardJudgeSettlement judgeSettlement) {
    }


    public boolean onDealingDamage(DamageSettlement settlement) {
        return true;
    }

    public void onDealtDamage(DamageSettlement settlement) {
    }

    public void onGettingDamage() {
    }

    public void onGotDamage() {
    }

    public void onDrawNumberCheck() {
    }

    public void onAlcoholLimitCheck() {
    }

    public void onSlashLimitCheck() {
    }

    public void onSlashNumberCheck() {
    }

    public void onHandUpperBoundCheck() {
    }

    public void onCardDisposeAllowCheck() {
    }

    public void onSkillActiveAllowCheck() {
    }

    public void onDistanceCheck() {
    }

    public void onEffectMuteCheck() {
    }

    public void onCardUsingQuery() {
    }

    public void onCardSacrificingQuery() {
    }

    public void onEnteringCureQuery() {
    }

    public void onLeavingCureQuery() {
    }

    public void onDanger(InDangerSettlement settlement) {
    }

    public void onRecover(InDangerSettlement settlement) {
    }

    public void onDying(DeceaseSettlement settlement) {
    }


    public void onStart() {
        for (Player player : PositionUtil.positionSort(currentPlayer, players)) {
            if (player.isDead()) {
                continue;
            }
            player.removeDynamicAbility(this, PhaseEvent.Start, currentPlayer);
        }
    }

    public void onPreparePhase() {
    }

    public void onPreEnterJudgePhase() {
    }

    public void onJudgePhase() {
    }

    public void onPreEnterDrawPhase() {
    }

    public void onEnteringDrawPhase() {
    }

    public void onDrawPhase() {
    }

    public void onLeavingDrawPhase() {

    }

    public void onPreEnterActivePhase() {
    }

    public void onEnteringActivePhase() {
    }

    public void onActivePhase() {
        log.info("player[{}] starts active phase", currentPlayer);
        while (!activeEnd) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.error("activePhase sleep error", e);
            }
        }
    }


    public void onLeavingActivePhase() {
    }

    public void onPreEnterDiscardPhase() {
    }

    public void onEnteringDiscardPhase() {
    }

    public void onDiscardPhase() {
    }

    public void onLeavingDiscardPhase() {
        for (Player player : PositionUtil.positionSort(currentPlayer, players)) {
            if (player.isDead()) {
                continue;
            }
            player.onLeavingDiscardPhase(this, currentPlayer);
        }
    }

    public void onEndPhase() {
    }

    public void onYield() {
        for (Player player : PositionUtil.positionSort(currentPlayer, players)) {
            if (player.isDead()) {
                continue;
            }
            player.removeDynamicAbility(this, PhaseEvent.Yield, currentPlayer);
        }
    }

    public void onCardBurying(Player player, List<CardBase> cards, Enum type) {
    }

    public void onCardBuried(Player player, List<CardBase> cards, Enum type) {

    }


    public void onJinkSucceed(SlashUseSettlement settlement, Player offender, Player defender) {
        for (Player player : PositionUtil.positionSort(currentPlayer, players)) {
            if (player.isDead()) {
                continue;
            }
            player.onJinkSucceed(this, settlement, offender, defender);
        }
    }
    /* Events End*/


    public void endActivePhase() {
        log.info("player[{}] ends active phase", currentPlayer);
        activeEnd = true;
    }

    public void batchQueryAbility(Player player, List<AbilityBase> abilities, Consumer<AbilityBase> action) {
        List<AbilityBase> abilityCol = new ArrayList<>(abilities);
        while (!abilityCol.isEmpty()) {
            AbilityBase ability = queryManager.abilitiesQuery(player, abilityCol);
            if (ability == null) {
                break;
            }
            abilityCol.remove(ability);
            action.accept(ability);
        }
    }


    public void onHealthChanged(Player player, int amount) {
    }

    public void yieldStageToPlayer(Player player) {
        gainStagePlayer = player;
        if (roundPlayer == null) {
            roundPlayer = currentPlayer;
        }
    }
    private Player nextPlayer() {
        if (gainStagePlayer != null) {
            Player temp = gainStagePlayer;
            gainStagePlayer = null;
            roundPlayer = currentPlayer;
            return temp;
        }
        if (roundPlayer != null) {
            Player temp = roundPlayer;
            roundPlayer = null;
            return PositionUtil.nextAlivePlayer(temp, players);
        }
        return PositionUtil.nextAlivePlayer(currentPlayer, players);
    }
    public boolean inGiantKingdom(Player player) {
        //TODO
        return false;
    }



    private static class CsBufferItem {
        private Player owner;
        private List<CardBase> cards;
        private CardLostType lostType;

        static CsBufferItem newOne(Player owner, List<CardBase> cards, CardLostType type) {
            CsBufferItem item = new CsBufferItem();
            item.owner = owner;
            item.lostType = type;
            item.cards = cards;
            return item;
        }
    }
}
