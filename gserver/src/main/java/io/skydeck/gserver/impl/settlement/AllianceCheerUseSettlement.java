package io.skydeck.gserver.impl.settlement;

import io.skydeck.gserver.domain.card.CardBase;
import io.skydeck.gserver.domain.player.Player;
import io.skydeck.gserver.domain.settlement.PloyCardSettlement;
import io.skydeck.gserver.domain.dto.CardTransferContext;
import io.skydeck.gserver.domain.dto.CardUseDTO;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.i18n.TextDictionary;

import java.util.List;

public class AllianceCheerUseSettlement extends PloyCardSettlement {
    public AllianceCheerUseSettlement newOne(CardUseDTO useDTO) {
        AllianceCheerUseSettlement settlement = new AllianceCheerUseSettlement();
        settlement.useDTO = useDTO;
        return settlement;
    }
    @Override
    public void resolve(GameEngine e) {
        commonResolve(e, (GameEngine eng, Player target) -> {
            if (target == useDTO.getPlayer()) {
                int benefitPoint = useDTO.getTargets().size() - 1;
                if (benefitPoint <= 0) {
                    return;
                }
                int opt = e.getQueryManager().optionQuery(target, TextDictionary.allianceCheerOption(benefitPoint));
                target.updateHealth(e, opt);
                int drawCount = benefitPoint - opt;
                if (drawCount > 0) {
                    List<CardBase> cards = e.getPcrManager().pollDeckTop(drawCount);
                    target.acquireHand(e, CardTransferContext.draw(), cards);
                }
            } else {
                List<CardBase> cards = e.getPcrManager().pollDeckTop(1);
                target.acquireHand(e, CardTransferContext.draw(), cards);
            }
        });
    }
}
