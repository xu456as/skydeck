package io.skydeck.gserver.impl;

import io.skydeck.gserver.domain.Player;
import io.skydeck.gserver.domain.PloyCardSettlement;
import io.skydeck.gserver.domain.dto.CardUseDTO;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.engine.PublicCardResManager;
import io.skydeck.gserver.util.PositionUtil;
import org.apache.commons.collections4.MapUtils;

import java.util.List;
import java.util.Map;

public class LightningPloySettlement extends PloyCardSettlement {

    public static LightningPloySettlement newOne(CardUseDTO useDTO) {
        LightningPloySettlement settlement = new LightningPloySettlement();
        settlement.useDTO = useDTO;
        return settlement;
    }
    @Override
    public void resolve(GameEngine e) {
        PublicCardResManager pcrManager = e.getPcrManager();
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
                    //TODO: judge
                    //
                    e.onCardEffected(useDTO, this, target);
                }
            }
        }
        e.onCardEffectFinish(useDTO, this);
    }
}
