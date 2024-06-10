package io.skydeck.gserver.impl;

import io.skydeck.gserver.domain.CardBase;
import io.skydeck.gserver.domain.Player;
import io.skydeck.gserver.domain.PloyCardSettlement;
import io.skydeck.gserver.domain.dto.CardDiscardDTO;
import io.skydeck.gserver.domain.dto.CardTransferContext;
import io.skydeck.gserver.domain.dto.CardUseDTO;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.engine.QueryManager;
import io.skydeck.gserver.enums.CardAcquireWay;
import io.skydeck.gserver.enums.CardLostType;
import io.skydeck.gserver.i18n.TextDictionary;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
