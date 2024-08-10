package io.skydeck.gserver.domain.card.basic;

import io.skydeck.gserver.annotation.CardExecMeta;
import io.skydeck.gserver.domain.CardBase;
import io.skydeck.gserver.enums.CardNameType;

@CardExecMeta(cardNameType = CardNameType.Liquor, settlement = "LiquorUseSettlement")
public class CardLiquor extends CardBase {
}
