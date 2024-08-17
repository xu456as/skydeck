package io.skydeck.gserver.impl.settlement;

import io.skydeck.gserver.domain.card.CardBase;
import io.skydeck.gserver.domain.settlement.CardSettlement;
import io.skydeck.gserver.domain.player.Player;
import io.skydeck.gserver.domain.dto.CardReframeDTO;
import io.skydeck.gserver.domain.dto.CardTransferContext;
import io.skydeck.gserver.domain.settlement.SettlementBase;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.enums.CardAcquireWay;
import io.skydeck.gserver.enums.CardLostType;
import lombok.Getter;

import java.util.List;

public class CardReframeSettlement extends SettlementBase {

    @Getter
    private CardReframeDTO reframeDTO;

    public static CardReframeSettlement newOne(CardReframeDTO reframeDTO) {
        CardReframeSettlement settlement = new CardReframeSettlement();
        settlement.reframeDTO = reframeDTO;
        return settlement;
    }

    @Override
    public void resolve(GameEngine e) {
        Player user = reframeDTO.getPlayer();
        user.removeCard(e, reframeDTO.getCard(), CardLostType.Use);
        e.onCardLost(user, CardLostType.Reframe, reframeDTO.getCard());
        e.onCardReframing(reframeDTO, this);
        List<CardBase> newCards = e.getPcrManager().pollDeckTop(reframeDTO.getCard().size());
        user.acquireHand(e,
                CardTransferContext.builder().acquireWay(CardAcquireWay.Draw).build(),
                newCards);
    }
}
