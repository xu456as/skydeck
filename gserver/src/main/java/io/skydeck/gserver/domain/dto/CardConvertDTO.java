package io.skydeck.gserver.domain.dto;

import io.skydeck.gserver.domain.card.CardBase;
import io.skydeck.gserver.domain.player.Player;
import io.skydeck.gserver.enums.CardNameType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CardConvertDTO {
    private Player user;
    private List<CardBase> originalCards;
    private CardNameType outputCardName;
}
