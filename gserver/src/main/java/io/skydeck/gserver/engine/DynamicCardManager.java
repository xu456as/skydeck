package io.skydeck.gserver.engine;

import io.skydeck.gserver.domain.CardBase;
import io.skydeck.gserver.domain.DynamicCard;
import io.skydeck.gserver.domain.GearCardBase;
import io.skydeck.gserver.enums.*;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;

public class DynamicCardManager {
    public DynamicCard convert(List<CardBase> cards, CardNameType cardNameType) {
        return new DynamicCard() {
            @Override
            public boolean virtual() {
                return false;
            }

            @Override
            public List<CardBase> originCards() {
                return cards;
            }

            @Override
            public String name() {
                return cardNameType.name();
            }

            @Override
            public CardNameType nameType() {
                return cardNameType;
            }

            @Override
            public Integer number() {
                return commonNumber(cards);
            }

            @Override
            public Color color() {
                return commonColor(cards);
            }

            @Override
            public Suit suit() {
                return commonSuit(cards);
            }

            @Override
            public CardType type() {
                return cardNameType.getCardType();
            }

            @Override
            public CardSubType subType() {
                return cardNameType.getCardSubType();
            }
        };
    }

    public DynamicCard imitate(CardNameType cardNameType) {
        return new DynamicCard() {
            @Override
            public boolean virtual() {
                return true;
            }

            @Override
            public List<CardBase> originCards() {
                return Collections.emptyList();
            }

            @Override
            public String name() {
                return cardNameType.name();
            }

            @Override
            public CardNameType nameType() {
                return cardNameType;
            }

            @Override
            public Integer number() {
                return null;
            }

            @Override
            public Color color() {
                return null;
            }

            @Override
            public Suit suit() {
                return null;
            }

            @Override
            public CardType type() {
                return cardNameType.getCardType();
            }

            @Override
            public CardSubType subType() {
                return cardNameType.getCardSubType();
            }
        };
    }

    private Integer commonNumber(List<CardBase> cards) {
        if (CollectionUtils.isEmpty(cards)) {
            return null;
        }
        if (cards.stream().map(CardBase::number).distinct().count() == 1) {
            return cards.get(0).number();
        }
        return null;
    }
    private Color commonColor(List<CardBase> cards) {
        if (CollectionUtils.isEmpty(cards)) {
            return null;
        }
        if (cards.stream().map(CardBase::color).distinct().count() == 1) {
            return cards.get(0).color();
        }
        return null;
    }
    private Suit commonSuit(List<CardBase> cards) {
        if (CollectionUtils.isEmpty(cards)) {
            return null;
        }
        if (cards.stream().map(CardBase::suit).distinct().count() == 1) {
            return cards.get(0).suit();
        }
        return null;
    }
}
