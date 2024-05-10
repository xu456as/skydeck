package io.skydeck.gserver.domain;

import io.skydeck.gserver.enums.Gender;
import io.skydeck.gserver.enums.Kingdom;

import java.util.List;

public abstract class HeroBase {
    public abstract Integer id();
    public abstract List<SkillBase> skills();
    public abstract String name();
    public abstract String title();
    public abstract int maxHealth();
    public abstract int initHealth();
    public abstract Kingdom kingdom();
    public abstract List<String> buddies();
    public abstract Gender gender();
    public boolean isLord() {return false;}
}
