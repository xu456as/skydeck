package io.skydeck.gserver.impl.settlement;

import io.skydeck.gserver.domain.card.CardBase;
import io.skydeck.gserver.domain.player.Player;
import io.skydeck.gserver.domain.settlement.PloyCardSettlement;
import io.skydeck.gserver.domain.dto.CardDiscardDTO;
import io.skydeck.gserver.domain.dto.CardUseDTO;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.engine.QueryManager;

public class DismantleUseSettlement extends PloyCardSettlement {

    public DismantleUseSettlement newOne(CardUseDTO useDTO) {
        DismantleUseSettlement settlement = new DismantleUseSettlement();
        settlement.useDTO = useDTO;
        return settlement;
    }
    @Override
    public void resolve(GameEngine e) {
        commonResolve(e, this::bizResolve);
    }
    private void bizResolve(GameEngine e, Player target) {
        Player user = useDTO.getPlayer();
        QueryManager qm = e.getQueryManager();
        CardBase card = qm.pickOneCard(user, target, QueryManager.AREA_ALL);
        if (card == null) {
            return;
        }
        CardDiscardSettlement ds = CardDiscardSettlement.newOne(
                CardDiscardDTO.builder().offender(user).defender(target).build()
        );
        e.runSettlement(ds);
    }
}
