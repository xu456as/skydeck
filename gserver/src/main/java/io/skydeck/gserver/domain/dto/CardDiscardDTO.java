package io.skydeck.gserver.domain.dto;

import io.skydeck.gserver.domain.CardBase;
import io.skydeck.gserver.domain.Player;
import lombok.Data;

@Data
public class CardDiscardDTO {
    private Player player;
    private CardBase card;
}
