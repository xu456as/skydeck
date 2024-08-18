package io.skydeck.gserver.domain.card.gear;

import io.skydeck.gserver.annotation.CardExecMeta;
import io.skydeck.gserver.domain.card.GearCardBase;
import io.skydeck.gserver.domain.dto.ActiveCheckDTO;
import io.skydeck.gserver.domain.settlement.SettlementBase;
import io.skydeck.gserver.domain.skill.AbilityBase;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.enums.CardLostType;
import io.skydeck.gserver.enums.CardNameType;
import io.skydeck.gserver.enums.DamageEvent;
import io.skydeck.gserver.impl.settlement.DamageSettlement;
import io.skydeck.gserver.impl.settlement.SlashUseSettlement;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@CardExecMeta(cardNameType = CardNameType.Huxinjing, settlement = "GearCardSettlement")
public class HuxinjingCard extends GearCardBase {
    @Override
    public List<AbilityBase> abilities() {
        return Collections.singletonList(ability);
    }

    private final HuxinjingAbility ability = new HuxinjingAbility();

    private class HuxinjingAbility extends AbilityBase {
        @Override
        public String name() {
            return HuxinjingCard.this.name();
        }
        @Override
        public Set<Enum> events() {
            return Collections.singleton(DamageEvent.DDamaging);
        }
        @Override
        public boolean canActive(GameEngine engine, Enum event, ActiveCheckDTO activeCheck) {
            SettlementBase set = engine.currentSettlement();
            if (!(set instanceof DamageSettlement dSet)) {
                return false;
            }
            if (!(events().contains(event) && activeCheck.getSubject() == owner)) {
                return false;
            }
            if (dSet.getParent() instanceof SlashUseSettlement slashSet) {
                if (slashSet.getIgnoreArmorSet().contains(owner)) {
                    return false;
                }
            }
            return dSet.getDamageCount() > 1 || dSet.getDamageCount() >= owner.getHealth();
        }

        @Override
        public void onDDamaging(GameEngine engine, DamageSettlement settlement) {
            settlement.setDamageCount(0);
            owner.removeCard(engine, Collections.singletonList(HuxinjingCard.this), CardLostType.Other);
            engine.onCardLost(owner, CardLostType.Other, Collections.singletonList(HuxinjingCard.this));
        }
    }
}
