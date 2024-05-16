package io.skydeck.gserver.impl;

import io.skydeck.gserver.domain.CardBase;
import io.skydeck.gserver.domain.CardSettlement;
import io.skydeck.gserver.domain.Player;
import io.skydeck.gserver.domain.dto.CardUseDTO;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.enums.CardLostType;
import io.skydeck.gserver.util.PositionUtil;
import org.apache.commons.collections4.MapUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CureCardUseSettlement extends CardSettlement {
    private CardUseDTO cardUseDTO;

    public static CureCardUseSettlement newOne(CardUseDTO cureCardUse) {
        CureCardUseSettlement settlement = new CureCardUseSettlement();
        settlement.cardUseDTO =  cureCardUse;
        return settlement;
    }
    @Override
    public void resolve(GameEngine e) {
        //TODO
        Player user = cardUseDTO.getPlayer();
        CardBase cardU = cardUseDTO.getCard();
        user.removeCard(e, Collections.singletonList(cardU), CardLostType.Use);
        user.getStageState().setUseCardCount(
                user.getStageState().getUseCardCount() + 1
        );
        e.onCardLost(user, CardLostType.Use, Collections.singletonList(cardUseDTO.getCard()));
        e.onCardUsing(cardUseDTO, this);
        Map<Player, Integer> targetMap = cardUseDTO.getTargets();
        if (!MapUtils.isEmpty(targetMap)) {
            List<Player> sortedList = PositionUtil.positionSort(e.getCurrentPlayer(), targetMap.keySet());
            for (Player target : sortedList) {
                int effectCnt = targetMap.get(target);
                for (int i = 0; i < effectCnt; i++) {
                    target.updateHealth(e, 1);
                }
            }
        }
        e.onCardEffectFinish(cardUseDTO, this);
        e.onCardUsed(cardUseDTO, this);
    }
}
