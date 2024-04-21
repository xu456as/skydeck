package io.skydeck.gserver.domain;

public abstract class AbilityBase {

    public void onCardUsing() {}
    public void onCardTargeting() {}
    public void onCardTargeted() {}
    public void onCardEffecting() {}
    public void onCardEffected() {}
    public void onCardUsed() {}

    public void onCardSacrificing() {}
    public void onCardSacrificed() {}

    public void onCardDiscarded() {}

    public void onCardLosing() {}
    public void onCardLost() {}

    public void onDealingDamage() {}
    public void onDealtDamage() {}
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

    public void onDying() {}

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
}
