package io.skydeck.gserver.impl;

import io.skydeck.gserver.domain.AbilityBase;
import io.skydeck.gserver.domain.DynamicAbilityBase;
import io.skydeck.gserver.domain.Player;
import io.skydeck.gserver.domain.PloyCardSettlement;
import io.skydeck.gserver.domain.dto.CardDiscardDTO;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.engine.QueryManager;
import io.skydeck.gserver.enums.PhaseEvent;
import io.skydeck.gserver.i18n.TextDictionary;

import java.util.Collections;
import java.util.Set;

public class GainStageUseSettlement extends PloyCardSettlement {
    @Override
    public void resolve(GameEngine engine) {
        commonResolve(engine, this::bizResolve);
    }
    private void bizResolve(GameEngine e, Player target) {
        target.addDynamicAbility(new DynamicAbilityBase() {
            @Override
            public boolean lostCheck(GameEngine eng, Enum moment, Player player) {
                return player == target && moment == PhaseEvent.Yield;
            }

            @Override
            public String name() {
                return TextDictionary.GainStagePloy.name();
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
        });
        e.endActivePhase();
    }
}
