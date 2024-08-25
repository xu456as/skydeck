package io.skydeck.gserver.domain.card.gear;

import io.skydeck.gserver.annotation.CardExecMeta;
import io.skydeck.gserver.domain.card.GearCardBase;
import io.skydeck.gserver.enums.CardNameType;

@CardExecMeta(cardNameType = CardNameType.Liulongcanjia, settlement = "GearCardSettlement")
public class LiulongcanjiaCard extends GearCardBase {
    @Override
    public int offensePoint() {
        return 1;
    }

    @Override
    public int defensePoint() {
        return 1;
    }
}
