package io.skydeck.gserver.engine;

import io.skydeck.gserver.domain.CardBase;
import io.skydeck.gserver.domain.CardSettlement;
import io.skydeck.gserver.domain.Player;
import io.skydeck.gserver.domain.SettlementBase;
import io.skydeck.gserver.domain.dto.CardUseDTO;
import io.skydeck.gserver.enums.*;
import io.skydeck.gserver.impl.DamageSettlement;
import io.skydeck.gserver.impl.DyingSettlement;
import io.skydeck.gserver.impl.InDangerSettlement;
import io.skydeck.gserver.impl.SlashCardUseSettlement;
import io.skydeck.gserver.util.PositionUtil;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
@Data
@Log4j2
public class GameEngine {

    private CardFilterFactory cardFilterFactory;
    private PublicCardResManager pcrManager;
    private QueryManager queryManager;
    private DynamicCardManager dynamicCardManager;
    private Player currentPlayer;
    private List<Player> players;
    private Phase currentPhase;
    private SettlementBase currentSettlement;
//    private Queue<SettlementBase> settlementQueue;

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
                return offender.attackRange() >= distance(offender, defender);
            case Cure:
                return offender == defender || defender.getHealth() <= 0;
            case Liquor:
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

    public void onCardSacrificing() {
    }

    public void onCardSacrificed() {
    }

    public void onCardDiscarded() {
    }

    public void onCardLosing() {
    }

    public void onCardLost() {
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

    public void onCardBurying(CardUseDTO dto, CardSettlement settlement) {
    }

    public void onCardBuried(CardUseDTO dto, CardSettlement settlement) {
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


}
