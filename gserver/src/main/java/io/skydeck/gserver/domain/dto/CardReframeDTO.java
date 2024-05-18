package io.skydeck.gserver.domain.dto;

import io.skydeck.gserver.domain.CardBase;
import io.skydeck.gserver.domain.Player;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CardReframeDTO {
    private Player player;
    private List<CardBase> card;
}
