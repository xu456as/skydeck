package io.skydeck.gserver.engine;

import io.skydeck.gserver.domain.CardBase;
import io.skydeck.gserver.domain.CardFilterIface;
import io.skydeck.gserver.enums.CardSubType;

import java.util.function.Function;

public class CardFilterFactory {
    public CardFilterIface newFilter(Function<CardBase, Boolean> func) {
        return func::apply;
    }

    public CardFilterIface jinkFilter() {
        return (CardBase card) -> card.subType() == CardSubType.Jink;
    }

    public CardFilterIface cureFilter() {return (CardBase  card) -> card.subType() == CardSubType.Cure;}
}
