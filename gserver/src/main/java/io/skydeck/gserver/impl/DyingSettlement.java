package io.skydeck.gserver.impl;

import io.skydeck.gserver.domain.Player;
import io.skydeck.gserver.domain.SettlementBase;
import io.skydeck.gserver.engine.CardFilterFactory;
import io.skydeck.gserver.engine.QueryManager;
import io.skydeck.gserver.engine.SettlementEngine;
import io.skydeck.gserver.util.PositionUtil;

import java.util.Collections;
import java.util.List;

public class DyingSettlement implements SettlementBase {
    private Player player;
    private Player dealer;

    public static DyingSettlement newOne(Player player, Player dealer) {
        DyingSettlement settlement = new DyingSettlement();
        settlement.player = player;
        settlement.dealer = dealer;
        return settlement;
    }

    @Override
    public void resolve(SettlementEngine engine) {
        QueryManager queryManager = engine.getQueryManager();
        CardFilterFactory cardFilterFactory = engine.getCardFilterFactory();
        engine.onDying(this);
        List<Player> sortedPlayers = PositionUtil.positionSort(engine.getCurrentPlayer(), engine.getPlayers())
                .stream()
                .filter(person -> !person.isDead() && !person.isInLimbo())
                .toList();
        for (Player person : sortedPlayers) {
            queryManager.cardUseQuery(person, cardFilterFactory.cureFilter(), Collections.emptyList());
        }
        engine.onRecover(this);

    }
}
