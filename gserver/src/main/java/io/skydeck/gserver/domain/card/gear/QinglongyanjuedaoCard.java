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
import io.skydeck.gserver.impl.settlement.SlashUseSettlement;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@CardExecMeta(cardNameType = CardNameType.Qinglongyanyuedao, settlement = "GearCardSettlement")
public class QinglongyanjuedaoCard extends GearCardBase {
    @Override
    public int attackRange() {
        return 3;
    }
    @Override
    public List<AbilityBase> abilities() {
        return Collections.singletonList(ability);
    }

    private final QinglongyanjuedaoAbility ability = new QinglongyanjuedaoAbility();

    private class QinglongyanjuedaoAbility extends AbilityBase {
        @Override
        public String name() {
            return QinglongyanjuedaoCard.this.name();
        }
        @Override
        public Set<Enum> events() {
            return Collections.singleton(CardUseEvent.OTargeted);
        }

        @Override
        public boolean mandatory() {
            return true;
        }

        @Override
        public boolean canActive(GameEngine engine, Enum event, ActiveCheckDTO activeCheck) {
            return activeOnTargeted(engine, event, activeCheck.getSubject());
        }
        private boolean activeOnTargeted(GameEngine engine, Enum event, Player player) {
            SettlementBase currentSettlement = engine.currentSettlement();
            if (!(currentSettlement instanceof SlashUseSettlement slashUseSettlement)) {
                return false;
            }
            if (slashUseSettlement.getUseDTO().getPlayer() == player) {
                return false;
            }
            return event == CardUseEvent.OTargeted && player.getEquips().contains(QinglongyanjuedaoCard.this);
        }

        @Override
        public void onOCardTargeted(GameEngine e, Player offender, Player defender, CardSettlement settlement) {
            if (!(settlement instanceof SlashUseSettlement sSettlement)) {
                return;
            }
            sSettlement.getDisableHeroRevealingSet().add(defender);
        }

    }
}
