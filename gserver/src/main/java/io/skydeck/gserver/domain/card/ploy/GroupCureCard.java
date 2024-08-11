package io.skydeck.gserver.domain.card.ploy;

import io.skydeck.gserver.annotation.CardExecMeta;
import io.skydeck.gserver.domain.card.CardBase;
import io.skydeck.gserver.enums.CardNameType;

@CardExecMeta(cardNameType = CardNameType.GroupCurePloy, settlement = "groupCureUseSettlement")
public class GroupCureCard extends CardBase {
}
