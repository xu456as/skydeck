package io.skydeck.gserver.impl.settlement;

import io.skydeck.gserver.domain.card.CardBase;
import io.skydeck.gserver.domain.player.Player;
import io.skydeck.gserver.domain.settlement.PloyCardSettlement;
import io.skydeck.gserver.domain.dto.CardTransferContext;
import io.skydeck.gserver.domain.dto.CardUseDTO;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.engine.QueryManager;
import io.skydeck.gserver.enums.CardAcquireWay;
import io.skydeck.gserver.enums.CardLostType;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

public class StealUseSettlement extends PloyCardSettlement {
    @Getter
    private CardUseDTO useDTO;


    public static StealUseSettlement newOne(CardUseDTO dto) {
        StealUseSettlement settlement = new StealUseSettlement();
        settlement.useDTO = dto;
        return settlement;
    }
    @Override
    public void resolve(GameEngine e) {
        commonResolve(e, this::bizResolve);
    }
    private void bizResolve(GameEngine e, Player target) {
        Player user = useDTO.getPlayer();
        QueryManager qm = e.getQueryManager();
        CardBase card = qm.pickOneCard(user, target, QueryManager.AREA_ALL);
        if (card == null) {
            return;
        }
        List<CardBase> cardMove = Collections.singletonList(card);
        cardMove = e.onCardLosing(target, CardLostType.Stolen, cardMove);
        target.removeCard(e, cardMove, CardLostType.Stolen);
        e.onCardLost(target, CardLostType.Stolen, cardMove);
        user.acquireHand(e,
                CardTransferContext.builder().acquireWay(CardAcquireWay.Steal).build(),
                cardMove);
    }
}
