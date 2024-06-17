package io.skydeck.gserver.impl;

import io.skydeck.gserver.domain.Player;
import io.skydeck.gserver.domain.PloyCardSettlement;
import io.skydeck.gserver.domain.dto.CardUseDTO;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.engine.QueryManager;
import io.skydeck.gserver.engine.VisibilityManager;
import io.skydeck.gserver.i18n.TextDictionary;

import java.util.stream.Stream;

public class GlanceUseSettlement extends PloyCardSettlement {
    public static GlanceUseSettlement newOne(CardUseDTO useDTO) {
        GlanceUseSettlement settlement = new GlanceUseSettlement();
        settlement.useDTO = useDTO;
        return settlement;
    }
    @Override
    public void resolve(GameEngine e) {
        commonResolve(e, this::bizResolve);
    }

    private void bizResolve(GameEngine e, Player target) {
        Player user = useDTO.getPlayer();
        QueryManager qm = e.getQueryManager();
        VisibilityManager vm = e.getVisibilityManager();
        int idx = qm.optionQuery(user,
                Stream.of(TextDictionary.PeekHand, TextDictionary.PeekPrimaryHero, TextDictionary.PeekViceHero)
                .map(TextDictionary::i18n).toList()
        );
        switch (idx) {
            case 0:
                vm.peekCard(user, target.getHands());
                break;
            case 1:
                vm.peekHero(user, target.getPrimaryHero().getStaticHero());
                break;
            case 2:
                vm.peekHero(user, target.getViceHero().getStaticHero());
                break;
        }
    }
}
