package io.skydeck.gserver.impl;

import io.skydeck.gserver.domain.CardBase;
import io.skydeck.gserver.domain.Player;
import io.skydeck.gserver.domain.SettlementBase;
import io.skydeck.gserver.engine.SettlementEngine;
import io.skydeck.gserver.enums.DamageNature;

public class DamageSettlement implements SettlementBase {
    private Player dealer;
    private Player sufferer;
    private CardBase card;
    private int damageCount;
    private DamageNature nature;

    public static DamageSettlement newOne(Player dealer, Player sufferer, int amount, DamageNature nature, CardBase card) {
        DamageSettlement settlement = new DamageSettlement();
        settlement.dealer = dealer;
        settlement.sufferer = sufferer;
        settlement.damageCount = amount;
        settlement.nature = nature;
        settlement.card = card;
        return settlement;
    }

    @Override
    public void resolve(SettlementEngine engine) {
        boolean valid = engine.onDealingDamage(this);
        if (!valid) {
            return;
        }
        int health = sufferer.getHealth();
        sufferer.setHealth(health - damageCount);
        if (sufferer.getHealth() <= 0) {
            DyingSettlement dyingSettlement = DyingSettlement.newOne(sufferer, dealer);
            dyingSettlement.resolve(engine);
        }
        engine.onDealtDamage(this);
    }
}
