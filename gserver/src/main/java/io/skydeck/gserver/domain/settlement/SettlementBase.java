package io.skydeck.gserver.domain.settlement;

import io.skydeck.gserver.domain.card.CardFilter;
import io.skydeck.gserver.domain.player.Player;
import io.skydeck.gserver.engine.GameEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class SettlementBase {
    protected Map<Player, List<CardFilter>> cardDisableMap = new HashMap<>();
    public abstract void resolve(GameEngine engine);

    public void disableCard(Player target, CardFilter filter) {
        if (!cardDisableMap.containsKey(target)) {
            cardDisableMap.put(target, new ArrayList<>());
        }
        cardDisableMap.get(target).add(filter);
    }
}
