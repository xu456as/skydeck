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
import io.skydeck.gserver.engine.CardFilterFactory;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.engine.QueryManager;
import io.skydeck.gserver.enums.*;
import io.skydeck.gserver.impl.settlement.CardDiscardSettlement;
import io.skydeck.gserver.impl.settlement.DamageSettlement;
import io.skydeck.gserver.impl.settlement.InDangerSettlement;
import io.skydeck.gserver.impl.settlement.SlashUseSettlement;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@CardExecMeta(cardNameType = CardNameType.Feilongduofeng, settlement = "GearCardSettlement")
public class FeilongduofengCard extends GearCardBase {
    @Override
    public int attackRange() {
        return 2;
    }

    @Override
    public List<AbilityBase> abilities() {
        return Collections.singletonList(ability);
    }

    private final FeilongduofengAbility ability = new FeilongduofengAbility();

    private class FeilongduofengAbility extends AbilityBase {
        @Override
        public String name() {
            return FeilongduofengCard.this.name();
        }
        @Override
        public Set<Enum> events() {
            return Collections.singleton(CardUseEvent.OTargeted);
        }
        @Override
        public boolean canActive(GameEngine engine, Enum event, Player player) {
            return activeOnTargeted(engine, event, player) || activeInDangering(engine, event, player);
        }
        private boolean activeOnTargeted(GameEngine engine, Enum event, Player player) {
            SettlementBase currentSettlement = engine.currentSettlement();
            if (!(currentSettlement instanceof SlashUseSettlement slashUseSettlement)) {
                return false;
            }
            if (slashUseSettlement.getUseDTO().getPlayer() == player) {
                return false;
            }
            return event == CardUseEvent.OTargeted && player.getEquips().contains(FeilongduofengCard.this);
        }
        private boolean activeInDangering(GameEngine engine, Enum event, Player player) {
            SettlementBase currentSettlement = engine.currentSettlement();
            if (!(currentSettlement instanceof InDangerSettlement settlement)) {
                return false;
            }
            if (player != settlement.getPlayer()) {
                return false;
            }
            DamageSettlement dSettlement = null;
            if ((dSettlement = settlement.getDamageSettlement()) == null) {
                return false;
            }
            if (dSettlement.getCard() == null || dSettlement.getCard().subType() != CardSubType.Slash) {
                return false;
            }
            Player dealer = null;
            if ((dealer = dSettlement.getDealer()) == null) {
                return false;
            }
            return event == InDangerEvent.InDangering && dealer.getEquips().contains(FeilongduofengCard.this);
        }

        @Override
        public void onOCardTargeted(GameEngine e, Player offender, Player defender, CardSettlement settlement) {
            if (!(settlement instanceof SlashUseSettlement)) {
                return;
            }
            QueryManager qm = e.getQueryManager();
            CardDiscardDTO discardDTO = qm.cardDiscardQuery(defender, 1, QueryManager.AREA_OWN);
            if (discardDTO != null) {
                e.runSettlement(CardDiscardSettlement.newOne(discardDTO));
            }
        }

        @Override
        public void onInDangering(GameEngine e, InDangerSettlement settlement) {
            Player suffer = settlement.getPlayer();
            QueryManager qm = e.getQueryManager();
            CardBase card = qm.pickOneCard(owner, suffer, QueryManager.AREA_OWN);
            if (card == null) {
                return;
            }
            List<CardBase> cardMove = Collections.singletonList(card);
            cardMove = e.onCardLosing(suffer, CardLostType.Stolen, cardMove);
            suffer.removeCard(e, cardMove, CardLostType.Stolen);
            e.onCardLost(suffer, CardLostType.Stolen, cardMove);
            owner.acquireHand(e,
                    CardTransferContext.builder().acquireWay(CardAcquireWay.Steal).build(),
                    cardMove);
        }

    }
}
