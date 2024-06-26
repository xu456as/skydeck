package io.skydeck.gserver.impl;

import io.skydeck.gserver.domain.CardBase;
import io.skydeck.gserver.domain.CardSettlement;
import io.skydeck.gserver.domain.Player;
import io.skydeck.gserver.domain.SettlementBase;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.engine.PublicCardResManager;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.function.Consumer;

public class CardJudgeSettlement extends CardSettlement {
    @Getter
    private Player user;
    @Getter
    @Setter
    private CardBase result;
    @Getter
    private SettlementBase parentSettlement;
    private Consumer<CardBase> action;
    public static CardJudgeSettlement newOne(Player user, SettlementBase parentSettlement, Consumer<CardBase> action) {
        CardJudgeSettlement settlement = new CardJudgeSettlement();
        settlement.parentSettlement = parentSettlement;
        settlement.user = user;
        settlement.action = action;
        return settlement;
    }

    @Override
    public void resolve(GameEngine e) {
        PublicCardResManager pcrManager = e.getPcrManager();
        List<CardBase> card = pcrManager.pollDeckTop(1);
        result = card.get(0);
        e.onCardJudgeEffecting(this);
        action.accept(result);
        e.onCardJudgeEffected(this);
        //todo
    }
}
