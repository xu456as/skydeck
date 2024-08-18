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
import io.skydeck.gserver.impl.settlement.DamageSettlement;
import io.skydeck.gserver.impl.settlement.MassiveJinkUseSettlement;
import io.skydeck.gserver.impl.settlement.MassiveSlashUseSettlement;
import io.skydeck.gserver.impl.settlement.SlashUseSettlement;
import org.apache.commons.collections4.SetUtils;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@CardExecMeta(cardNameType = CardNameType.Baiyinshizi, settlement = "GearCardSettlement")
public class BaiyinshiziCard extends GearCardBase {
    @Override
    public List<AbilityBase> abilities() {
        return Collections.singletonList(ability);
    }

    private final BaiyinshiziAbility ability = new BaiyinshiziAbility();

    @Override
    public void onLeavedEquipArea(GameEngine engine, Player player) {
        player.updateHealth(engine, 1);
    }

    private class BaiyinshiziAbility extends AbilityBase {
        @Override
        public String name() {
            return BaiyinshiziCard.this.name();
        }
        @Override
        public Set<Enum> events() {
            return SetUtils.hashSet(DamageEvent.DDamaging);
        }

        @Override
        public boolean mandatory() {
            return true;
        }

        @Override
        public boolean canActive(GameEngine engine, Enum event, ActiveCheckDTO activeCheck) {
            if (!(events().contains(event) && activeCheck.getSubject() == owner)) {
                return false;
            }
            SettlementBase set = engine.currentSettlement();
            if (set instanceof DamageSettlement dSet) {
                if (dSet.getParent() instanceof SlashUseSettlement sSet) {
                    if (sSet.getIgnoreArmorSet().contains(owner)) {
                        return false;
                    }
                }
                return dSet.getDamageCount() > 1;
            }
            return false;
        }


        @Override
        public void onDDamaging(GameEngine engine, DamageSettlement settlement) {
            settlement.setDamageCount(1);
        }
    }
}
