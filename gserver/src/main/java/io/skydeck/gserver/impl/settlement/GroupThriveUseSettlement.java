package io.skydeck.gserver.impl.settlement;

import io.skydeck.gserver.domain.card.CardBase;
import io.skydeck.gserver.domain.settlement.PloyCardSettlement;
import io.skydeck.gserver.domain.dto.CardTransferContext;
import io.skydeck.gserver.domain.dto.CardUseDTO;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.engine.PublicCardResManager;
import io.skydeck.gserver.engine.QueryManager;
import io.skydeck.gserver.enums.CardAcquireWay;

import java.util.Collections;
import java.util.List;

public class GroupThriveUseSettlement extends PloyCardSettlement {

    public static GroupThriveUseSettlement newOne(CardUseDTO useDTO) {
        GroupThriveUseSettlement settlement = new GroupThriveUseSettlement();
        settlement.useDTO = useDTO;
        return settlement;
    }
    @Override
    public void resolve(GameEngine engine) {
        int count = useDTO.getTargets().values().stream()
                .reduce(Integer::sum)
                .orElse(0);
        if (count == 0) {
            return;
        }
        PublicCardResManager pcrManager = engine.getPcrManager();
        QueryManager queryManager = engine.getQueryManager();
        List<CardBase> cards = pcrManager.pollDeckTop(count);
        commonResolve(engine, (eng, target) -> {
            if (cards.isEmpty()) {
                return;
            }
            int idx = queryManager.cardIndexQuery(target, cards);
            CardBase card = cards.get(idx);
            cards.remove(idx);
            target.acquireHand(eng, CardTransferContext.builder().acquireWay(CardAcquireWay.SpecialDraw).build(),
                    Collections.singletonList(card));
        });
    }
}
