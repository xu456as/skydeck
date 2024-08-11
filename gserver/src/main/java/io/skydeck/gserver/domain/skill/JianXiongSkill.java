package io.skydeck.gserver.domain.skill;

import io.skydeck.gserver.annotation.AbilityName;
import io.skydeck.gserver.domain.card.CardBase;
import io.skydeck.gserver.domain.player.Player;
import io.skydeck.gserver.domain.dto.CardTransferContext;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.engine.QueryManager;
import io.skydeck.gserver.enums.CardAcquireWay;
import io.skydeck.gserver.enums.DamageEvent;
import io.skydeck.gserver.i18n.TextDictionary;
import io.skydeck.gserver.impl.settlement.DamageSettlement;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@AbilityName("JianXiong")
public class JianXiongSkill extends SkillBase {
    private Player owner;

    public JianXiongSkill(Player owner) {
        this.owner = owner;
    }

    @Override
    public Set<Enum> events() {
        return Collections.singleton(DamageEvent.DDamaged);
    }

    @Override
    public String name() {
        return "JianXiong";
    }

    @Override
    public boolean canActive(GameEngine engine, Enum event, Player player) {
        return player == owner && events().contains(event);
    }

    @Override
    public void onDDamaged(GameEngine engine, DamageSettlement settlement) {
        QueryManager queryManager = engine.getQueryManager();
        int index = queryManager.abilityOptionQuery(settlement.getSufferer(), this,
                TextDictionary.NoOps, TextDictionary.DrawOneCard, TextDictionary.GainDamagingCard);
        switch (index) {
            case 1:
                List<CardBase> cardList = engine.getPcrManager().pollDeckTop(1);
                owner.acquireHand(engine,
                        CardTransferContext.builder().acquireWay(CardAcquireWay.Draw).build(),
                        cardList);
            case 2:
                CardBase card = settlement.getCard();
                if (card == null) {
                    return;
                }
                if (engine.checkCsBuffer(card)) {
                    List<CardBase> cards = engine.recycleCsBuffer(card);
                    if (CollectionUtils.isEmpty(cards)) {
                        return;
                    }
                    owner.acquireHand(engine,
                            CardTransferContext.builder().acquireWay(CardAcquireWay.Other).build(),
                            cards);
                }
        }

    }
}
