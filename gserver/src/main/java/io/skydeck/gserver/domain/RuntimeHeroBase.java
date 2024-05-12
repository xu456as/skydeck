package io.skydeck.gserver.domain;

import io.skydeck.gserver.engine.AbilityFactory;
import io.skydeck.gserver.engine.GameEngine;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

public abstract class RuntimeHeroBase {
    @Getter
    protected StaticHeroBase staticHero;
    private RuntimeHeroBase(StaticHeroBase hero) {
        this.staticHero = hero;
    }
    public abstract List<AbilityBase> skills();

    public static RuntimeHeroBase newOne(GameEngine engine, Player player, StaticHeroBase sHero) {
        AbilityFactory abilityFactory = engine.getAbilityFactory();
        return new RuntimeHeroBase(sHero) {
            @Override
            public List<AbilityBase> skills() {
                return Collections.emptyList();
            }
        };
    }
}
