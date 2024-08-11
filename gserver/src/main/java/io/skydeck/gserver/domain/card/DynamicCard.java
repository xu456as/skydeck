package io.skydeck.gserver.domain.card;

import io.skydeck.gserver.domain.card.CardBase;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class DynamicCard extends CardBase {
    private static final AtomicInteger ID_GEN = new AtomicInteger(1000);

    private final int dynamicId;
    public DynamicCard() {
        this.dynamicId = ID_GEN.getAndIncrement();
    }
    @Override
    public Integer id() { return dynamicId; }
    public abstract boolean virtual();
    public abstract List<CardBase> originCards();

}