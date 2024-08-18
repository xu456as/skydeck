package io.skydeck.gserver.impl.settlement;

import io.skydeck.gserver.domain.card.CardBase;
import io.skydeck.gserver.domain.player.Player;
import io.skydeck.gserver.domain.settlement.PloyCardSettlement;
import io.skydeck.gserver.domain.dto.CardDiscardDTO;
import io.skydeck.gserver.domain.dto.CardUseDTO;
import io.skydeck.gserver.engine.CardFilterFactory;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.engine.QueryManager;
import io.skydeck.gserver.engine.VisibilityManager;

import java.util.Collections;

public class FireUseSettlement extends PloyCardSettlement {

    public static FireUseSettlement newOne(CardUseDTO useDTO) {
        FireUseSettlement settlement = new FireUseSettlement();
        settlement.useDTO = useDTO;
        return settlement;
    }
    private void bizResolve(GameEngine e, Player target) {
        QueryManager queryManager = e.getQueryManager();
        CardFilterFactory cff = e.getCardFilterFactory();
        VisibilityManager vbm = e.getVisibilityManager();
        CardBase showingCard = queryManager.handCardPickQuery(target);
        vbm.showHandCards(target, Collections.singletonList(showingCard));
        CardDiscardDTO cardDiscardDTO = queryManager.handCardDiscardQuery(useDTO.getPlayer(),
                1, cff.suitFilter(showingCard.suit()), null);
        if (cardDiscardDTO != null) {
            e.runSettlement(CardDiscardSettlement.newOne(cardDiscardDTO));
            e.runSettlement(DamageSettlement.newOne(useDTO.getPlayer(), target,
                    1, useDTO.getCard().nature(), useDTO.getCard(), this));
        }
    }
    @Override
    public void resolve(GameEngine e) {
        commonResolve(e, this::bizResolve);
    }
}
