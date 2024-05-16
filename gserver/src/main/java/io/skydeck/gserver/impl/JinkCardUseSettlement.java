package io.skydeck.gserver.impl;

import io.skydeck.gserver.domain.CardBase;
import io.skydeck.gserver.domain.CardSettlement;
import io.skydeck.gserver.domain.Player;
import io.skydeck.gserver.domain.dto.CardUseDTO;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.enums.CardLostType;

import java.util.Collections;

public class JinkCardUseSettlement extends CardSettlement {
    private CardUseDTO jinkUseDTO;
    private CardUseDTO slashUseDTO;

    public static JinkCardUseSettlement newOne(CardUseDTO slashUseDTO, CardUseDTO jinkUseDTO) {
        JinkCardUseSettlement settlement = new JinkCardUseSettlement();
        settlement.slashUseDTO = slashUseDTO;
        settlement.jinkUseDTO = jinkUseDTO;
        return settlement;
    }

    @Override
    public void resolve(GameEngine e) {
        Player defender = jinkUseDTO.getPlayer();
        CardBase cardU = jinkUseDTO.getCard();
        defender.removeCard(e, Collections.singletonList(cardU), CardLostType.Use);
        defender.getStageState().setUseCardCount(
                defender.getStageState().getUseCardCount() + 1
        );
        e.onCardLost(defender, CardLostType.Use, Collections.singletonList(jinkUseDTO.getCard()));
        e.onCardUsing(jinkUseDTO, this);
        e.onCardEffectFinish(jinkUseDTO, this);
        e.onCardUsed(jinkUseDTO, this);
    }
}
