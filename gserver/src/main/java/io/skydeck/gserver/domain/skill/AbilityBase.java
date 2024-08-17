package io.skydeck.gserver.domain.skill;

import io.skydeck.gserver.annotation.I18n;
import io.skydeck.gserver.domain.card.CardBase;
import io.skydeck.gserver.domain.dto.ProactiveActionDTO;
import io.skydeck.gserver.domain.player.Player;
import io.skydeck.gserver.domain.settlement.CardSettlement;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.enums.AbilityTag;
import io.skydeck.gserver.impl.settlement.DamageSettlement;
import io.skydeck.gserver.impl.settlement.InDangerSettlement;
import io.skydeck.gserver.impl.settlement.SlashUseSettlement;

import java.util.Collections;
import java.util.Locale;
import java.util.Set;

public abstract class AbilityBase {

    protected Player owner = null;
    public Player owner() {return owner;}
    public void setOwner(Player owner) {this.owner = owner;}
    public Set<AbilityTag> tags() {return Collections.emptySet();}

    public Set<Enum> events() {
        return Collections.emptySet();
    }
    public boolean canActive(GameEngine e, Enum event, Player player) {return false;}
    public boolean mandatory() {return false;}

    public String name() {
        return nameI18n(null);
    }
    public String nameI18n(Locale locale) {
        I18n i18n = this.getClass().getAnnotation(I18n.class);
        if (locale == Locale.CHINA) {
            if (i18n != null) {
                return i18n.zhCn();
            }
        }
        if (i18n != null) {
            return i18n.value();
        }
        return "";
    }

    public void onCardUsing(GameEngine e, Player offender, CardSettlement settlement) {
    }

    public void onOCardTargeting(GameEngine e, Player offender, Player defender, CardSettlement settlementBase) {
    }
    public void onDCardTargeting(GameEngine e, Player offender, Player defender, CardSettlement settlementBase) {
    }

    public void onOCardTargeted(GameEngine e, Player offender, Player defender, CardSettlement settlementBase) {
    }
    public void onDCardTargeted(GameEngine e, Player offender, Player defender, CardSettlement settlementBase) {
    }

    public void onCardEffecting() {
    }

    public void onCardEffected() {
    }

    public void onCardUsed() {
    }

    public void onCardSacrificing() {
    }

    public void onCardSacrificed() {
    }

    public void onCardDiscarded() {
    }

    public void onCardLosing() {
    }

    public void onCardLost(GameEngine e, Enum type, Player player) {
    }

    public void onODamaging(GameEngine engine, DamageSettlement settlement) {
    }
    public void onDDamaging(GameEngine engine, DamageSettlement settlement) {
    }
    public void onODamaged(GameEngine engine, DamageSettlement settlement) {
    }


    public void onDDamaged(GameEngine engine, DamageSettlement settlement) {
    }

    public void onDrawNumberCheck() {
    }

    public void onAlcoholLimitCheck() {
    }

    public int slashQuotaMod(int original) {
        return original;
    }

    public boolean ignoreDistance(Player target, CardBase card) {
        return false;
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

    public void onDying() {
    }

    public void onStart() {
    }

    public void onPreparePhase() {
    }

    public void onPreEnterJudgePhase() {
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

    public void onLeavingActivePhase() {
    }

    public void onPreEnterDiscardPhase() {
    }

    public void onEnteringDiscardPhase() {
    }

    public void onDiscardPhase() {
    }

    public void onLeavingDiscardPhase(GameEngine e, Player currentPlayer) {
    }

    public void onEndPhase() {
    }

    public void onYield() {
    }

    public void onJinkSucceed(GameEngine engine, SlashUseSettlement settlement, Player offender, Player defender) {

    }
    public void proactiveAction(GameEngine engine, Player player, ProactiveActionDTO proactiveActionDTO) {

    }
    public void onInDangering(GameEngine e, InDangerSettlement settlement) {

    }

    public boolean canSelectAsCardTarget(GameEngine e, Player player, Player target, CardBase card) {
        return false;
    }


    //begin extra attributes
    public int offensePoint() {
        return 0;
    }
    public int defensePoint() {
        return 0;
    }


    //end extra attributes
}
