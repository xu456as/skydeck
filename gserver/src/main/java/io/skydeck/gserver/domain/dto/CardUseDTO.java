package io.skydeck.gserver.domain.dto;


import io.skydeck.gserver.domain.CardBase;
import io.skydeck.gserver.domain.Player;
import lombok.Data;

import java.util.Map;

@Data
public class CardUseDTO {
    private Player player;
    private CardBase card;
    private Map<Player, Integer> targets;
}
