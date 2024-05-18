package io.skydeck.gserver.impl;

import io.skydeck.gserver.domain.Player;
import io.skydeck.gserver.domain.PloyCardSettlement;
import io.skydeck.gserver.domain.dto.CardDiscardDTO;
import io.skydeck.gserver.domain.dto.CardSacrificeDTO;
import io.skydeck.gserver.domain.dto.CardUseDTO;
import io.skydeck.gserver.engine.CardFilterFactory;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.engine.QueryManager;

import java.util.HashMap;
import java.util.Map;

public class DuelUseSettlement extends PloyCardSettlement {

    private Map<Player, Integer> slashRequireMap = new HashMap<>();

    public static DuelUseSettlement newOne(CardUseDTO dto) {
        DuelUseSettlement settlement = new DuelUseSettlement();
        settlement.useDTO = dto;
        return settlement;
    }

    public void requireMoreSlash(Player target) {
        slashRequireMap.put(target, slashRequireMap.getOrDefault(target, 1) + 1);
    }


    @Override
    public void resolve(GameEngine e) {
        commonResolve(e, this::bizResolve);
    }

    private void bizResolve(GameEngine e, Player target) {
        QueryManager qm = e.getQueryManager();
        CardFilterFactory cff = e.getCardFilterFactory();
        Player[] duelers = new Player[]{useDTO.getPlayer(), target};
        int ans = 1;
        boolean end = false;
        while (!duelers[0].isDead() && duelers[1].isDead() && !end) {
            int slashRequireCount = slashRequireMap.getOrDefault(duelers[ans], 1);
            for (int i = 0; i < slashRequireCount && !end; i++) {
                CardSacrificeDTO sacrificeDTO = qm.cardSacrificeQuery(duelers[ans], cff.slashFilter(), null);
                if (sacrificeDTO == null) {
                    end = true;
                }
            }
            if (end) {
                e.runSettlement(DamageSettlement.newOne(duelers[(ans + 1) % 2], duelers[ans],
                        1, useDTO.getCard().nature(),
                        useDTO.getCard()));
            } else {
                ++ans;
            }
        }
    }
}
