package io.skydeck.gserver.domain.card.gear;

import io.skydeck.gserver.annotation.CardExecMeta;
import io.skydeck.gserver.domain.card.GearCardBase;
import io.skydeck.gserver.domain.dto.ActiveCheckDTO;
import io.skydeck.gserver.domain.player.Player;
import io.skydeck.gserver.domain.settlement.CardSettlement;
import io.skydeck.gserver.domain.settlement.SettlementBase;
import io.skydeck.gserver.domain.skill.AbilityBase;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.enums.*;
import io.skydeck.gserver.impl.settlement.SlashUseSettlement;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@CardExecMeta(cardNameType = CardNameType.Renwangdun, settlement = "GearCardSettlement")
public class RenwangdunCard extends GearCardBase {
    @Override
    public List<AbilityBase> abilities() {
        return Collections.singletonList(ability);
    }

    private final RenwangdunAbility ability = new RenwangdunAbility();

    private class RenwangdunAbility extends AbilityBase {
        @Override
        public String name() {
            return RenwangdunAbility.this.name();
        }
        @Override
        public Set<Enum> events() {
            return Collections.singleton(CardUseEvent.Effecting);
        }

        @Override
        public boolean mandatory() {
            return true;
        }

        @Override
        public boolean canActive(GameEngine engine, Enum event, ActiveCheckDTO activeCheck) {
            if (!events().contains(event)) {
                return false;
            }
            if (activeCheck.getSubject() != owner) {
                return false;
            }
            SettlementBase curSet = engine.currentSettlement();
            if (!(curSet instanceof SlashUseSettlement sSet)) {
                return false;
            }
            if (sSet.getIgnoreArmorSet().contains(owner)) {
                return false;
            }
            return true;
        }

        @Override
        public boolean onCardEffecting(CardSettlement settlement, Player target) {
            return false;
        }
    }
}
