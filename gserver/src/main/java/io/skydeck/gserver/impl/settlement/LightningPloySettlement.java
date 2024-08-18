package io.skydeck.gserver.impl.settlement;

import io.skydeck.gserver.domain.card.CardBase;
import io.skydeck.gserver.domain.player.Player;
import io.skydeck.gserver.domain.settlement.PloyCardSettlement;
import io.skydeck.gserver.domain.dto.CardUseDTO;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.enums.DamageNature;
import io.skydeck.gserver.enums.Suit;
import io.skydeck.gserver.util.PositionUtil;
import org.apache.commons.collections4.MapUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class LightningPloySettlement extends PloyCardSettlement {

    public static LightningPloySettlement newOne(CardUseDTO useDTO) {
        LightningPloySettlement settlement = new LightningPloySettlement();
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
                                    new Lightning(e, target, useDTO));
                    e.runSettlement(judgeSettlement);
                    e.onCardEffected(useDTO, this, target);
                }
            }
        }
        e.onCardEffectFinish(useDTO, this);
    }

    private record Lightning(GameEngine e, Player user, CardUseDTO useDTO) implements Consumer<CardBase> {
        @Override
        public void accept(CardBase judgeResult) {
            if (user.cardSuitMod(judgeResult) == Suit.Spade) {
                if (judgeResult.number() >= 2 && judgeResult.number() <= 9) {
                    DamageSettlement settlement = DamageSettlement.newOne(null, user,
                            3, DamageNature.Lightning, useDTO.getCard());
                    e.runSettlement(settlement);
                }
            }
        }
    }
}
