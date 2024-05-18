package io.skydeck.gserver.impl;

import io.skydeck.gserver.domain.CardBase;
import io.skydeck.gserver.domain.CardSettlement;
import io.skydeck.gserver.domain.Player;
import io.skydeck.gserver.domain.dto.CardUseDTO;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.enums.CardLostType;
import lombok.Getter;

import java.util.Collections;

public class LiquorUseSettlement extends CardSettlement {

    @Getter
    private CardUseDTO cardUseDTO;

    public static LiquorUseSettlement newOne(CardUseDTO cardUseDTO) {
        LiquorUseSettlement settlement = new LiquorUseSettlement();
        settlement.cardUseDTO = cardUseDTO;
        return settlement;
    }

    @Override
    public void resolve(GameEngine e) {
        Player user = cardUseDTO.getPlayer();
        CardBase cardU = cardUseDTO.getCard();
        user.removeCard(e, Collections.singletonList(cardU), CardLostType.Use);
        e.onCardLost(user, CardLostType.Use, Collections.singletonList(cardU));
        e.onCardUsing(cardUseDTO, this);
        if (user.isInDanger()) {
            user.updateHealth(e, 1);
        } else {
            user.getStageState().setDrunk(true);
        }
        e.onCardEffectFinish(cardUseDTO, this);
        e.onCardUsed(cardUseDTO, this);
    }
}
