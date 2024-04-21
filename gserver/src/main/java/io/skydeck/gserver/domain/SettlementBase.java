package io.skydeck.gserver.domain;

import io.skydeck.gserver.engine.SettlementEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class SettlementBase {
    protected Map<Player, List<CardFilterIface>> cardDisableMap = new HashMap<>();
    public abstract void resolve(SettlementEngine engine);

    public void disableCard(Player target, CardFilterIface filter) {
        if (!cardDisableMap.containsKey(target)) {
            cardDisableMap.put(target, new ArrayList<>());
        }
        cardDisableMap.get(target).add(filter);
    }
}
