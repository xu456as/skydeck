package io.skydeck.gserver.domain.skill;

import io.skydeck.gserver.annotation.I18n;
import io.skydeck.gserver.domain.card.CardBase;
import io.skydeck.gserver.domain.dto.ActiveCheckDTO;
import io.skydeck.gserver.domain.dto.ProactiveActionDTO;
import io.skydeck.gserver.domain.player.Player;
import io.skydeck.gserver.domain.settlement.CardSettlement;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.enums.AbilityTag;
import io.skydeck.gserver.enums.Suit;
import io.skydeck.gserver.impl.settlement.DamageSettlement;
import io.skydeck.gserver.impl.settlement.InDangerSettlement;
import io.skydeck.gserver.impl.settlement.SlashUseSettlement;
import lombok.Getter;

import java.util.Collections;
import java.util.Locale;
import java.util.Set;

public abstract class AbilityBase {
    @Getter
    protected final AbilityStageState stageState = new AbilityStageState();


    protected Player owner = null;
    public Player owner() {return owner;}
    public void setOwner(Player owner) {this.owner = owner;}
    public Set<AbilityTag> tags() {return Collections.emptySet();}

    public Set<Enum> events() {
        return Collections.emptySet();
    }
    public boolean canActive(GameEngine e, Enum event, ActiveCheckDTO activeCheck) {return false;}
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

    public boolean onCardEffecting(CardSettlement settlement, Player target) {
        return true;
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

    public int slashQuotaMod(GameEngine e, int original) {
        return original;
    }
    public int kingdomVolMod(GameEngine e, int original) {
        return original;
    }
    public int drawQuotaMod(GameEngine e, int original) {
        return original;
    }

    public int handQuotaMod(GameEngine e, int original) {return original;}

    public Suit cardSuitMod(GameEngine e, CardBase original) {
        return original.suit();
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

    public void onEnteringActivePhase(GameEngine e, Player subject) {
    }

    public void onLeavingActivePhase(GameEngine e, Player subject) {
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

    public void onUpdatedChained(GameEngine e, Player player) {
    }
    public boolean onUpdatingChained(GameEngine e, Player player) {
        return true;
    }
    //end extra attributes


}
