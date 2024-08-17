package io.skydeck.gserver.domain.card.gear;

import io.skydeck.gserver.annotation.CardExecMeta;
import io.skydeck.gserver.domain.card.CardBase;
import io.skydeck.gserver.domain.card.GearCardBase;
import io.skydeck.gserver.domain.dto.CardDiscardDTO;
import io.skydeck.gserver.domain.dto.CardTransferContext;
import io.skydeck.gserver.domain.player.Player;
import io.skydeck.gserver.domain.settlement.CardSettlement;
import io.skydeck.gserver.domain.settlement.SettlementBase;
import io.skydeck.gserver.domain.skill.AbilityBase;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.engine.QueryManager;
import io.skydeck.gserver.enums.*;
import io.skydeck.gserver.i18n.TextDictionary;
import io.skydeck.gserver.impl.settlement.CardDiscardSettlement;
import io.skydeck.gserver.impl.settlement.DamageSettlement;
import io.skydeck.gserver.impl.settlement.InDangerSettlement;
import io.skydeck.gserver.impl.settlement.SlashUseSettlement;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@CardExecMeta(cardNameType = CardNameType.Zhuqueyushan, settlement = "GearCardSettlement")
public class ZhuqueyushanCard extends GearCardBase {
    @Override
    public int attackRange() {
        return 4;
    }

    @Override
    public List<AbilityBase> abilities() {
        return Collections.singletonList(ability);
    }

    private final ZhuqueyushanAbility ability = new ZhuqueyushanAbility();

    private class ZhuqueyushanAbility extends AbilityBase {
        @Override
        public String name() {
            return ZhuqueyushanCard.this.name();
        }
        @Override
        public Set<Enum> events() {
            return Collections.singleton(CardUseEvent.OTargeted);
        }
        @Override
        public boolean canActive(GameEngine engine, Enum event, Player player) {
            return activeOnUsing(engine, event, player);
        }
        private boolean activeOnUsing(GameEngine engine, Enum event, Player player) {
            SettlementBase currentSettlement = engine.currentSettlement();
            if (!(currentSettlement instanceof SlashUseSettlement slashUseSettlement)) {
                return false;
            }
            if (slashUseSettlement.getUseDTO().getPlayer() == player) {
                return false;
            }
            return event == CardUseEvent.Using && player.getEquips().contains(ZhuqueyushanCard.this);
        }

        @Override
        public void onCardUsing(GameEngine e, Player offender, CardSettlement settlement) {

            if (!(settlement instanceof SlashUseSettlement slashSettlement)) {
                return;
            }
            CardBase card = slashSettlement.getUseDTO().getCard();
            if (card.nature() != DamageNature.Normal) {
                return;
            }
            QueryManager qm = e.getQueryManager();
            int index = qm.optionQuery(offender,
                    TextDictionary.ZhuqueyushanOpt0.i18n(),
                    TextDictionary.ZhuqueyushanOpt1.i18n(),
                    TextDictionary.ZhuqueyushanOpt2.i18n());
            if (index == -1) {
                return;
            }
            switch (index) {
                case 0:
                    slashSettlement.updateNature(DamageNature.Fire);
                    break;
                case 1:
                    slashSettlement.updateNature(DamageNature.Lightning);
                    break;
                case 2:
                    slashSettlement.updateNature(DamageNature.Ice);
                    break;
            }
        }
    }
}
