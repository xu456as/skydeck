package io.skydeck.gserver.impl;

import io.skydeck.gserver.domain.CardSettlement;
import io.skydeck.gserver.domain.dto.CardSacrificeDTO;
import io.skydeck.gserver.domain.dto.CardUseDTO;
import io.skydeck.gserver.engine.GameEngine;

public class CardSacrificeSettlement extends CardSettlement {

    private CardSacrificeDTO cardSacrificeDTO;

    public static CardSacrificeSettlement newOne(CardSacrificeDTO cardSacrificeDTO) {
        CardSacrificeSettlement settlement = new CardSacrificeSettlement();
        settlement.cardSacrificeDTO =  cardSacrificeDTO;
        return settlement;
    }

    @Override
    public void resolve(GameEngine engine) {
        engine.onCardSacrificing();
    }
}
