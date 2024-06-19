package io.skydeck.gserver.impl;

import io.skydeck.gserver.domain.CardBase;
import io.skydeck.gserver.domain.Player;
import io.skydeck.gserver.domain.PloyCardSettlement;
import io.skydeck.gserver.domain.dto.CardUseDTO;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.enums.Suit;
import io.skydeck.gserver.util.PositionUtil;
import org.apache.commons.collections4.MapUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class SkipDrawPloySettlement extends PloyCardSettlement {
    public SkipDrawPloySettlement newOne(CardUseDTO useDTO) {
        SkipDrawPloySettlement settlement = new SkipDrawPloySettlement();
        settlement.useDTO = useDTO;
        return settlement;
    }
    @Override
    public void resolve(GameEngine e) {
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
                    CardJudgeSettlement judgeSettlement =
                            CardJudgeSettlement.newOne(target, this,
                                    new SkipDraw(e, target));
                    e.runSettlement(judgeSettlement);
                    e.onCardEffected(useDTO, this, target);
                }
            }
        }
        e.onCardEffectFinish(useDTO, this);
    }

    private record SkipDraw(GameEngine e, Player target) implements Consumer<CardBase> {

        @Override
        public void accept(CardBase judgeResult) {
            if (judgeResult.suit() != Suit.Club) {
                target.getStageState().setSkipNextDrawPhase(true);
            }
        }
    }
}
