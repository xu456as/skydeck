package io.skydeck.gserver.domain.card.gear;

import io.skydeck.gserver.annotation.CardExecMeta;
import io.skydeck.gserver.domain.card.CardBase;
import io.skydeck.gserver.domain.card.GearCardBase;
import io.skydeck.gserver.domain.dto.CardUseDTO;
import io.skydeck.gserver.domain.player.Player;
import io.skydeck.gserver.domain.settlement.CardSettlement;
import io.skydeck.gserver.domain.settlement.SettlementBase;
import io.skydeck.gserver.domain.skill.AbilityBase;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.enums.CardNameType;
import io.skydeck.gserver.enums.CardSubType;
import io.skydeck.gserver.enums.CardUseEvent;
import io.skydeck.gserver.enums.DamageEvent;
import io.skydeck.gserver.impl.settlement.DamageSettlement;
import io.skydeck.gserver.impl.settlement.SlashUseSettlement;

import java.util.Collections;
import java.util.List;

@CardExecMeta(cardNameType = CardNameType.Qinggangjian, settlement = "GearCardSettlement")
public class QinggangjianCard extends GearCardBase {
    @Override
    public int attackRange() {
        return 2;
    }
    @Override
    public List<AbilityBase> abilities() {
        return Collections.singletonList(ability);
    }

    private final QinggangjianAbility ability = new QinggangjianAbility();

    private class QinggangjianAbility extends AbilityBase {

        @Override
        public String name() {
            return QinggangjianCard.this.name();
        }
        @Override
        public boolean canActive(GameEngine engine, Enum event, Player player) {
            //TODO
            return false;
        }

        @Override
        public void onOCardTargeted(Player offender, Player defender, CardSettlement settlement) {
            //TODO
        }
    }
}
