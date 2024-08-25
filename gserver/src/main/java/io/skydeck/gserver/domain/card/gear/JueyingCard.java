package io.skydeck.gserver.domain.card.gear;

import io.skydeck.gserver.annotation.CardExecMeta;
import io.skydeck.gserver.domain.card.GearCardBase;
import io.skydeck.gserver.enums.CardNameType;

@CardExecMeta(cardNameType = CardNameType.Jueying, settlement = "GearCardSettlement")
public class JueyingCard extends GearCardBase {

    @Override
    public int defensePoint() {
        return 1;
    }
}
