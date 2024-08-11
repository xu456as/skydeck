package io.skydeck.gserver.domain.card.ploy;

import io.skydeck.gserver.annotation.CardExecMeta;
import io.skydeck.gserver.enums.CardNameType;

@CardExecMeta(cardNameType = CardNameType.StealWeaponPloy, settlement = "StealWeaponUseSettlement")
public class StealWeaponCard extends ChainCard {
}
