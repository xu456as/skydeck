package io.skydeck.gserver.impl;

import io.skydeck.gserver.domain.CardSettlement;
import io.skydeck.gserver.domain.dto.CardUseDTO;
import io.skydeck.gserver.engine.GameEngine;

public class JinkCardUseSettlement extends CardSettlement {
    private CardUseDTO jinkUseDTO;
    private CardUseDTO slashUseDTO;

    public static JinkCardUseSettlement newOne(CardUseDTO slashUseDTO, CardUseDTO jinkUseDTO) {
        JinkCardUseSettlement settlement = new JinkCardUseSettlement();
        settlement.slashUseDTO = slashUseDTO;
        settlement.jinkUseDTO = jinkUseDTO;
        return settlement;
    }

    @Override
    public void resolve(GameEngine engine) {

    }
}
