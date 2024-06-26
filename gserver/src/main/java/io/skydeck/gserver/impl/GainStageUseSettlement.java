package io.skydeck.gserver.impl;

import io.skydeck.gserver.annotation.I18n;
import io.skydeck.gserver.domain.AbilityBase;
import io.skydeck.gserver.domain.DynamicAbilityBase;
import io.skydeck.gserver.domain.Player;
import io.skydeck.gserver.domain.PloyCardSettlement;
import io.skydeck.gserver.domain.dto.CardDiscardDTO;
import io.skydeck.gserver.domain.dto.CardUseDTO;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.engine.QueryManager;
import io.skydeck.gserver.enums.PhaseEvent;
import io.skydeck.gserver.i18n.TextDictionary;

import java.util.Collections;
import java.util.Locale;
import java.util.Set;

public class GainStageUseSettlement extends PloyCardSettlement {

    public static GainStageUseSettlement newOne(CardUseDTO useDTO) {
        GainStageUseSettlement settlement = new GainStageUseSettlement();
        settlement.useDTO = useDTO;
        return settlement;
    }

    @Override
    public void resolve(GameEngine engine) {
        commonResolve(engine, this::bizResolve);
    }
    private void bizResolve(GameEngine e, Player target) {
        GainStageAbility ability = new GainStageAbility(target);
        target.addDynamicAbility(ability);
        e.endActivePhase();
        System.out.println(ability.nameI18n(Locale.CHINA));
    }

    @I18n(value = "gainStageAbility")
    private class GainStageAbility extends DynamicAbilityBase {

        private Player target;

        private GainStageAbility(Player target) {
            this.target = target;
        }

        @Override
        public boolean lostCheck(GameEngine eng, Enum moment, Player player) {
            return player == target && moment == PhaseEvent.Yield;
        }

        @Override
        public Set<Enum> events() {
            return Collections.singleton(PhaseEvent.LeavingDiscardPhase);
        }

        @Override
        public boolean canActive(GameEngine engine, Enum event, Player player) {
            return events().contains(event) && player == target;
        }

        @Override
        public void onLeavingDiscardPhase(GameEngine eng, Player currentPlayer) {
            QueryManager qm = eng.getQueryManager();
            CardDiscardDTO discardDTO = qm.handCardDiscardQuery(target, 1, null, Collections.emptyList());
            if (discardDTO != null) {
                eng.runSettlement(CardDiscardSettlement.newOne(discardDTO));
                eng.yieldStageToPlayer(target);
            }
        }
    }
}
