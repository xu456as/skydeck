package io.skydeck.gserver.domain.card.gear;

import io.skydeck.gserver.annotation.CardExecMeta;
import io.skydeck.gserver.domain.card.GearCardBase;
import io.skydeck.gserver.enums.CardNameType;

@CardExecMeta(cardNameType = CardNameType.Chitu, settlement = "GearCardSettlement")
public class ChituCard extends GearCardBase {
    @Override
    public int offensePoint() {
        return 1;
    }
}
