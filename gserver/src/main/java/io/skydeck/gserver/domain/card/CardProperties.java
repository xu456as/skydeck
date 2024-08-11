package io.skydeck.gserver.domain.card;

import io.skydeck.gserver.domain.card.BonusProperties;
import io.skydeck.gserver.enums.*;

import java.util.Collections;
import java.util.List;

public interface CardProperties {

    public abstract String name();
    public abstract CardNameType nameType();
    public abstract Integer number();
    public default Color color() { if (suit() != null) { return suit().color();} return null; }
    public abstract Suit suit();
    public abstract CardType type();
    public abstract CardSubType subType();
    public default DamageNature nature() { return DamageNature.None; }
    public default boolean union() { return false; }
    public default boolean groupInvincible() { return false; }
    public default List<BonusProperties> bonusProperties() {return Collections.emptyList();};

}
