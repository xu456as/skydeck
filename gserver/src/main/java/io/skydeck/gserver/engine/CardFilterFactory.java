package io.skydeck.gserver.engine;

import io.skydeck.gserver.domain.card.CardBase;
import io.skydeck.gserver.domain.card.CardFilter;
import io.skydeck.gserver.enums.CardSubType;
import io.skydeck.gserver.enums.CardType;
import io.skydeck.gserver.enums.Suit;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.function.Function;

@Component
@Scope("prototype")
public class CardFilterFactory {
    public CardFilter newFilter(Function<CardBase, Boolean> func) {
        return func::apply;
    }

    public CardFilter suitFilter(Suit... suits) {
        return (CardBase card) -> {
            return Arrays.stream(suits)
                    .anyMatch(suit -> suit.equals(card.suit()));
        };
    }

    public CardFilter jinkFilter() {
        return (CardBase card) -> card.subType() == CardSubType.Jink;
    }

    public CardFilter cureFilter() {return (CardBase  card) -> card.subType() == CardSubType.Cure;}
    public CardFilter slashFilter() {return (CardBase  card) -> card.subType() == CardSubType.Slash;}

    public CardFilter liquorFilter() { return (CardBase card) -> card.subType() == CardSubType.Liquor; }

    public CardFilter or(CardFilter...  filters) {
        return (CardBase card)  -> {
            for (CardFilter filter : filters) {
                if (filter.filter(card)) {
                    return true;
                }
            }
            return false;
        };
    }

    public CardFilter and(CardFilter...  filters) {
        return (CardBase card)  -> {
            for (CardFilter filter : filters) {
                if (!filter.filter(card)) {
                    return false;
                }
            }
            return true;
        };
    }

    public CardFilter not(CardFilter filter) {
        return (CardBase card)  -> !filter.filter(card);
    }

    public CardFilter gearFilter() {
        return (CardBase card) -> card.type() == CardType.Gear;
    }

    public CardFilter rideFilter() {return (CardBase  card) -> card.subType() == CardSubType.DefenseRide || card.subType() == CardSubType.OffenseRide || card.subType() == CardSubType.SpecialRide;}
}
