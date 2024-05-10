package io.skydeck.gserver.impl;

import io.skydeck.gserver.domain.Player;
import io.skydeck.gserver.domain.SettlementBase;
import io.skydeck.gserver.engine.GameEngine;

public class DyingSettlement extends SettlementBase {
    private Player deceased;
    private Player killer;

    public static DyingSettlement newOne(Player deceased, Player killer) {
        DyingSettlement settlement = new DyingSettlement();
        settlement.deceased = deceased;
        settlement.killer = killer;
        return settlement;
    }

    @Override
    public void resolve(GameEngine engine) {
        //TODO punishment and reward
        deceased.setDead(true);
        engine.onDying(this);

        deceased.clearExtraResource(engine);
    }
}
