package io.skydeck.gserver.domain.card;

import java.util.concurrent.atomic.AtomicInteger;

public class CardIdGenUtil {
    private static final AtomicInteger idInc = new AtomicInteger(0);

    public static int genId() {
        return idInc.getAndIncrement();
    }
}
