package io.skydeck.gserver.domain.settlement;

import io.skydeck.gserver.domain.dto.CardUseDTO;
import io.skydeck.gserver.domain.player.Player;
import io.skydeck.gserver.domain.settlement.CardSettlement;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.enums.CardLostType;
import io.skydeck.gserver.util.PositionUtil;
import lombok.Getter;
import org.apache.commons.collections4.MapUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public abstract class PloyCardSettlement extends CardSettlement {

    protected void commonResolve(GameEngine e, BiConsumer<GameEngine, Player> func) {
        Player user = useDTO.getPlayer();
        user.removeCard(e, Collections.singletonList(useDTO.getCard()), CardLostType.Use);
        e.onCardLost(user, CardLostType.Use, Collections.singletonList(useDTO.getCard()));
        e.onCardUsing(useDTO, this);
        e.onOCardTargeting(useDTO, this);
        e.onDCardTargeting(useDTO, this);
        e.onOCardTargeted(useDTO, this);
        e.onDCardTargeted(useDTO, this);
        Map<Player, Integer> targetMap = useDTO.getTargets();
        if (!MapUtils.isEmpty(targetMap)) {
            List<Player> sortedList = PositionUtil.positionSort(e.getCurrentPlayer(), targetMap.keySet());
            for (Player target : sortedList) {
                int effectCnt = targetMap.get(target);
                for (int i = 0; i < effectCnt; i++) {
                    boolean valid = e.onCardEffecting(useDTO, this, target);
                    if (!valid || target.isDead()) {
                        continue;
                    }
                    func.accept(e, target);
                    e.onCardEffected(useDTO, this, target);
                }
            }
        }
        e.onCardEffectFinish(useDTO, this);
        e.onCardUsed(useDTO, this);
    }
}
