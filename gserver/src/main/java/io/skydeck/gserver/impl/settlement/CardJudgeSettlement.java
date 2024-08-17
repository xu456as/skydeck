package io.skydeck.gserver.impl.settlement;

import io.skydeck.gserver.domain.card.CardBase;
import io.skydeck.gserver.domain.settlement.CardSettlement;
import io.skydeck.gserver.domain.player.Player;
import io.skydeck.gserver.domain.settlement.SettlementBase;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.engine.PublicCardResManager;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.function.Consumer;

public class CardJudgeSettlement extends SettlementBase {
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
