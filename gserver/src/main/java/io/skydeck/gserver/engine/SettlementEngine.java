package io.skydeck.gserver.engine;

import io.skydeck.gserver.domain.CardSettlement;
import io.skydeck.gserver.domain.Player;
import io.skydeck.gserver.domain.SettlementBase;
import io.skydeck.gserver.domain.dto.CardUseDTO;
import io.skydeck.gserver.impl.DamageSettlement;
import io.skydeck.gserver.impl.DyingSettlement;
import lombok.Data;

import java.util.List;
import java.util.Queue;

@Data
public class SettlementEngine {

    private CardFilterFactory cardFilterFactory;
    private QueryManager queryManager;
    private Player currentPlayer;
    private List<Player> players;
    private Queue<SettlementBase> settlementQueue;


    public void execute() {
        while (!settlementQueue.isEmpty()) {
            SettlementBase settlement = settlementQueue.poll();
            settlement.resolve(this);
        }
    }



    /* Events Begin*/
    public void onCardUsing(CardUseDTO dto, CardSettlement settlement) {}
    public void onCardTargeting(CardUseDTO dto, CardSettlement settlement) {}
    public void onCardTargeted(CardUseDTO dto, CardSettlement settlement) {}
    public boolean onCardEffecting(CardUseDTO dto, CardSettlement settlement, Player target) {return true;}
    public void onCardEffected(CardUseDTO dto, CardSettlement settlement, Player target) {}

    public void onCardEffectFinish(CardUseDTO dto, CardSettlement settlement){}
    public void onCardUsed(CardUseDTO dto, CardSettlement settlement) {}

    public void onCardSacrificing() {}
    public void onCardSacrificed() {}

    public void onCardDiscarded() {}

    public void onCardLosing() {}
    public void onCardLost() {}

    public boolean onDealingDamage(DamageSettlement settlement) { return true; }
    public void onDealtDamage(DamageSettlement settlement) {}
    public void onGettingDamage() {}
    public void onGotDamage() {}

    public void onDrawNumberCheck() {}
    public void onAlcoholLimitCheck() {}
    public void onSlashLimitCheck() {}
    public void onSlashNumberCheck() {}
    public void onHandUpperBoundCheck() {}

    public void onCardDisposeAllowCheck() {}
    public void onSkillActiveAllowCheck() {}

    public void onDistanceCheck() {}
    public void onEffectMuteCheck() {}
    public void onCardUsingQuery() {}
    public void onCardSacrificingQuery() {}
    public void onEnteringCureQuery() {}
    public void onLeavingCureQuery() {}
    public void onDying(DyingSettlement settlement) {}
    public void onRecover(DyingSettlement settlement) {}
    public void onStart() {}
    public void onPreparePhase() {}
    public void onPreEnterJudgePhase() {}
    public void onPreEnterDrawPhase() {}
    public void onEnteringDrawPhase() {}
    public void onDrawPhase() {}
    public void onLeavingDrawPhase() {}
    public void onPreEnterActivePhase() {}
    public void onEnteringActivePhase() {}
    public void onLeavingActivePhase() {}
    public void onPreEnterDiscardPhase() {}
    public void onEnteringDiscardPhase() {}
    public void onDiscardPhase() {}
    public void onLeavingDiscardPhase() {}
    public void onEndPhase() {}
    public void onYield() {}

    public void onCardBurying(CardUseDTO dto, CardSettlement settlement){}
    public void onCardBuried(CardUseDTO dto, CardSettlement settlement){}
    /* Events End*/




}
