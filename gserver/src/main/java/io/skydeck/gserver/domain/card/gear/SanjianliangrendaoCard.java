package io.skydeck.gserver.domain.card.gear;

import io.skydeck.gserver.annotation.CardExecMeta;
import io.skydeck.gserver.domain.card.GearCardBase;
import io.skydeck.gserver.domain.player.Player;
import io.skydeck.gserver.domain.settlement.SettlementBase;
import io.skydeck.gserver.domain.skill.AbilityBase;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.enums.CardNameType;
import io.skydeck.gserver.enums.CardSubType;
import io.skydeck.gserver.enums.DamageEvent;
import io.skydeck.gserver.impl.settlement.DamageSettlement;

import java.util.Collections;
import java.util.List;
import java.util.Set;


@CardExecMeta(cardNameType = CardNameType.Sanjianliangrendao, settlement = "GearCardSettlement")
public class SanjianliangrendaoCard extends GearCardBase {
    @Override
    public int attackRange() {
        return 3;
    }
    @Override
    public List<AbilityBase> abilities() {
        return Collections.singletonList(ability);
    }

    private final SanjianliangrendaoAbility ability = new SanjianliangrendaoAbility();

    private class SanjianliangrendaoAbility extends AbilityBase {
        @Override
        public String name() {
            return SanjianliangrendaoCard.this.name();
        }
        @Override
        public Set<Enum> events() {
            return Collections.singleton(DamageEvent.ODamaged);
        }

        @Override
        public boolean canActive(GameEngine engine, Enum event, Player player) {
            if (events().contains(event)) {
                return false;
            }
            if (player != owner) {
                return false;
            }
            SettlementBase se = engine.currentSettlement();
            if (!(se instanceof DamageSettlement dSe)) {
                return false;
            }
            if (dSe.getCard() == null || dSe.getCard().subType() != CardSubType.Slash) {
                return false;
            }
            return !dSe.getSufferer().isDead();
        }

        @Override
        public void onODamaged(GameEngine engine, DamageSettlement settlement) {
            //todo
        }
    }
}
