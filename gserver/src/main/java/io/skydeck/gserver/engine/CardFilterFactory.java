package io.skydeck.gserver.engine;

import io.skydeck.gserver.domain.card.CardBase;
import io.skydeck.gserver.domain.card.CardFilterIface;
import io.skydeck.gserver.enums.CardSubType;
import io.skydeck.gserver.enums.CardType;
import io.skydeck.gserver.enums.Suit;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.function.Function;

@Component
public class CardFilterFactory {
    public CardFilterIface newFilter(Function<CardBase, Boolean> func) {
        return func::apply;
    }

    public CardFilterIface suitFilter(Suit... suits) {
        return (CardBase card) -> {
            return Arrays.stream(suits)
                    .anyMatch(suit -> suit.equals(card.suit()));
        };
    }

    public CardFilterIface jinkFilter() {
        return (CardBase card) -> card.subType() == CardSubType.Jink;
    }

    public CardFilterIface cureFilter() {return (CardBase  card) -> card.subType() == CardSubType.Cure;}
    public CardFilterIface slashFilter() {return (CardBase  card) -> card.subType() == CardSubType.Slash;}

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

    public CardFilterIface gearFilter() {
        return (CardBase card) -> card.type() == CardType.Gear;
    }
}
