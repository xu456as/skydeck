package io.skydeck.gserver.domain;

import io.skydeck.gserver.enums.Gender;
import io.skydeck.gserver.enums.Kingdom;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@Data
public class Player implements Comparable<Player> {
    private int id;
    private String name;
    private int health;
    private int maxHealth;
    private boolean dead = false;
    private Gender gender = Gender.None;
    private Kingdom kingdom = Kingdom.Unknown;
    private Object primaryHero;
    private Object viceHero;
    private List<CardBase> equips;
    private List<CardBase> judges;
    private List<CardBase> hands;
    private List<TokenBase> tokens;
    private List<SkillBase> skills;
    private List<AbilityBase> abilities;
    private boolean inLimbo = false;
    private boolean chained = false;

    @Override
    public int compareTo(Player o) {
        return Integer.compare(id, o.id);
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Player)) {
            return false;
        }
        Player other = (Player) obj;
        return Objects.equals(id, other.id);
    }
}
