package io.skydeck.gserver.domain;

import io.skydeck.gserver.engine.AbilityFactory;
import io.skydeck.gserver.engine.GameEngine;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

public abstract class RuntimeHeroBase {
    @Getter
    protected Player player;
    @Getter
    protected StaticHeroBase staticHero;
    @Getter
    @Setter
    protected boolean active = false;
    private RuntimeHeroBase(Player player, StaticHeroBase hero) {
        this.player = player;
        this.staticHero = hero;
    }
    public abstract List<AbilityBase> skills();

    public static RuntimeHeroBase newOne(GameEngine engine, Player player, StaticHeroBase sHero) {
        AbilityFactory abilityFactory = engine.getAbilityFactory();
        return new RuntimeHeroBase(player, sHero) {
            @Override
            public List<AbilityBase> skills() {
                return Collections.emptyList();
            }
        };
    }
}
