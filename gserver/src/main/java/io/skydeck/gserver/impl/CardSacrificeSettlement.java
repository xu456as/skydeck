package io.skydeck.gserver.impl;

import io.skydeck.gserver.domain.CardBase;
import io.skydeck.gserver.domain.CardSettlement;
import io.skydeck.gserver.domain.Player;
import io.skydeck.gserver.domain.dto.CardSacrificeDTO;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.enums.CardLostType;

import java.util.Collections;

public class CardSacrificeSettlement extends CardSettlement {

    private CardSacrificeDTO cardSacrificeDTO;

    public static CardSacrificeSettlement newOne(CardSacrificeDTO cardSacrificeDTO) {
        CardSacrificeSettlement settlement = new CardSacrificeSettlement();
        settlement.cardSacrificeDTO =  cardSacrificeDTO;
        return settlement;
    }

    @Override
    public void resolve(GameEngine e) {
        Player player = cardSacrificeDTO.getPlayer();
        CardBase cardS = cardSacrificeDTO.getCard();
        player.removeCard(e, Collections.singletonList(cardS), CardLostType.Sacrifice);
        player.getStageState().setSacrificeCardCount(
                player.getStageState().getSacrificeCardCount() + 1
        );
        e.onCardLost(player, CardLostType.Sacrifice, Collections.singletonList(cardSacrificeDTO.getCard()));
        e.onCardSacrificing(cardSacrificeDTO, this);
        e.onCardSacrificed(cardSacrificeDTO, this);
    }
}
