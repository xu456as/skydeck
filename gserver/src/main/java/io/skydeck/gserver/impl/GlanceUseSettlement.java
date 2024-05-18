package io.skydeck.gserver.impl;

import io.skydeck.gserver.domain.Player;
import io.skydeck.gserver.domain.PloyCardSettlement;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.engine.QueryManager;
import io.skydeck.gserver.engine.VisibilityManager;
import io.skydeck.gserver.i18n.TextDictionary;

public class GlanceUseSettlement extends PloyCardSettlement {
    @Override
    public void resolve(GameEngine e) {
        commonResolve(e, this::bizResolve);
    }

    private void bizResolve(GameEngine e, Player target) {
        Player user = useDTO.getPlayer();
        QueryManager qm = e.getQueryManager();
        VisibilityManager vm = e.getVisibilityManager();
        int idx = qm.optionQuery(user, TextDictionary.glanceOption());
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
