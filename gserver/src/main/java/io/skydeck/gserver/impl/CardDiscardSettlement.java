package io.skydeck.gserver.impl;

import io.skydeck.gserver.domain.CardBase;
import io.skydeck.gserver.domain.CardSettlement;
import io.skydeck.gserver.domain.Player;
import io.skydeck.gserver.domain.dto.CardDiscardDTO;
import io.skydeck.gserver.domain.dto.CardSacrificeDTO;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.enums.CardLostEvent;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CardDiscardSettlement extends CardSettlement {
    private CardDiscardDTO discardDTO;
    public static CardDiscardSettlement newOne(CardDiscardDTO discardDTO) {
        CardDiscardSettlement settlement = new CardDiscardSettlement();
        settlement.discardDTO = discardDTO;
        return settlement;
    }
    @Override
    public void resolve(GameEngine e) {
        Player player = discardDTO.getPlayer();
        List<CardBase> cardDiscard = discardDTO.getCard();
        Collection<CardBase> handCardCol = CollectionUtils.intersection(player.getHands(), cardDiscard);
        Collection<CardBase> equipCardCol = CollectionUtils.intersection(player.getEquips(), cardDiscard);
        player.removeHand(e, new ArrayList<>(handCardCol));
        player.removeEquip(e, new ArrayList<>(equipCardCol));
        List<CardBase> discardedCard = Stream.of(handCardCol, equipCardCol).flatMap(Collection::stream).toList();
        player.getStageState().setDiscardCardCount(
                player.getStageState().getDiscardCardCount() + discardedCard.size()
        );
        e.onCardDiscarded(player, discardedCard);
        e.onCardLost(player, CardLostEvent.Discard, discardedCard);
        e.onCardBurying(player, CardLostEvent.Discard, discardedCard);
        e.purgeCsBuffer();
        e.onCardBuried(player, CardLostEvent.Discard, discardedCard);
    }
}
