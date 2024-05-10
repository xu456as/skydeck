package io.skydeck.gserver.domain;

import java.util.Objects;

public abstract class CardBase implements CardProperties, Comparable<CardBase> {
    public abstract Integer id();

    @Override
    public int compareTo(CardBase o) {
        return Integer.compare(id(), o.id());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CardBase)) {
            return false;
        }
        CardBase other = (CardBase) obj;
        return Objects.equals(id(), other.id());
    }
}
