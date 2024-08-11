package io.skydeck.gserver.impl.settlement;

import io.skydeck.gserver.domain.card.CardBase;
import io.skydeck.gserver.domain.player.Player;
import io.skydeck.gserver.domain.settlement.PloyCardSettlement;
import io.skydeck.gserver.domain.dto.CardTransferContext;
import io.skydeck.gserver.domain.dto.CardUseDTO;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.engine.PublicCardResManager;

import java.util.List;

public class MassiveChainUseSettlement extends PloyCardSettlement {

    public static MassiveChainUseSettlement newOne(CardUseDTO useDTO) {
        MassiveChainUseSettlement settlement = new MassiveChainUseSettlement();
        settlement.useDTO = useDTO;
        return settlement;
    }

    @Override
    public void resolve(GameEngine e) {
        commonResolve(e, this::bizResolve);
    }
    private void bizResolve(GameEngine e, Player target) {
        if (target.isChained()) {
            PublicCardResManager pcrManager = e.getPcrManager();
            List<CardBase> card = pcrManager.pollDeckTop(1);
            target.acquireHand(e, CardTransferContext.draw(), card);
        } else {
            target.setChained(true);
        }
    }
}
