package io.skydeck.gserver.domain.skill;

import io.skydeck.gserver.annotation.I18n;
import io.skydeck.gserver.domain.card.CardBase;
import io.skydeck.gserver.domain.dto.ProactiveActionDTO;
import io.skydeck.gserver.domain.player.Player;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.enums.AbilityTag;
import io.skydeck.gserver.impl.settlement.DamageSettlement;
import io.skydeck.gserver.impl.settlement.SlashUseSettlement;

import java.util.Collections;
import java.util.Locale;
import java.util.Set;

public abstract class AbilityBase {

    public Set<AbilityTag> tags() {return Collections.emptySet();}

    public Set<Enum> events() {
        return Collections.emptySet();
    }
    public boolean canActive(GameEngine engine, Enum event, Player player) {return false;}

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

    public void onCardUsing() {
    }

    public void onCardTargeting() {
    }

    public void onCardTargeted() {
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
    public void onODamage(GameEngine engine, DamageSettlement settlement) {
    }


    public void onDDamaged(GameEngine engine, DamageSettlement settlement) {
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

    public boolean canSelectAsCardTarget(Player player, Player target, CardBase card) {
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
