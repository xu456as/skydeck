package io.skydeck.gserver.domain.card.gear;

import io.skydeck.gserver.annotation.CardExecMeta;
import io.skydeck.gserver.domain.card.CardBase;
import io.skydeck.gserver.domain.card.GearCardBase;
import io.skydeck.gserver.domain.dto.ActiveCheckDTO;
import io.skydeck.gserver.domain.dto.CardDiscardDTO;
import io.skydeck.gserver.domain.settlement.SettlementBase;
import io.skydeck.gserver.domain.skill.AbilityBase;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.engine.QueryManager;
import io.skydeck.gserver.enums.*;
import io.skydeck.gserver.i18n.TextDictionary;
import io.skydeck.gserver.impl.settlement.CardDiscardSettlement;
import io.skydeck.gserver.impl.settlement.DamageSettlement;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@CardExecMeta(cardNameType = CardNameType.Hanbingjian, settlement = "GearCardSettlement")
public class HanbingjianCard extends GearCardBase {
    @Override
    public int attackRange() {
        return 2;
    }
    @Override
    public List<AbilityBase> abilities() {
        return Collections.singletonList(ability);
    }

    private final HanbingjianAbility ability = new HanbingjianAbility();

    private class HanbingjianAbility extends AbilityBase {


        @Override
        public String name() {
            return HanbingjianCard.this.name();
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
            return event == DamageEvent.ODamaging && activeCheck.getSubject().getEquips().contains(HanbingjianCard.this);
        }

        @Override
        public void onODamaging(GameEngine e, DamageSettlement dSettlement) {
            CardBase card = dSettlement.getCard();
            if (card == null || card.subType() != CardSubType.Slash) {
                return;
            }
            QueryManager qm = e.getQueryManager();
            int idx = qm.optionQuery(dSettlement.getDealer(), TextDictionary.Dismantle.i18n());
            if (idx == -1) {
                return;
            }
            dSettlement.setDamageCount(0);
            for (int i = 0; i < 2; i++) {
                CardBase cardDiscard = qm.pickOneCard(
                        dSettlement.getDealer(), dSettlement.getSufferer(),
                        QueryManager.AREA_EQUIP | QueryManager.AREA_HAND);
                if (cardDiscard == null) {
                    break;
                }
                CardDiscardSettlement discardSettlement = CardDiscardSettlement.newOne(
                        CardDiscardDTO.builder()
                                .offender(dSettlement.getDealer())
                                .defender(dSettlement.getSufferer())
                                .card(Collections.singletonList(cardDiscard))
                        .build()
                );
                e.runSettlement(discardSettlement);
            }
        }
    }
}
