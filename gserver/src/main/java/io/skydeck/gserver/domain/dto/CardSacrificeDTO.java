package io.skydeck.gserver.domain.dto;

import io.skydeck.gserver.domain.card.CardBase;
import io.skydeck.gserver.domain.player.Player;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardSacrificeDTO {
    private Player player;
    private CardBase card;
}
