package io.skydeck.gserver.impl;

import io.skydeck.gserver.domain.CardBase;
import io.skydeck.gserver.domain.Player;
import io.skydeck.gserver.domain.PloyCardSettlement;
import io.skydeck.gserver.domain.dto.CardDiscardDTO;
import io.skydeck.gserver.engine.CardFilterFactory;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.engine.QueryManager;
import io.skydeck.gserver.engine.VisibilityManager;
import io.skydeck.gserver.i18n.TextDictionary;
import io.skydeck.gserver.util.PlayerUtil;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GroupExposeUseSettlement extends PloyCardSettlement {

    @Override
    public void resolve(GameEngine e) {
        commonResolve(e, this::bizResolve);
    }

    private void bizResolve(GameEngine e, Player target) {
        QueryManager qm = e.getQueryManager();
        CardFilterFactory cff = e.getCardFilterFactory();
        VisibilityManager vm = e.getVisibilityManager();

        int idx = qm.optionQuery(target, options(), mask(target));
        switch (idx) {
            case 0:
                target.updateHealth(e, -1);
                break;
            case 1:
                CardDiscardDTO discardDTO = qm.cardDiscardQuery(target, 1, cff.gearFilter(), null);
                e.runSettlement(CardDiscardSettlement.newOne(discardDTO));
                break;
            case 2:
                target.updateHeroActive(0, true);
                break;
            case 3:
                target.updateHeroActive(1, true);
                break;
        }
    }
    private List<String> options() {
        return Stream.of(TextDictionary.DropOneHealth, TextDictionary.DiscardGear, TextDictionary.ExposePrimaryHero, TextDictionary.ExposeViceHero)
                .map(TextDictionary::i18n)
                .collect(Collectors.toList());
    }
    private int mask(Player target) {
        int mask = 1;
        if (PlayerUtil.gearCount(target) > 0) {
            mask |= 2;
        }
        if (!target.getPrimaryHero().isActive()) {
            mask |= 4;
        }
        if (!target.getViceHero().isActive()) {
            mask |= 8;
        }
        return mask;
    }
}
