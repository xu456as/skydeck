package io.skydeck.gserver.domain.card.gear;

import io.skydeck.gserver.annotation.CardExecMeta;
import io.skydeck.gserver.domain.card.CardBase;
import io.skydeck.gserver.domain.card.CardFilterIface;
import io.skydeck.gserver.domain.card.GearCardBase;
import io.skydeck.gserver.domain.dto.CardDiscardDTO;
import io.skydeck.gserver.domain.player.Player;
import io.skydeck.gserver.domain.skill.AbilityBase;
import io.skydeck.gserver.engine.CardFilterFactory;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.engine.QueryManager;
import io.skydeck.gserver.enums.*;
import io.skydeck.gserver.impl.settlement.SlashUseSettlement;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@CardExecMeta(cardNameType = CardNameType.Guanshifu, settlement = "GearCardSettlement")
public class GuanShiFuCard extends GearCardBase {
    @Override
    public int attackRange() {
        return 3;
    }

    @Override
    public List<AbilityBase> abilities() {
        return Collections.singletonList(ability);
    }

    private final GuanShiFuAbility ability = new GuanShiFuAbility();

    private class GuanShiFuAbility extends AbilityBase {
        @Override
        public Set<Enum> events() {
            return Collections.singleton(DuckEvent.JinkUseDuck);
        }

        @Override
        public boolean canActive(GameEngine engine, Enum event, Player player) {
            return event == DuckEvent.JinkUseDuck && player.getEquips().contains(GuanShiFuCard.this);
        }

        @Override
        public String name() {
            return GuanShiFuCard.this.name();
        }
        @Override
        public void onJinkSucceed(GameEngine engine, SlashUseSettlement settlement, Player offender, Player defender) {
            if (!offender.getEquips().contains(GuanShiFuCard.this)) {
                return;
            }
            QueryManager queryManager = engine.getQueryManager();
            CardFilterFactory  cff = engine.getCardFilterFactory();
            CardDiscardDTO discardDTO = queryManager.cardDiscardQuery(
                    offender, 2, null, Collections.singletonList(this::filter));
            if (discardDTO == null) {
                return;
            }
            settlement.setJinkInvalid(true);
        }
        public boolean filter(CardBase cardBase) {
            return cardBase.id() != GuanShiFuCard.this.id;
        }
    }
}
