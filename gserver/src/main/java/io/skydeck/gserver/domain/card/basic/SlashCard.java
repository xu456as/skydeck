package io.skydeck.gserver.domain.card.basic;

import io.skydeck.gserver.annotation.CardExecMeta;
import io.skydeck.gserver.domain.card.CardBase;
import io.skydeck.gserver.enums.CardNameType;
@CardExecMeta(cardNameType = CardNameType.Slash, settlement = "SlashUseSettlement")
public class SlashCard extends CardBase {
}
