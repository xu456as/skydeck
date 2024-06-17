package io.skydeck.gserver.domain;

import io.skydeck.gserver.enums.*;

import java.util.Collections;
import java.util.List;

public interface CardProperties {

    public abstract String name();
    public abstract CardNameType nameType();
    public abstract Integer number();
    public abstract Color color();
    public abstract Suit suit();
    public abstract CardType type();
    public abstract CardSubType subType();
    public default boolean union() { return false; }
    public default boolean groupInvincible() { return false; }
    public default List<BonusProperties> bonusProperties() {return Collections.emptyList();};

    public default DamageNature nature() { return DamageNature.None; }

}
