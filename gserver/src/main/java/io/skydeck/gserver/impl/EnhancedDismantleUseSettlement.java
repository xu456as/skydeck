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

import java.util.*;

public class EnhancedDismantleUseSettlement extends PloyCardSettlement {
    @Getter
    private CardUseDTO useDTO;

    @Getter
    @Setter
    private boolean enhanced = false;

    public static EnhancedDismantleUseSettlement newOne(CardUseDTO dto) {
        EnhancedDismantleUseSettlement settlement = new EnhancedDismantleUseSettlement();
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
        if (enhanced) {
            Map<Integer, String> options = new HashMap<>(){{
                put(0, TextDictionary.Steal.i18n());
                put(1, TextDictionary.Dismantle.i18n());
            }};
            while (!options.isEmpty()) {
                int index = qm.optionQuery(user, options);
                options.remove(index);
                switch (index) {
                    case 0 -> steal(e, user, target);
                    case 1 -> dismantle(e, user, target);
                }
            }
        } else {
            int distance = e.distance(user, target);
            if (distance <= 1) {
                steal(e, user, target);
            } else {
                dismantle(e, user, target);
            }
        }
    }
    private void steal(GameEngine e, Player offender, Player defender) {
        QueryManager qm = e.getQueryManager();
        CardBase card = qm.pickOneCard(offender, defender, QueryManager.AREA_ALL);
        if (card == null) {
            return;
        }
        List<CardBase> cardMove = Collections.singletonList(card);
        cardMove = e.onCardLosing(defender, CardLostType.Stolen, cardMove);
        defender.removeCard(e, cardMove, CardLostType.Stolen);
        e.onCardLost(defender, CardLostType.Stolen, cardMove);
        offender.acquireHand(e,
                CardTransferContext.builder().acquireWay(CardAcquireWay.Steal).build(),
                cardMove);
    }
    private void dismantle(GameEngine e, Player offender, Player defender) {
        QueryManager qm = e.getQueryManager();
        CardBase card = qm.pickOneCard(offender, defender, QueryManager.AREA_ALL);
        if (card == null) {
            return;
        }
        CardDiscardSettlement ds = CardDiscardSettlement.newOne(
                CardDiscardDTO.builder().offender(offender).defender(defender).build()
        );
        e.runSettlement(ds);
    }
}
