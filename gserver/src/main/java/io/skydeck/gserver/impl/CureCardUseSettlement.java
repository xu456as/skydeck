package io.skydeck.gserver.impl;

import io.skydeck.gserver.domain.CardSettlement;
import io.skydeck.gserver.domain.dto.CardUseDTO;
import io.skydeck.gserver.engine.GameEngine;

public class CureCardUseSettlement extends CardSettlement {
    private CardUseDTO cardUseDTO;

    public static CureCardUseSettlement newOne(CardUseDTO cureCardUse) {
        CureCardUseSettlement settlement = new CureCardUseSettlement();
        settlement.cardUseDTO =  cureCardUse;
        return settlement;
    }
    @Override
    public void resolve(GameEngine engine) {
        //TODO
    }
}
