package io.skydeck.gserver.impl;

import io.skydeck.gserver.domain.CardBase;
import io.skydeck.gserver.domain.Player;
import io.skydeck.gserver.domain.PloyCardSettlement;
import io.skydeck.gserver.domain.dto.CardUseDTO;
import io.skydeck.gserver.engine.CardFilterFactory;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.engine.QueryManager;
import io.skydeck.gserver.engine.VisibilityManager;
import io.skydeck.gserver.enums.CardLostType;
import io.skydeck.gserver.exception.BizException;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CommonDelayPloySettlement extends PloyCardSettlement {

    public static CommonDelayPloySettlement newOne(CardUseDTO useDTO) {
        CommonDelayPloySettlement settlement = new CommonDelayPloySettlement();
        settlement.useDTO =  useDTO;
        return settlement;
    }
    @Override
    public void resolve(GameEngine engine) {

        Player user = useDTO.getPlayer();
        user.removeCard(engine, Collections.singletonList(useDTO.getCard()), CardLostType.Use);
        engine.onCardLost(user, CardLostType.Use, Collections.singletonList(useDTO.getCard()));
        engine.onCardUsing(useDTO, this);
        engine.onCardTargeting(useDTO, this);
        engine.onCardTargeted(useDTO, this);
        Map<Player, Integer> targetMap = useDTO.getTargets();
        Player target = targetMap.keySet().stream().findAny().orElse(null);
        if (target != null) {
            List<CardBase> cardBases = engine.recycleCsBuffer(useDTO.getCard());
            if (CollectionUtils.isEmpty(cardBases) || cardBases.size() > 1) {
                throw new BizException("target of delay card can't be more than one");
            }
            CardBase judgeCard = cardBases.get(0);
            target.getJudges().add(judgeCard);
        }
        engine.onCardUsed(useDTO, this);
    }
}
