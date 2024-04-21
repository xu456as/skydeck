package io.skydeck.gserver.domain;

import java.util.List;

public abstract class DynamicCard extends CardBase {
    public Integer id() { return null; }
    public abstract boolean virtual();
    public abstract List<CardBase> originCards();
}