package io.skydeck.gserver.domain.hero;

import io.skydeck.gserver.enums.Gender;
import io.skydeck.gserver.enums.Kingdom;

import java.util.List;

public abstract class StaticHeroBase {
    public abstract String id();
    public abstract List<String> skills();
    public abstract String name();
    public abstract String title();
    public abstract String maxHealth();
    public String initHealth() {return maxHealth();}
    public abstract Kingdom kingdom();
    public abstract List<String> buddies();
    public abstract Gender gender();
    public boolean isLord() {return false;}
}
