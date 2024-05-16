package io.skydeck.gserver.impl;

import io.skydeck.gserver.domain.CardBase;
import io.skydeck.gserver.domain.Player;
import io.skydeck.gserver.domain.SettlementBase;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.enums.DamageNature;
import lombok.Data;

@Data
public class DamageSettlement extends SettlementBase {
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
    public void resolve(GameEngine engine) {
        boolean valid = engine.onDealingDamage(this);
        if (!valid || damageCount == 0) {
            return;
        }
        int health = sufferer.getHealth();
        sufferer.updateHealth(engine, -damageCount);
        if (sufferer.getHealth() <= 0) {
            InDangerSettlement inDangerSettlement = InDangerSettlement.newOne(sufferer, dealer);
            inDangerSettlement.resolve(engine);
        }
        engine.onDealtDamage(this);
    }
}
