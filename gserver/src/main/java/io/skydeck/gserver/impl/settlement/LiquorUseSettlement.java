package io.skydeck.gserver.impl.settlement;

import io.skydeck.gserver.domain.card.CardBase;
import io.skydeck.gserver.domain.settlement.CardSettlement;
import io.skydeck.gserver.domain.player.Player;
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
        e.onOCardTargeting(useDTO, this);
        e.onDCardTargeting(useDTO, this);
        e.onOCardTargeted(useDTO, this);
        e.onDCardTargeted(useDTO, this);
        if (user.isInDanger()) {
            user.updateHealth(e, 1);
        } else {
            user.getStageState().setDrunk(true);
        }
        e.onCardEffected(useDTO, this, user);
        e.onCardEffectFinish(cardUseDTO, this);
        e.onCardUsed(cardUseDTO, this);
    }
}
