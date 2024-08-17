package io.skydeck.gserver.impl.settlement;

import io.skydeck.gserver.domain.card.CardBase;
import io.skydeck.gserver.domain.player.Player;
import io.skydeck.gserver.domain.settlement.SettlementBase;
import io.skydeck.gserver.domain.dto.CardDiscardDTO;
import io.skydeck.gserver.domain.dto.CardTransferContext;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.enums.CardAcquireWay;
import io.skydeck.gserver.enums.Kingdom;

import java.util.ArrayList;
import java.util.List;

public class DeceaseSettlement extends SettlementBase {
    private Player deceased;
    private Player killer;

    public static DeceaseSettlement newOne(Player deceased, Player killer) {
        DeceaseSettlement settlement = new DeceaseSettlement();
        settlement.deceased = deceased;
        settlement.killer = killer;
        return settlement;
    }

    @Override
    public void resolve(GameEngine engine) {
        deceased.revealFinalKingdom(engine);
        if (killer != null && killer.getKingdom() != Kingdom.Unknown) {
            if (killer.getKingdom() == Kingdom.Am) {
                List<CardBase> reward = engine.getPcrManager().pollDeckTop(3);
                killer.acquireHand(engine,
                        CardTransferContext.builder().acquireWay(CardAcquireWay.Draw).build(),
                        reward);
            } else if (killer.getKingdom() == deceased.getKingdom()) {
                List<CardBase> allCards = new ArrayList<>();
                allCards.addAll(killer.getEquips());
                allCards.addAll(killer.getHands());
                engine.runSettlement(CardDiscardSettlement.newOne(
                        CardDiscardDTO.builder().offender(killer).defender(killer).card(allCards).build()
                ));
            } else {
                List<CardBase> reward = engine.getPcrManager().pollDeckTop(engine.aliveTeamMember(deceased));
                killer.acquireHand(engine,
                        CardTransferContext.builder().acquireWay(CardAcquireWay.Draw).build(),
                        reward);
            }
        }
        deceased.setDead(true);
        engine.onDying(this);
        deceased.clearResource(engine);
    }
}
