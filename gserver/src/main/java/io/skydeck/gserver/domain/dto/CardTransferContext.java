package io.skydeck.gserver.domain.dto;

import io.skydeck.gserver.domain.player.Player;
import io.skydeck.gserver.enums.CardAcquireWay;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CardTransferContext {
    private CardAcquireWay acquireWay;
    private Player source;

    public static CardTransferContext draw() {
        return CardTransferContext.builder().acquireWay(CardAcquireWay.Draw).build();
    }

}
