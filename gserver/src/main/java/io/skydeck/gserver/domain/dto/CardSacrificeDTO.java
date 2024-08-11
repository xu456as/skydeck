package io.skydeck.gserver.domain.dto;

import io.skydeck.gserver.domain.card.CardBase;
import io.skydeck.gserver.domain.player.Player;
import lombok.Data;

@Data
public class CardSacrificeDTO {
    private Player player;
    private CardBase card;
}
