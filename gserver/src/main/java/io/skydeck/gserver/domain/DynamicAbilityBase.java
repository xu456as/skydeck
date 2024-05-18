package io.skydeck.gserver.domain;

import io.skydeck.gserver.engine.GameEngine;

import java.util.Set;

public abstract class DynamicAbilityBase extends AbilityBase {
    public abstract boolean lostCheck(GameEngine e, Enum moment, Player player);
}
