package io.skydeck.gserver.impl.settlement;

import io.skydeck.gserver.domain.player.Player;
import io.skydeck.gserver.domain.settlement.PloyCardSettlement;
import io.skydeck.gserver.domain.dto.CardUseDTO;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.enums.DamageNature;

public class MassiveFireUseSettlement extends PloyCardSettlement {
    public static MassiveFireUseSettlement newOne(CardUseDTO useDTO) {
        MassiveFireUseSettlement settlement = new MassiveFireUseSettlement();
        settlement.useDTO = useDTO;
        return settlement;
    }
    @Override
    public void resolve(GameEngine e) {
        commonResolve(e, this::bizResolve);
    }
    private void bizResolve(GameEngine e, Player target) {
        DamageSettlement settlement =
                DamageSettlement.newOne(useDTO.getPlayer(), target, 1, DamageNature.Fire, useDTO.getCard(), this);
        e.runSettlement(settlement);
    }
}
