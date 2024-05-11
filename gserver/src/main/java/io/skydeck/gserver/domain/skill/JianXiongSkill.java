package io.skydeck.gserver.domain.skill;

import io.skydeck.gserver.domain.CardBase;
import io.skydeck.gserver.domain.DynamicCard;
import io.skydeck.gserver.domain.Player;
import io.skydeck.gserver.domain.SkillBase;
import io.skydeck.gserver.domain.dto.CardTransferContext;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.engine.QueryManager;
import io.skydeck.gserver.enums.CardAcquireWay;
import io.skydeck.gserver.enums.DamageEvent;
import io.skydeck.gserver.impl.DamageSettlement;

import java.util.Collections;
import java.util.List;
import java.util.Set;

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
    public String queryName() {
        return "JianXiong";
    }
    @Override
    public boolean canActive(GameEngine engine, Enum event, Player player) {
        return player == owner && events().contains(event);
    }

    @Override
    public void onDDamaged(GameEngine engine, DamageSettlement settlement) {
        QueryManager queryManager = engine.getQueryManager();
        //todo
        int index = queryManager.abilityOptionQuery(settlement.getSufferer(), this,
                "0", "1", "2");
        switch (index) {
            case 1:
                List<CardBase> cardList = engine.getPcrManager().pollDeckTop(1);
                owner.acquireHand(
                        CardTransferContext.builder().acquireWay(CardAcquireWay.Draw).build(),
                        cardList);
            case 2:
                List<CardBase> csBuffer = engine.getCsBuffer();
                CardBase card = settlement.getCard();
                if (csBuffer.contains(card)) {
                    if (card instanceof DynamicCard dCard && !dCard.virtual()) {
                        List<CardBase> cards = dCard.originCards();
                        csBuffer.remove(card);
                        owner.acquireHand(CardTransferContext.builder().acquireWay(CardAcquireWay.Other).build(),
                                cards);
                    } else {
                        csBuffer.remove(card);
                        owner.acquireHand(CardTransferContext.builder().acquireWay(CardAcquireWay.Other).build(),
                                Collections.singletonList(card));
                    }
                }
        }

    }
}
