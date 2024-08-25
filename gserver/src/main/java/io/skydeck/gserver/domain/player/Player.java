package io.skydeck.gserver.domain.player;

import io.skydeck.gserver.domain.card.CardBase;
import io.skydeck.gserver.domain.card.DynamicCard;
import io.skydeck.gserver.domain.card.GearCardBase;
import io.skydeck.gserver.domain.dto.ActiveCheckDTO;
import io.skydeck.gserver.domain.dto.CardTransferContext;
import io.skydeck.gserver.domain.hero.RuntimeHeroBase;
import io.skydeck.gserver.domain.hero.StaticHeroBase;
import io.skydeck.gserver.domain.skill.AbilityBase;
import io.skydeck.gserver.domain.skill.DynamicAbilityBase;
import io.skydeck.gserver.domain.skill.SkillBase;
import io.skydeck.gserver.domain.token.TokenBase;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.enums.*;
import io.skydeck.gserver.exception.BizException;
import io.skydeck.gserver.impl.settlement.DamageSettlement;
import io.skydeck.gserver.impl.settlement.InDangerSettlement;
import io.skydeck.gserver.impl.settlement.SlashUseSettlement;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
public class Player implements Comparable<Player> {

    public static final int HERO_MASK_PRIMARY = 1;
    public static final int HERO_MAST_VICE = 2;

    private int id;
    private String name;
    private int health;
    private int maxHealth;
    private RuntimeHeroBase primaryHero;
    private RuntimeHeroBase viceHero;
    private PlayerStageState stageState;
    private Gender gender = Gender.None;
    private boolean inDanger = false;
    private boolean dead = false;
    private Kingdom kingdom = Kingdom.Unknown;
    private int heroMask = 0;
    private List<GearCardBase> equips = new ArrayList<>();
    private List<CardBase> judges = new ArrayList<>();
    private List<CardBase> hands = new ArrayList<>();
    private List<TokenBase> tokens = new ArrayList<>();
    private List<SkillBase> skills = new ArrayList<>();
    private List<AbilityBase> abilities = new ArrayList<>();
    private List<DynamicAbilityBase> dynamicAbilities = new ArrayList<>();
    private boolean expelled = false;
    private boolean chained = false;

    public void init(GameEngine e, int id, String name, StaticHeroBase primaryHero, StaticHeroBase viceHero) {
        this.id = id;
        this.name = name;
        this.health = (int)Math.ceil(
                        Double.parseDouble(primaryHero.initHealth()) + Double.parseDouble(viceHero.initHealth())
        );
        this.maxHealth = (int)Math.ceil(
                Double.parseDouble(primaryHero.maxHealth()) + Double.parseDouble(viceHero.maxHealth())
        );
        this.primaryHero = RuntimeHeroBase.newOne(e, this, primaryHero);
        this.viceHero = RuntimeHeroBase.newOne(e, this, viceHero);
        this.stageState = new PlayerStageState();
    }

    public void clearResource(GameEngine engine) {
        //TODO clear tokens when dead
    }

    public Kingdom revealFinalKingdom(GameEngine e) {
        return kingdom;
    }

    @Override
    public String toString() {
        return "%d:%s".formatted(id, name);
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

    public boolean canSelectAsCardTarget(GameEngine e, Player target, CardBase card) {
        return getSkills().stream().anyMatch(skill -> skill.canSelectAsCardTarget(e, this, target, card));
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
    public int slashQuota(GameEngine e) {
        List<AbilityBase> abs = allAbilities();
        int val = stageState.getSlashQuota();
        for (AbilityBase ab : abs) {
            val = ab.slashQuotaMod(e, val);
        }
        return val;
    }
    public int drawQuota(GameEngine e) {
        List<AbilityBase> abs = allAbilities();
        int val = stageState.getDrawQuota();
        for (AbilityBase ab : abs) {
            val = ab.drawQuotaMod(e, val);
        }
        return val;
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

    public void onJinkSucceed(GameEngine engine, SlashUseSettlement settlement, Player offender, Player defender) {
        List<AbilityBase> abilities = allAbilities().stream()
                .filter(abilityBase -> abilityBase.canActive(engine, DuckEvent.JinkUseDuck, ActiveCheckDTO.subject(defender)))
                .collect(Collectors.toList());
        engine.batchQueryAbility(this, abilities,
                (ability) -> ability.onJinkSucceed(engine, settlement, offender, defender)
        );
    }

    public void removeDynamicAbility(GameEngine e, Enum event, Player player) {
        dynamicAbilities.removeIf(da -> da.lostCheck(e, event, player));
    }


    public List<AbilityBase> allAbilities() {
        List<AbilityBase> abilityList = new ArrayList<>();
        abilityList.addAll(skills);
        abilityList.addAll(abilities);
        abilityList.addAll(dynamicAbilities);
        abilityList.addAll(primaryHero.skills());
        abilityList.addAll(viceHero.skills());
        if (!CollectionUtils.isEmpty(equips)) {
            abilityList.addAll(equips.stream().flatMap(e -> e.abilities().stream()).toList());
        }

        return abilityList;
    }

    public void onInDangering(GameEngine e, InDangerSettlement settlement) {
        List<AbilityBase> abilities = allAbilities().stream()
                .filter(ab -> ab.canActive(e, InDangerEvent.InDangering, ActiveCheckDTO.subject(settlement.getPlayer())))
                .toList();
        e.batchQueryAbility(this, abilities, (ability) -> ability.onInDangering(e, settlement));
    }

    public void onDDamaged(GameEngine engine, DamageSettlement settlement) {
        List<AbilityBase> abilities = allAbilities().stream()
                .filter(ab -> ab.canActive(engine, DamageEvent.DDamaged, ActiveCheckDTO.subject(settlement.getSufferer())))
                .toList();
        engine.batchQueryAbility(this, abilities, (ability) -> ability.onDDamaged(engine, settlement));
    }

    public Suit cardSuitMod(GameEngine e, CardBase card) {
        for (AbilityBase ab : allAbilities()) {
            Suit suit = ab.cardSuitMod(e, card);
            if (suit != card.suit()) {
                return suit;
            }
        }
        return card.suit();
    }

    public void onLeavingDiscardPhase(GameEngine e, Player currentPlayer) {
        List<AbilityBase> abilities = allAbilities().stream()
                .filter(ab -> ab.canActive(e, PhaseEvent.LeavingDiscardPhase, ActiveCheckDTO.subject(currentPlayer)))
                .toList();
        e.batchQueryAbility(this, abilities, (ability) -> ability.onLeavingDiscardPhase(e, this));
    }

    public void acquireHand(GameEngine e, CardTransferContext ctc, List<CardBase> cards) {
        //TODO add alarm
        hands.addAll(cards);
    }
    public void acquireEquip(GameEngine e, CardTransferContext ctc, List<GearCardBase> cards) {
        //TODO add alarm
        cards.forEach(c -> c.onEnteringEquipArea(e, this));
        cards.forEach(c -> c.updateOwner(this));
        equips.addAll(cards);
        cards.forEach(c -> c.onEnteredEquipArea(e, this));
    }
    public void updateHeroActive(int index, boolean active) {
        switch (index) {
            case 0 -> primaryHero.setActive(active);
            case 1 -> viceHero.setActive(active);
        }
    }
    public void updateHealth(GameEngine e, int amount) {
        if (stageState.getHealthLocked() || stageState.getInLimbo()) {
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
    public void updateChain(GameEngine e, boolean chained) {
        for (AbilityBase ab : allAbilities()) {
            if (!ab.onUpdatingChained(e, this)) {
                return;
            }
        }
        this.chained = chained;
        for (AbilityBase ab : allAbilities()) {
           ab.onUpdatedChained(e, this);
        }
    }

    public void removeCard(GameEngine e, List<CardBase> cards, CardLostType type) {
        if (CollectionUtils.isEmpty(cards)) {
            return;
        }
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
        for (GearCardBase card : equips) {
            if (cardSet.contains(card)) {
                equipToRemove.add(card);
                card.onLeavingEquipArea(e, this);
                card.updateOwner(null);
                card.onLeavedEquipArea(e, this);
            }
        }
        hands.removeAll(handToRemove);
        equips.removeAll(equipToRemove);
        e.addToCsBuffer(this, cards, type);
    }


    public void onCardLost(GameEngine e, Player player, Enum type) {
        List<AbilityBase> abList = allAbilities().stream()
                .filter(ab -> ab.canActive(e, type, ActiveCheckDTO.subject(player)))
                .toList();
        e.batchQueryAbility(this, abList, (ab) -> ab.onCardLost(e, type, player));
    }

    public int incStageCount(String key) {
        return incStageCount(key, 1);
    }

    public void addDynamicAbility(AbilityBase ability) {
        //TODO
    }

    public int incStageCount(String key, int count) {
        try {
            return stageState.incCount(key, count);
        } catch (Exception e) {
            throw new BizException("can't increment stage count for key[%s]".formatted(key), e);
        }
    }

    public boolean ignoreDistance(Player defender, CardBase card) {
        return allAbilities().stream().anyMatch(ab -> ab.ignoreDistance(defender, card));
    }

    public int kingdomVolMod(GameEngine e, int original) {
        for (AbilityBase ab : allAbilities()) {
            original = ab.kingdomVolMod(e, original);
        }
        return original;
    }
}
