package io.skydeck.gserver.domain.card.basic;

import io.skydeck.gserver.annotation.CardExecMeta;
import io.skydeck.gserver.domain.CardBase;
import io.skydeck.gserver.enums.CardNameType;
@CardExecMeta(cardNameType = CardNameType.Slash, settlement = "SlashUseSettlement")
public class CardSlash extends CardBase {
}
