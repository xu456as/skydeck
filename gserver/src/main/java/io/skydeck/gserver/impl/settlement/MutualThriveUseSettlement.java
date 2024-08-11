package io.skydeck.gserver.impl.settlement;

import io.skydeck.gserver.domain.card.CardBase;
import io.skydeck.gserver.domain.player.Player;
import io.skydeck.gserver.domain.settlement.PloyCardSettlement;
import io.skydeck.gserver.domain.dto.CardTransferContext;
import io.skydeck.gserver.domain.dto.CardUseDTO;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.engine.PublicCardResManager;

import java.util.List;

public class MutualThriveUseSettlement extends PloyCardSettlement {
    public static MutualThriveUseSettlement newOne(CardUseDTO useDTO) {
        MutualThriveUseSettlement settlement = new MutualThriveUseSettlement();
        settlement.useDTO = useDTO;
        return settlement;
    }
    @Override
    public void resolve(GameEngine e) {
        commonResolve(e, this::bizResolve);
    }
    private void bizResolve(GameEngine e, Player target) {
        PublicCardResManager pcrManager = e.getPcrManager();
        List<CardBase> cardOne = pcrManager.pollDeckTop(1);
        target.acquireHand(e, CardTransferContext.draw(), cardOne);
        List<CardBase> cardThree = pcrManager.pollDeckTop(3);
        useDTO.getPlayer().acquireHand(e, CardTransferContext.draw(), cardThree);
    }
}
