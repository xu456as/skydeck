package io.skydeck.gserver.impl.settlement;

import io.skydeck.gserver.domain.player.Player;
import io.skydeck.gserver.domain.settlement.PloyCardSettlement;
import io.skydeck.gserver.domain.dto.CardUseDTO;
import io.skydeck.gserver.engine.GameEngine;
import lombok.Getter;

public class ChainUseSettlement extends PloyCardSettlement {
    @Getter
    private CardUseDTO useDTO;
    public static ChainUseSettlement newOne(CardUseDTO dto) {
        ChainUseSettlement settlement = new ChainUseSettlement();
        settlement.useDTO = dto;
        return settlement;
    }
    @Override
    public void resolve(GameEngine e) {
        commonResolve(e, this::bizResolve);
    }
    private void bizResolve(GameEngine e, Player target) {
        target.updateChain(e, !target.isChained());
    }
}
