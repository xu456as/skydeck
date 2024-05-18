package io.skydeck.gserver.impl;

import io.skydeck.gserver.domain.CardBase;
import io.skydeck.gserver.domain.CardSettlement;
import io.skydeck.gserver.domain.Player;
import io.skydeck.gserver.domain.dto.CardUseDTO;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.enums.CardLostType;
import lombok.Getter;

import java.util.Collections;

public class JinkUseSettlement extends CardSettlement {
    @Getter
    private CardUseDTO jinkUseDTO;
    @Getter
    private CardUseDTO slashUseDTO;

    public static JinkUseSettlement newOne(CardUseDTO slashUseDTO, CardUseDTO jinkUseDTO) {
        JinkUseSettlement settlement = new JinkUseSettlement();
        settlement.slashUseDTO = slashUseDTO;
        settlement.jinkUseDTO = jinkUseDTO;
        return settlement;
    }

    @Override
    public void resolve(GameEngine e) {
        Player defender = jinkUseDTO.getPlayer();
        CardBase cardU = jinkUseDTO.getCard();
        defender.removeCard(e, Collections.singletonList(cardU), CardLostType.Use);
        e.onCardLost(defender, CardLostType.Use, Collections.singletonList(jinkUseDTO.getCard()));
        e.onCardUsing(jinkUseDTO, this);
        e.onCardEffectFinish(jinkUseDTO, this);
        e.onCardUsed(jinkUseDTO, this);
    }
}
