package io.skydeck.gserver.impl.settlement;

import io.skydeck.gserver.domain.player.Player;
import io.skydeck.gserver.domain.settlement.PloyCardSettlement;
import io.skydeck.gserver.domain.dto.CardSacrificeDTO;
import io.skydeck.gserver.domain.dto.CardUseDTO;
import io.skydeck.gserver.engine.CardFilterFactory;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.engine.QueryManager;
import io.skydeck.gserver.enums.DamageNature;

public class MassiveJinkUseSettlement extends PloyCardSettlement {
    public static MassiveJinkUseSettlement newOne(CardUseDTO useDTO) {
        MassiveJinkUseSettlement settlement = new MassiveJinkUseSettlement();
        settlement.useDTO = useDTO;
        return settlement;
    }
    @Override
    public void resolve(GameEngine e) {
        commonResolve(e, this::bizResolve);
    }
    private void bizResolve(GameEngine e, Player target) {
        QueryManager queryManager = e.getQueryManager();
        CardFilterFactory cff = e.getCardFilterFactory();
        CardSacrificeDTO sacrificeDTO = queryManager.cardSacrificeQuery(target, cff.jinkFilter(), null);
        if (sacrificeDTO != null) {
            e.runSettlement(CardSacrificeSettlement.newOne(sacrificeDTO));
        } else {
            DamageSettlement settlement = DamageSettlement.newOne(useDTO.getPlayer(), target,
                    1, DamageNature.Normal, useDTO.getCard());
            e.runSettlement(settlement);
        }
    }

}
