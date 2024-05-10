package io.skydeck.gserver.impl;

import io.skydeck.gserver.domain.CardSettlement;
import io.skydeck.gserver.domain.dto.CardUseDTO;
import io.skydeck.gserver.engine.GameEngine;

public class LiquorCardUseSettlement extends CardSettlement {

    private CardUseDTO cardUseDTO;
    private boolean inDanger;

    public static LiquorCardUseSettlement newOne(CardUseDTO cardUseDTO, boolean inDanger) {
        LiquorCardUseSettlement settlement = new LiquorCardUseSettlement();
        settlement.cardUseDTO = cardUseDTO;
        settlement.inDanger = inDanger;
        return settlement;
    }

    @Override
    public void resolve(GameEngine engine) {
        //TODO
    }
}
