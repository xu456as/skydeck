package io.skydeck.gserver.domain.card;

import io.skydeck.gserver.enums.DamageNature;
import io.skydeck.gserver.enums.Suit;

public record CardParseRcd(int number, Suit suit, DamageNature nature, boolean union, boolean groupInvincible, String bonusProperties) {
}
