package io.skydeck.gserver.domain.card.gear;

import io.skydeck.gserver.annotation.CardExecMeta;
import io.skydeck.gserver.domain.card.CardBase;
import io.skydeck.gserver.domain.card.GearCardBase;
import io.skydeck.gserver.domain.dto.ActiveCheckDTO;
import io.skydeck.gserver.domain.dto.CardDiscardDTO;
import io.skydeck.gserver.domain.settlement.SettlementBase;
import io.skydeck.gserver.domain.skill.AbilityBase;
import io.skydeck.gserver.engine.CardFilterFactory;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.engine.QueryManager;
import io.skydeck.gserver.enums.CardNameType;
import io.skydeck.gserver.enums.CardSubType;
import io.skydeck.gserver.enums.DamageEvent;
import io.skydeck.gserver.impl.settlement.CardDiscardSettlement;
import io.skydeck.gserver.impl.settlement.DamageSettlement;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@CardExecMeta(cardNameType = CardNameType.Qilingong, settlement = "GearCardSettlement")
public class QilingongCard extends GearCardBase {
    @Override
    public int attackRange() {
        return 5;
    }
    @Override
    public List<AbilityBase> abilities() {
        return Collections.singletonList(ability);
    }

    private final QilingongAbility ability = new QilingongAbility();

    private class QilingongAbility extends AbilityBase {


        @Override
        public String name() {
            return QilingongCard.this.name();
        }
        @Override
        public Set<Enum> events() {
            return Collections.singleton(DamageEvent.ODamaging);
        }
        @Override
        public boolean canActive(GameEngine engine, Enum event, ActiveCheckDTO activeCheck) {
            SettlementBase currentSettlement = engine.currentSettlement();
            if (!(currentSettlement instanceof DamageSettlement dSettlement)) {
                return false;
            }
            CardBase card = dSettlement.getCard();
            if (card == null || card.subType() != CardSubType.Slash || activeCheck.getSubject() != dSettlement.getDealer()) {
                return false;
            }
            return event == DamageEvent.ODamaging && activeCheck.getSubject().getEquips().contains(QilingongCard.this);
        }

        @Override
        public void onODamaging(GameEngine e, DamageSettlement dSettlement) {
            CardBase card = dSettlement.getCard();
            if (card == null || card.subType() != CardSubType.Slash) {
                return;
            }
            if (dSettlement.getSufferer().getEquips().stream()
                    .noneMatch(c -> c.subType() == CardSubType.OffenseRide
                            || c.subType() == CardSubType.DefenseRide
                            || c.subType() == CardSubType.SpecialRide)
            ) {
                return;
            }
            QueryManager qm = e.getQueryManager();
            CardFilterFactory cff = e.getCardFilterFactory();
            CardBase pickedCard = qm.pickOneCard(dSettlement.getDealer(), dSettlement.getSufferer(), QueryManager.AREA_EQUIP, cff.rideFilter());
            if (pickedCard == null) {
                return;
            }
            CardDiscardSettlement discardSettlement = CardDiscardSettlement.newOne(
                    CardDiscardDTO.builder()
                            .offender(dSettlement.getDealer())
                            .defender(dSettlement.getSufferer())
                            .card(Collections.singletonList(pickedCard))
                            .build()
            );
            e.runSettlement(discardSettlement);
        }
    }
}
