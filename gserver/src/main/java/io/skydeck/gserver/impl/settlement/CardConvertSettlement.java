package io.skydeck.gserver.impl.settlement;

import io.skydeck.gserver.domain.card.DynamicCard;
import io.skydeck.gserver.domain.dto.CardConvertDTO;
import io.skydeck.gserver.domain.dto.CardDiscardDTO;
import io.skydeck.gserver.domain.settlement.SettlementBase;
import io.skydeck.gserver.engine.DynamicCardManager;
import io.skydeck.gserver.engine.GameEngine;
import lombok.Getter;

public class CardConvertSettlement extends SettlementBase {
    @Getter
    private CardConvertDTO cardConvertDTO;

    @Getter
    private DynamicCard output;
    public static CardConvertSettlement newOne(CardConvertDTO cardConvertDTO) {
        CardConvertSettlement settlement = new CardConvertSettlement();
        settlement.cardConvertDTO = cardConvertDTO;
        return settlement;
    }
    @Override
    public void resolve(GameEngine engine) {
        DynamicCardManager dcm = engine.getDynamicCardManager();
        output = dcm.convert(cardConvertDTO.getOriginalCards(), cardConvertDTO.getOutputCardName());
    }
}
