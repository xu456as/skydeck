package io.skydeck.gserver.impl;

import io.skydeck.gserver.domain.PloyCardSettlement;
import io.skydeck.gserver.domain.dto.CardUseDTO;
import io.skydeck.gserver.engine.GameEngine;

public class ThriveUseSettlement extends PloyCardSettlement {
    private CardUseDTO useDTO;

    public static ThriveUseSettlement newOne(CardUseDTO cardUseDTO) {
        ThriveUseSettlement settlement = new ThriveUseSettlement();
        settlement.useDTO = cardUseDTO;
        return settlement;
    }

    //TODO
    @Override
    public void resolve(GameEngine e) {

    }
}
