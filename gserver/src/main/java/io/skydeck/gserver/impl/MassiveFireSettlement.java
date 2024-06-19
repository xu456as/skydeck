package io.skydeck.gserver.impl;

import io.skydeck.gserver.domain.Player;
import io.skydeck.gserver.domain.PloyCardSettlement;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.enums.DamageNature;

public class MassiveFireSettlement extends PloyCardSettlement {
    @Override
    public void resolve(GameEngine e) {
        commonResolve(e, this::bizResolve);
    }
    private void bizResolve(GameEngine e, Player target) {
        DamageSettlement settlement =
                DamageSettlement.newOne(useDTO.getPlayer(), target, 1, DamageNature.Fire, useDTO.getCard());
        e.runSettlement(settlement);
    }
}
