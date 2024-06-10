package io.skydeck.gserver.impl;

import io.skydeck.gserver.domain.Player;
import io.skydeck.gserver.domain.PloyCardSettlement;
import io.skydeck.gserver.engine.GameEngine;

public class GroupCureUseSettlement extends PloyCardSettlement {
    @Override
    public void resolve(GameEngine engine) {
        commonResolve(engine, this::bizResolve);
    }

    private void bizResolve(GameEngine e, Player target) {
        target.updateHealth(e, 1);
    }
}
