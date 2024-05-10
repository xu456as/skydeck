package io.skydeck.gserver.domain.dto;

import io.skydeck.gserver.domain.CardBase;
import io.skydeck.gserver.domain.Player;
import lombok.Data;

import java.util.List;

@Data
public class CardSacrificeDTO {
    private Player player;
    private CardBase card;
}
