package io.skydeck.gserver.engine;

import io.skydeck.gserver.domain.CardBase;
import io.skydeck.gserver.domain.CardFilterIface;
import io.skydeck.gserver.enums.CardSubType;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class CardFilterFactory {
    public CardFilterIface newFilter(Function<CardBase, Boolean> func) {
        return func::apply;
    }

    public CardFilterIface jinkFilter() {
        return (CardBase card) -> card.subType() == CardSubType.Jink;
    }

    public CardFilterIface cureFilter() {return (CardBase  card) -> card.subType() == CardSubType.Cure;}

    public CardFilterIface liquorFilter() { return (CardBase card) -> card.subType() == CardSubType.Liquor; }

    public CardFilterIface or(CardFilterIface...  filters) {
        return (CardBase card)  -> {
            for (CardFilterIface filter : filters) {
                if (filter.filter(card)) {
                    return true;
                }
            }
            return false;
        };
    }

    public CardFilterIface and(CardFilterIface...  filters) {
        return (CardBase card)  -> {
            for (CardFilterIface filter : filters) {
                if (!filter.filter(card)) {
                    return false;
                }
            }
            return true;
        };
    }

    public CardFilterIface not(CardFilterIface filter) {
        return (CardBase card)  -> !filter.filter(card);
    }
}
