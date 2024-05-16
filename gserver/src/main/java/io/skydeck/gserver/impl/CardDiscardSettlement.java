package io.skydeck.gserver.impl;

import io.skydeck.gserver.domain.CardBase;
import io.skydeck.gserver.domain.CardSettlement;
import io.skydeck.gserver.domain.Player;
import io.skydeck.gserver.domain.dto.CardDiscardDTO;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.enums.CardLostType;

import java.util.List;

public class CardDiscardSettlement extends CardSettlement {
    private CardDiscardDTO discardDTO;
    public static CardDiscardSettlement newOne(CardDiscardDTO discardDTO) {
        CardDiscardSettlement settlement = new CardDiscardSettlement();
        settlement.discardDTO = discardDTO;
        return settlement;
    }
    @Override
    public void resolve(GameEngine e) {
        Player player = discardDTO.getPlayer();
        List<CardBase> cardDiscard = discardDTO.getCard();
        player.removeCard(e, cardDiscard, CardLostType.Discard);
        player.getStageState().setDiscardCardCount(
                player.getStageState().getDiscardCardCount() + cardDiscard.size()
        );
        e.onCardLost(player, CardLostType.Discard, cardDiscard);
        e.onCardDiscarded(discardDTO, this);
    }
}
