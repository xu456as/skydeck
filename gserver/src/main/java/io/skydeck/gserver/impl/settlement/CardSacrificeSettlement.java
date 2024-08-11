package io.skydeck.gserver.impl.settlement;

import io.skydeck.gserver.domain.card.CardBase;
import io.skydeck.gserver.domain.settlement.CardSettlement;
import io.skydeck.gserver.domain.player.Player;
import io.skydeck.gserver.domain.dto.CardSacrificeDTO;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.enums.CardLostType;
import lombok.Getter;

import java.util.Collections;

public class CardSacrificeSettlement extends CardSettlement {
    @Getter
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
        e.onCardLost(player, CardLostType.Sacrifice, Collections.singletonList(cardSacrificeDTO.getCard()));
        e.onCardSacrificing(cardSacrificeDTO, this);
        e.onCardSacrificed(cardSacrificeDTO, this);
    }
}
