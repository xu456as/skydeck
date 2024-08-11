package io.skydeck.gserver.domain.card.ploy;

import io.skydeck.gserver.annotation.CardExecMeta;
import io.skydeck.gserver.domain.card.CardBase;
import io.skydeck.gserver.enums.CardNameType;

@CardExecMeta(cardNameType = CardNameType.GlancePloy, settlement = "glanceUseSettlement")
public class GlanceCard extends CardBase {
}
