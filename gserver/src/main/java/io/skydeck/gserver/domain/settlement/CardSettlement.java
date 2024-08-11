package io.skydeck.gserver.domain.settlement;

import io.skydeck.gserver.domain.dto.CardUseDTO;
import lombok.Getter;

public abstract class CardSettlement extends SettlementBase {
    @Getter
    protected CardUseDTO useDTO;

}
