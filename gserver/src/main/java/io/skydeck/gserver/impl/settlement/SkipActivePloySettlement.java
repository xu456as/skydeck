package io.skydeck.gserver.impl.settlement;

import io.skydeck.gserver.domain.card.CardBase;
import io.skydeck.gserver.domain.player.Player;
import io.skydeck.gserver.domain.settlement.PloyCardSettlement;
import io.skydeck.gserver.domain.dto.CardUseDTO;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.enums.Suit;
import io.skydeck.gserver.util.PositionUtil;
import org.apache.commons.collections4.MapUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class SkipActivePloySettlement extends PloyCardSettlement {
    public SkipActivePloySettlement newOne(CardUseDTO useDTO) {
        SkipActivePloySettlement settlement = new SkipActivePloySettlement();
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
                                    new SkipActive(e, target));
                    e.runSettlement(judgeSettlement);
                    e.onCardEffected(useDTO, this, target);
                }
            }
        }
        e.onCardEffectFinish(useDTO, this);
    }

    private record SkipActive(GameEngine e, Player target) implements Consumer<CardBase> {

        @Override
        public void accept(CardBase judgeResult) {
            if (target.cardSuitMod(e, judgeResult) != Suit.Heart) {
                target.getStageState().setSkipNextActivePhase(true);
            }
        }
    }
}
