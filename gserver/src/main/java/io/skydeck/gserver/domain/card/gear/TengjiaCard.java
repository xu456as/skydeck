package io.skydeck.gserver.domain.card.gear;

import io.skydeck.gserver.annotation.CardExecMeta;
import io.skydeck.gserver.domain.card.GearCardBase;
import io.skydeck.gserver.domain.dto.ActiveCheckDTO;
import io.skydeck.gserver.domain.player.Player;
import io.skydeck.gserver.domain.settlement.CardSettlement;
import io.skydeck.gserver.domain.settlement.SettlementBase;
import io.skydeck.gserver.domain.skill.AbilityBase;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.enums.CardNameType;
import io.skydeck.gserver.enums.CardUseEvent;
import io.skydeck.gserver.enums.DamageEvent;
import io.skydeck.gserver.enums.DamageNature;
import io.skydeck.gserver.impl.settlement.DamageSettlement;
import io.skydeck.gserver.impl.settlement.MassiveJinkUseSettlement;
import io.skydeck.gserver.impl.settlement.MassiveSlashUseSettlement;
import io.skydeck.gserver.impl.settlement.SlashUseSettlement;
import org.apache.commons.collections4.SetUtils;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@CardExecMeta(cardNameType = CardNameType.Tengjia, settlement = "GearCardSettlement")
public class TengjiaCard extends GearCardBase {
    @Override
    public List<AbilityBase> abilities() {
        return Collections.singletonList(ability);
    }

    private final TengjiaAbility ability = new TengjiaAbility();

    private class TengjiaAbility extends AbilityBase {
        @Override
        public String name() {
            return TengjiaCard.this.name();
        }
        @Override
        public Set<Enum> events() {
            return SetUtils.hashSet(DamageEvent.DDamaging, CardUseEvent.Effecting);
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
            if (set instanceof CardSettlement cSet) {
                if (cSet instanceof SlashUseSettlement sSet) {
                    if(sSet.getIgnoreArmorSet().contains(owner)) {
                        return false;
                    }
                    if (sSet.getDamageNature() == DamageNature.Normal) {
                        return true;
                    }
                }
                return cSet instanceof MassiveSlashUseSettlement || cSet instanceof MassiveJinkUseSettlement;
            } else if (set instanceof DamageSettlement dSet) {
                if (dSet.getParent() instanceof SlashUseSettlement sSet) {
                    if(sSet.getIgnoreArmorSet().contains(owner)) {
                        return false;
                    }
                }
                return dSet.getNature() == DamageNature.Fire;
            }
            return false;
        }

        @Override
        public boolean onCardEffecting(CardSettlement settlement, Player target) {
            return target != owner;
        }

        @Override
        public void onDDamaging(GameEngine engine, DamageSettlement settlement) {
            settlement.setDamageCount(settlement.getDamageCount() + 1);
        }
    }
}
