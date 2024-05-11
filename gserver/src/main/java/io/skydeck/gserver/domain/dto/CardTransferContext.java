package io.skydeck.gserver.domain.dto;

import io.skydeck.gserver.domain.Player;
import io.skydeck.gserver.enums.CardAcquireWay;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CardTransferContext {
    private CardAcquireWay acquireWay;
    private Player source;

}
