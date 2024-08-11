package io.skydeck.gserver.impl.settlement;

import io.skydeck.gserver.domain.card.CardBase;
import io.skydeck.gserver.domain.player.Player;
import io.skydeck.gserver.domain.settlement.PloyCardSettlement;
import io.skydeck.gserver.domain.dto.CardDiscardDTO;
import io.skydeck.gserver.domain.dto.CardTransferContext;
import io.skydeck.gserver.domain.dto.CardUseDTO;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.engine.PublicCardResManager;
import io.skydeck.gserver.engine.QueryManager;

import java.util.List;

public class OptimizeUseSettlement extends PloyCardSettlement {

    public static OptimizeUseSettlement newOne(CardUseDTO useDTO) {
        OptimizeUseSettlement settlement = new OptimizeUseSettlement();
        settlement.useDTO = useDTO;
        return settlement;
    }
    @Override
    public void resolve(GameEngine engine) {
        commonResolve(engine, this::bizResolve);
    }
    private void bizResolve(GameEngine e, Player target) {
        PublicCardResManager pcrManager = e.getPcrManager();
        QueryManager queryManager = e.getQueryManager();
        List<CardBase> cards = pcrManager.pollDeckTop(2);
        target.acquireHand(e, CardTransferContext.draw(), cards);
        CardDiscardDTO discardDTO = queryManager.cardDiscardQuery(target, 2, null, null);
        CardDiscardSettlement discardSettlement = CardDiscardSettlement.newOne(discardDTO);
        e.runSettlement(discardSettlement);
    }
}
