package io.skydeck.gserver.domain.skill;

import io.skydeck.gserver.domain.player.Player;
import io.skydeck.gserver.domain.skill.AbilityBase;
import io.skydeck.gserver.engine.GameEngine;

public abstract class DynamicAbilityBase extends AbilityBase {
    public abstract boolean lostCheck(GameEngine e, Enum moment, Player player);
}
