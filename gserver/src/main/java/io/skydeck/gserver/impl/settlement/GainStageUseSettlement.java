package io.skydeck.gserver.impl.settlement;

import io.skydeck.gserver.annotation.I18n;
import io.skydeck.gserver.domain.dto.ActiveCheckDTO;
import io.skydeck.gserver.domain.skill.DynamicAbilityBase;
import io.skydeck.gserver.domain.player.Player;
import io.skydeck.gserver.domain.settlement.PloyCardSettlement;
import io.skydeck.gserver.domain.dto.CardDiscardDTO;
import io.skydeck.gserver.domain.dto.CardUseDTO;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.engine.QueryManager;
import io.skydeck.gserver.enums.PhaseEvent;

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
        public boolean canActive(GameEngine engine, Enum event, ActiveCheckDTO activeCheck) {
            return events().contains(event) && activeCheck.getSubject() == target;
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
