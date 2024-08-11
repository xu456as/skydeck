package io.skydeck.gserver.impl.settlement;

import io.skydeck.gserver.domain.player.Player;
import io.skydeck.gserver.domain.settlement.PloyCardSettlement;
import io.skydeck.gserver.domain.dto.CardUseDTO;
import io.skydeck.gserver.engine.GameEngine;

public class SendLimboSettlement extends PloyCardSettlement {
    public static SendLimboSettlement newOne(CardUseDTO useDTO) {
        SendLimboSettlement settlement = new SendLimboSettlement();
        settlement.useDTO = useDTO;
        return settlement;
    }
    @Override
    public void resolve(GameEngine e) {
        commonResolve(e, this::bizResolve);
    }
    private void bizResolve(GameEngine e, Player target) {
        target.getStageState().setInLimbo(true);
    }
}
