package io.skydeck.gserver.domain.dto;

import io.skydeck.gserver.domain.CardBase;
import io.skydeck.gserver.domain.Player;
import lombok.Data;

import java.util.List;

@Data
public class CardDiscardDTO {
    private Player player;
    private List<CardBase> card;
}
