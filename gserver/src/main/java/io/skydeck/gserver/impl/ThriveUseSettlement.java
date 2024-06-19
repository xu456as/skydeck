package io.skydeck.gserver.impl;

import io.skydeck.gserver.domain.CardBase;
import io.skydeck.gserver.domain.Player;
import io.skydeck.gserver.domain.PloyCardSettlement;
import io.skydeck.gserver.domain.dto.CardTransferContext;
import io.skydeck.gserver.domain.dto.CardUseDTO;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.engine.PublicCardResManager;

import java.util.List;

public class ThriveUseSettlement extends PloyCardSettlement {
    private CardUseDTO useDTO;

    public static ThriveUseSettlement newOne(CardUseDTO cardUseDTO) {
        ThriveUseSettlement settlement = new ThriveUseSettlement();
        settlement.useDTO = cardUseDTO;
        return settlement;
    }

    @Override
    public void resolve(GameEngine e) {
        commonResolve(e, this::bizResolve);
    }
    private void bizResolve(GameEngine e, Player target) {
        PublicCardResManager pcrManager = e.getPcrManager();
        List<CardBase> cards = pcrManager.pollDeckTop(2);
        target.acquireHand(e, CardTransferContext.draw(), cards);
    }
}
