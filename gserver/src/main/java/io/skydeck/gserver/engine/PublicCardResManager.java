package io.skydeck.gserver.engine;

import io.skydeck.gserver.domain.card.CardBase;
import io.skydeck.gserver.domain.card.DynamicCard;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.rng.core.source32.JDKRandom;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class PublicCardResManager {

//    head in ground list
    private LinkedList<CardBase> deck;
    private Set<CardBase> grave;

    public void addToGrave(GameEngine e, List<CardBase> cards) {
        List<CardBase> realCards = new ArrayList<>();
        for (CardBase card : cards) {
            if (card instanceof DynamicCard dCard) {
                if (dCard.virtual()) {
                    continue;
                }
                realCards.addAll(dCard.originCards());
            } else {
                realCards.add(card);
            }
        }
        grave.addAll(realCards);
    }
    public void removeFromGrave(List<CardBase> cards) {
        grave.removeAll(cards);
    }

    public List<CardBase> pollDeckTop(int number) {
        List<CardBase> list = new ArrayList<>();
        if (deck.size() <= number) {
            list.addAll(deck);
            deck.clear();
        } else {
            Iterator<CardBase> it = deck.descendingIterator();
            for (int i = 0; i < number; i++) {
                list.add(it.next());
                it.remove();
            }
            Collections.reverse(list);
        }
        return list;
    }
    public List<CardBase> peekDeckTop(int number) {
        List<CardBase> list = new ArrayList<>();
        if (deck.size() <= number) {
            list.addAll(deck);
        } else {
            Iterator<CardBase> it = deck.descendingIterator();
            for (int i = 0; i < number; i++) {
                list.add(it.next());
            }
            Collections.reverse(list);
        }
        return list;
    }
    public void removeFromDeck(List<CardBase> cards) {
        deck.removeAll(cards);
    }
    public void addDeckTop(List<CardBase> cards) {
        for (int i = 0; i < cards.size(); i++) {
            int idx = cards.size() - 1 - i;
            deck.addFirst(cards.get(idx));
        }
    }
    public void addDeckBottom(List<CardBase> cards) {
        for (CardBase card : cards) {
            deck.addLast(card);
        }
    }

    public List<CardBase> queryGraveByName(String name) {
        return grave.stream()
                .filter(card -> StringUtils.equals(name, card.name()))
                .findAny().stream()
                .toList();
    }

    private void shuffle() {
        if (!deck.isEmpty()) {
            return;
        }
        List<CardBase> cards = new ArrayList<>(grave);
        JDKRandom random = new JDKRandom(System.currentTimeMillis());
        int size = cards.size();
        for (int i = 0; i < size; i++) {
            int idx = random.nextInt(size - i);
            CardBase last = cards.get(size - 1 - i);
            cards.set(size - 1 - i, cards.get(idx));
            cards.set(idx, last);
            deck.add(cards.get(size - 1 - i));
        }
    }
}
