package io.skydeck.gserver.engine;

import io.skydeck.gserver.domain.*;
import io.skydeck.gserver.domain.dto.CardDiscardDTO;
import io.skydeck.gserver.domain.dto.CardSacrificeDTO;
import io.skydeck.gserver.domain.dto.CardUseDTO;
import io.skydeck.gserver.enums.*;
import io.skydeck.gserver.impl.*;
import io.skydeck.gserver.util.PositionUtil;
import jakarta.annotation.Resource;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Data
@Log4j2
@Component
public class GameEngine {
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
    private Player currentPlayer;
    private List<Player> players;
    private volatile boolean activeEnd = false;
    private Phase currentPhase;
    private SettlementBase currentSettlement;
    private Queue<CsBufferItem> csBuffer = new LinkedList<>();

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
                if(p.getKingdom() == kingdom) {
                    ++count;
                }
            }
            return count;
        }
    }
    public int distance(Player offender, Player defender) {
        return PositionUtil.distance(offender, defender, players);
    }
    @SuppressWarnings("DuplicateBranchesInSwitch")
    public boolean canSelectAsCardTarget(Player offender, Player defender, CardBase card) {
        boolean val = offender.canSelectAsCardTarget(defender, card);
        if (val) {
            return true;
        }
        switch (card.nameType()) {
            case Slash:
            case FireSlash:
            case IceSlash:
            case ThunderSlash:
                if (offender == currentPlayer && currentPhase == Phase.ActivePhase) {
                    StageState stageState = offender.getStageState();
                    if (stageState.getUseSlashCount() >= stageState.getSlashQuota()) {
                        return false;
                    }
                }
                return offender.attackRange() >= distance(offender, defender);
            case Cure:
                return offender == defender || defender.getHealth() <= 0;
            case Liquor:
                if (offender == currentPlayer) {
                    StageState stageState = offender.getStageState();
                    if (stageState.getUseLiquorCount() >= stageState.getLiquorQuota()) {
                        return false;
                    }
                }
                return offender == defender;
            case DuelPloy:
            case DismantlePloy:
            case StealPloy:
            case EnhancedDismantlePloy:
            case SendLimboPloy:
            case SkipActivePloy:
                return offender != defender;
            case SkipDrawPloy:
                return offender != defender && distance(offender, defender) <= 1;
            case ThrivePloy:
            case LightningPloy:
            case MoreStagePloy:
                return offender == defender;
            case StealWeaponPloy:
                return offender != defender
                        && defender.getEquips().stream().anyMatch(equip -> equip.subType() == CardSubType.Weapon);
            case MutualBenefitPloy:
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
    public void mainLoop() {
        for (; ; currentPlayer = PositionUtil.nextAlivePlayer(currentPlayer, players)) {
            if (!this.preStartCheck()) {
                continue;
            }
            this.resolveStage();
            this.postYieldCheck();
        }
    }

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
        }
        return true;
    }

    private void postYieldCheck() {
        for (Player player : players) {
            player.onPostYield(this);
            try {
                player.getStageState().resetDefault();
            } catch (Exception e) {
                log.error("reset stageState error, player:{}", player.getId(), e);
            }
        }
    }

    public void initPlayers(List<Player> players) {
        this.players = players;
    }

    public void runSettlement(SettlementBase settlement) {
        settlement.resolve(this);
        purgeCsBuffer();
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
    }

    public void onCardTargeting(CardUseDTO dto, CardSettlement settlement) {
    }

    public void onCardTargeted(CardUseDTO dto, CardSettlement settlement) {
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
    }

    public void onCardSacrificed(CardSacrificeDTO dto, CardSacrificeSettlement settlement) {
    }

    public void onCardDiscarded(CardDiscardDTO dto, CardDiscardSettlement settlement) {
    }

    public void onCardLosing() {
    }

    public void onCardLost(Player player, Enum type, List<CardBase> cards) {
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

    public void onDying(DyingSettlement settlement) {
    }

    public void onStart() {
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
        System.out.println("active");
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
    }

    public void onEndPhase() {
    }

    public void onYield() {
    }

    public void onCardBurying(Player player, List<CardBase> cards, Enum type) {
    }

    public void onCardBuried(Player player, List<CardBase> cards, Enum type) {

    }


    public void onJinkSucceed(SlashCardUseSettlement settlement, Player offender, Player defender) {
        for (Player player : PositionUtil.positionSort(currentPlayer, players)) {
            if (player.isDead()) {
                continue;
            }
            player.onJinkSucceed(this, settlement, offender, defender);
        }
    }
    /* Events End*/


    public void endActivePhase() {
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
