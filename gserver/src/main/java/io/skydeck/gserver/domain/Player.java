package io.skydeck.gserver.domain;

import io.skydeck.gserver.domain.dto.CardTransferContext;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.engine.QueryManager;
import io.skydeck.gserver.enums.DuckEvent;
import io.skydeck.gserver.enums.Gender;
import io.skydeck.gserver.enums.Kingdom;
import io.skydeck.gserver.impl.DamageSettlement;
import io.skydeck.gserver.impl.SlashCardUseSettlement;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
public class Player implements Comparable<Player> {
    private int id;
    private String name;
    private int health;
    private int maxHealth;
    private boolean dead = false;
    private Gender gender = Gender.None;
    private Kingdom kingdom = Kingdom.Unknown;
    private HeroBase primaryHero;
    private HeroBase viceHero;
    private List<GearCardBase> equips;
    private List<CardBase> judges;
    private List<CardBase> hands;
    private List<TokenBase> tokens;
    private List<SkillBase> skills;
    private List<AbilityBase> abilities;
    private boolean expelled = false;
    private boolean chained = false;
    private StageState stageState;

    public void clearExtraResource(GameEngine engine) {
        //TODO
    }

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

    public void onPreStart(GameEngine gameEngine) {
    }

    public void onPostYield(GameEngine gameEngine) {
    }

    public boolean canSelectAsCardTarget(Player target, CardBase card) {
        return getSkills().stream().anyMatch(skill -> skill.canSelectAsCardTarget(this, target, card));
    }

    public int attackRange() {
        //TODO
        return 1;
    }
    public int offensePoint() {
        //TODO
        return 0;
    }
    public int defensePoint() {
        //TODO
        return 0;
    }

    public void onJinkSucceed(GameEngine engine, SlashCardUseSettlement settlement, Player offender, Player defender) {
        List<AbilityBase> abilityBaseList = abilityList().stream()
                .filter(abilityBase -> abilityBase.events().contains(DuckEvent.JinkUseDuck))
                .filter(abilityBase -> abilityBase.canActive(engine, DuckEvent.JinkUseDuck, this))
                .collect(Collectors.toList());
        QueryManager queryManager = engine.getQueryManager();
        while (!abilityBaseList.isEmpty()) {
            AbilityBase abilityBase = queryManager.abilitiesQuery(this, abilityBaseList);
            if (abilityBase == null) {
                break;
            }
            abilityBaseList.remove(abilityBase);
            abilityBase.onJinkSucceed(engine, settlement, offender, defender);
        }
    }


    private List<AbilityBase> abilityList() {
        List<AbilityBase> abilityList = new ArrayList<>();
        abilityList.addAll(skills);
        abilityList.addAll(abilities);
        abilityList.addAll(primaryHero.skills());
        abilityList.addAll(viceHero.skills());
        if (!CollectionUtils.isEmpty(equips)) {
            abilityList.addAll(equips.stream().flatMap(e -> e.abilities().stream()).toList());
        }

        return abilityList;
    }

    public void onDDamaged(GameEngine engine, DamageSettlement settlement) {
//        getSkills().forEach(skill -> skill.onDDamaged(settlement));
        //TODO
    }

    public void acquireHand(CardTransferContext ctc, List<CardBase> cards) {
        //TODO add alarm
        hands.addAll(cards);
    }
}
