package io.skydeck.gserver.domain;

import io.skydeck.gserver.enums.*;

public interface CardProperties {
    public abstract String name();
    public abstract Integer number();
    public abstract Color color();
    public abstract Suit suit();
    public abstract CardType type();
    public abstract CardSubType subType();
    public default DamageNature nature() { return DamageNature.None; }
}
