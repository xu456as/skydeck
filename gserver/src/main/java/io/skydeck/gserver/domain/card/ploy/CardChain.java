package io.skydeck.gserver.domain.card.ploy;

import io.skydeck.gserver.annotation.CardExecMeta;
import io.skydeck.gserver.domain.CardBase;
import io.skydeck.gserver.enums.CardNameType;
import io.skydeck.gserver.enums.CardSubType;
import io.skydeck.gserver.enums.CardType;

@CardExecMeta(cardNameType = CardNameType.ChainPloy, settlement = "ChainUseSettlement")
public class CardChain extends CardBase {
}
