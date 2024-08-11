package io.skydeck.gserver.domain.card.gear;

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

public class GuanShiFuCard extends GearCardBase {
    private int id;
    private int number;
    private Color color;
    private Suit suit;

    private GuanShiFuAbility ability = new GuanShiFuAbility();
    private CardFilterIface denyFilter = new CardFilterIface() {
        @Override
        public boolean filter(CardBase cardBase) {
            return cardBase.id() != GuanShiFuCard.this.id;
        }
    };

    public GuanShiFuCard(int id, int number, Color color, Suit suit) {
        this.id = id;
        this.number = number;
        this.color = color;
        this.suit = suit;
    }


    @Override
    public int attackRange() {
        return 3;
    }

    @Override
    public Integer id() {
        return id;
    }

    @Override
    public String name() {
        return CardNameType.Guanshifu.name();
    }

    @Override
    public CardNameType nameType() {
        return CardNameType.Guanshifu;
    }

    @Override
    public Integer number() {
        return number;
    }

    @Override
    public Color color() {
        return color;
    }

    @Override
    public Suit suit() {
        return suit;
    }

    @Override
    public CardType type() {
        return CardType.Gear;
    }

    @Override
    public CardSubType subType() {
        return CardSubType.Weapon;
    }

    @Override
    public List<AbilityBase> abilities() {
        return Collections.singletonList(ability);
    }

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
            return "GuanShiFu";
        }
        @Override
        public void onJinkSucceed(GameEngine engine, SlashUseSettlement settlement, Player offender, Player defender) {
            if (!offender.getEquips().contains(GuanShiFuCard.this)) {
                return;
            }
            QueryManager queryManager = engine.getQueryManager();
            CardFilterFactory  cff = engine.getCardFilterFactory();
            CardDiscardDTO discardDTO = queryManager.cardDiscardQuery(
                    offender, 2, null, Collections.singletonList(denyFilter));
            if (discardDTO == null) {
                return;
            }
            settlement.setJinkInvalid(true);
        }
    }
}
