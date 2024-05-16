package io.skydeck.gserver.domain;

import io.skydeck.gserver.domain.dto.CardTransferContext;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.enums.*;
import io.skydeck.gserver.impl.DamageSettlement;
import io.skydeck.gserver.impl.SlashCardUseSettlement;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
public class Player implements Comparable<Player> {
    private int id;
    private String name;
    private int health;
    private int maxHealth;
    private boolean inDanger = false;
    private boolean dead = false;
    private Gender gender = Gender.None;
    private Kingdom kingdom = Kingdom.Unknown;
    private RuntimeHeroBase primaryHero;
    private RuntimeHeroBase viceHero;
    private int heroMask = 0;
    private List<GearCardBase> equips = new ArrayList<>();
    private List<CardBase> judges = new ArrayList<>();
    private List<CardBase> hands = new ArrayList<>();
    private List<TokenBase> tokens = new ArrayList<>();
    private List<SkillBase> skills = new ArrayList<>();
    private List<AbilityBase> abilities = new ArrayList<>();
    private boolean expelled = false;
    private boolean chained = false;
    private StageState stageState;

    public void clearResource(GameEngine engine) {
        //TODO clear tokens when dead
    }

    public Kingdom revealFinalKingdom(GameEngine e) {
        return kingdom;
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
        if (CollectionUtils.isEmpty(equips)) {
            return 1;
        }
        return equips.stream()
                .filter(card -> card.subType() == CardSubType.Weapon)
                .map(GearCardBase::attackRange)
                .max(Integer::compareTo)
                .orElse(1);
    }
    public int offensePoint() {
        return horseSum(GearCardBase::offensePoint, AbilityBase::offensePoint);
    }
    public int defensePoint() {
        return horseSum(GearCardBase::defensePoint, AbilityBase::defensePoint);
    }
    private int horseSum(Function<GearCardBase, Integer> cardFunc, Function<AbilityBase, Integer> abilityFunc) {
        int sum = 0;
        sum += equips.stream()
                .map(cardFunc)
                .reduce(Integer::sum)
                .orElse(0);
        sum += allAbilities().stream()
                .map(abilityFunc)
                .reduce(Integer::sum)
                .orElse(0);
        return sum;
    }

    public void onJinkSucceed(GameEngine engine, SlashCardUseSettlement settlement, Player offender, Player defender) {
        List<AbilityBase> abilities = allAbilities().stream()
                .filter(abilityBase -> abilityBase.canActive(engine, DuckEvent.JinkUseDuck, this))
                .collect(Collectors.toList());
        engine.batchQueryAbility(this, abilities,
                (ability) -> ability.onJinkSucceed(engine, settlement, offender, defender)
        );
    }


    private List<AbilityBase> allAbilities() {
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
        List<AbilityBase> abilities = allAbilities().stream()
                .filter(ab -> ab.canActive(engine, DamageEvent.DDamaged, this))
                .toList();
        engine.batchQueryAbility(this, abilities, (ability) -> ability.onDDamaged(engine, settlement));
    }

    public void acquireHand(GameEngine e, CardTransferContext ctc, List<CardBase> cards) {
        //TODO add alarm
        hands.addAll(cards);
    }

    public void updateHealth(GameEngine e, int amount) {
        if (stageState.getHealthLocked()) {
            return;
        }
        int delta = 0;
        if (this.health + amount >= this.maxHealth) {
            delta = maxHealth - health;
            this.health = maxHealth;
        } else {
            delta = amount;
            this.health += amount;
        }
        if (delta != 0) {
            e.onHealthChanged(this, delta);
        }
    }
    public void removeCard(GameEngine e, List<CardBase> cards, CardLostType type) {
        List<CardBase> handToRemove = new ArrayList<>();
        List<CardBase> equipToRemove = new ArrayList<>();
        Set<CardBase> cardSet = new HashSet<>();
        for (CardBase card : cards) {
            if (card instanceof DynamicCard dynamicCard) {
                if (dynamicCard.virtual()) {
                    continue;
                }
                cardSet.addAll(dynamicCard.originCards());
            } else {
                cardSet.add(card);
            }
        }
        for (CardBase card : hands) {
            if (cardSet.contains(card)) {
                handToRemove.add(card);
            }
        }
        for (CardBase card : equips) {
            if (cardSet.contains(card)) {
                equipToRemove.add(card);
            }
        }
        hands.removeAll(handToRemove);
        equips.removeAll(equipToRemove);
        e.addToCsBuffer(this, cards, type);
    }


    public void onCardLost(GameEngine e, Player player, Enum type) {
        List<AbilityBase> abList = allAbilities().stream()
                .filter(ab -> ab.canActive(e, type, player))
                .toList();
        e.batchQueryAbility(this, abList, (ab) -> ab.onCardLost(e, type, player));
    }
}
