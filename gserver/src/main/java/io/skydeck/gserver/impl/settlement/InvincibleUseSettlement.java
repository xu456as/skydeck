package io.skydeck.gserver.impl.settlement;

import io.skydeck.gserver.domain.player.Player;
import io.skydeck.gserver.domain.settlement.PloyCardSettlement;
import io.skydeck.gserver.domain.dto.CardUseDTO;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.enums.CardLostType;
import lombok.Getter;

import java.util.Collections;

public class InvincibleUseSettlement extends PloyCardSettlement {
    private PloyCardSettlement counterSettlement;
    @Getter
    private boolean success = false;
    public static InvincibleUseSettlement newOne(CardUseDTO useDTO) {
        InvincibleUseSettlement settlement = new InvincibleUseSettlement();
        settlement.useDTO = useDTO;
        settlement.counterSettlement = (PloyCardSettlement) useDTO.getCounterSettlement();
        return settlement;
    }
    @Override
    public void resolve(GameEngine e) {
        Player user = useDTO.getPlayer();
        user.removeCard(e, Collections.singletonList(useDTO.getCard()), CardLostType.Use);
        e.onCardLost(user, CardLostType.Use, Collections.singletonList(useDTO.getCard()));
        e.onCardUsing(useDTO, this);
//        e.onCardTargeting(useDTO, this);
//        e.onCardTargeted(useDTO, this);
        boolean valid = e.onCardEffecting(useDTO, this, null);
        if (valid) {
            e.onCardEffected(useDTO, this, null);
        }
        counterSuccess();
        e.onCardEffectFinish(useDTO, this);
        e.onCardUsed(useDTO, this);
    }
    private void counterSuccess() {
        success = true;
        if (counterSettlement instanceof InvincibleUseSettlement) {
            return;
        }
        CardUseDTO counterUseDTO = counterSettlement.getUseDTO();
        useDTO.getTargets().forEach((Player target, Integer count) -> {
            int originCount = counterUseDTO.getTargets().getOrDefault(target, 0);
            if (originCount == 0) {
                return;
            }
            int newCount = Math.max(originCount - count, 0);
            counterUseDTO.getTargets().put(target, newCount);
        });
    }
}
