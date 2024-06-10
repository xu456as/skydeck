package io.skydeck.gserver.impl;

import io.skydeck.gserver.domain.CardBase;
import io.skydeck.gserver.domain.CardSettlement;
import io.skydeck.gserver.domain.Player;
import io.skydeck.gserver.domain.dto.CardDiscardDTO;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.enums.CardLostType;
import lombok.Getter;

import java.util.List;

public class CardDiscardSettlement extends CardSettlement {
    @Getter
    private CardDiscardDTO discardDTO;
    public static CardDiscardSettlement newOne(CardDiscardDTO discardDTO) {
        CardDiscardSettlement settlement = new CardDiscardSettlement();
        settlement.discardDTO = discardDTO;
        return settlement;
    }
    @Override
    public void resolve(GameEngine e) {
        Player offender = discardDTO.getOffender();
        Player defender = discardDTO.getDefender();
        List<CardBase> cardDiscard = e.onCardLosing(discardDTO.getDefender(), CardLostType.Stolen, discardDTO.getCard());
        discardDTO.setCard(cardDiscard);
        e.onCardDiscarding(discardDTO, this);
        defender.removeCard(e, cardDiscard, CardLostType.Discard);
        e.onCardLost(defender, CardLostType.Discard, cardDiscard);
        e.onCardDiscarded(discardDTO, this);
    }
}
