package io.skydeck.gserver.domain.card;

import io.skydeck.gserver.domain.skill.SkillBase;
import io.skydeck.gserver.domain.player.Player;
import io.skydeck.gserver.domain.skill.AbilityBase;
import io.skydeck.gserver.engine.GameEngine;

import java.util.Collections;
import java.util.List;

public abstract class GearCardBase extends CardBase {
    protected Player owner;
    public boolean unique() {return false;}
    public int offensePoint() {
        return 0;
    }
    public int defensePoint() {
        return 0;
    }
    public int attackRange() {
        return 0;
    }
    public int extraHandQuota() { return 0;}
    public List<SkillBase> skills() { return Collections.emptyList(); }
    public List<AbilityBase> abilities() { return Collections.emptyList(); }

    public void onEnteringEquipArea(GameEngine engine, Player player) {}
    public void onEnteredEquipArea(GameEngine engine, Player player) {}
    public void onLeavingEquipArea(GameEngine engine, Player player) {}
    public void onLeavedEquipArea(GameEngine engine, Player player) {}
    public void onBurying(GameEngine engine, Player player) {}

    public void onAbilityChanged(GameEngine engine, Player player) {

    }

    public void updateOwner(Player owner) {
        this.owner = owner;
        abilities().forEach(ab -> ab.setOwner(owner));
    }

}
