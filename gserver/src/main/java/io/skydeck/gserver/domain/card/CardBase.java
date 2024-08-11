package io.skydeck.gserver.domain.card;

import io.skydeck.gserver.annotation.CardExecMeta;
import io.skydeck.gserver.enums.*;
import io.skydeck.gserver.exception.BizException;

import java.util.Objects;

public abstract class CardBase implements CardProperties, Comparable<CardBase> {
    protected int id;
    protected int number;
    protected Suit suit;
    protected DamageNature nature = DamageNature.None;
    protected boolean union = false;
    protected boolean groupInvincible = false;

    public Integer id() {
        return id;
    }
    @Override
    public String name() {
        CardExecMeta cardExecMeta = this.getClass().getAnnotation(CardExecMeta.class);
        if (cardExecMeta == null) {
            return "";
        }
        return cardExecMeta.cardNameType().getSimple();
    }
    @Override
    public CardNameType nameType() {
        CardExecMeta cardExecMeta = this.getClass().getAnnotation(CardExecMeta.class);
        if (cardExecMeta == null) {
            return null;
        }
        return cardExecMeta.cardNameType();
    }

    @Override
    public CardType type() {
        CardExecMeta cardExecMeta = this.getClass().getAnnotation(CardExecMeta.class);
        if (cardExecMeta == null) {
            return null;
        }
        return cardExecMeta.cardNameType().getCardType();
    }

    @Override
    public CardSubType subType() {
        CardExecMeta cardExecMeta = this.getClass().getAnnotation(CardExecMeta.class);
        if (cardExecMeta == null) {
            return null;
        }
        return cardExecMeta.cardNameType().getCardSubType();
    }

    @Override
    public Integer number() {
        return number;
    }

    @Override
    public Suit suit() {
        return suit;
    }

    @Override
    public DamageNature nature() {
        return nature;
    }

    @Override
    public boolean union() {
        return union;
    }


    @Override
    public int compareTo(CardBase o) {
        return Integer.compare(id(), o.id());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CardBase other)) {
            return false;
        }
        return Objects.equals(id(), other.id());
    }

    // slash,1,Diamond,Normal,false,false,{bonus}
    public static <T extends CardBase> T newStatic(Class<T> tClass, String csv) {
        CardParseRcd rcd = CardPropertiesUtil.parseCsv(csv);
        try {
            T card = tClass.getDeclaredConstructor().newInstance();
            card.id = CardIdGenUtil.genId();
            card.number = rcd.number();
            card.suit = rcd.suit();
            card.nature = rcd.nature();
            card.union = rcd.union();
            card.groupInvincible = rcd.groupInvincible();
            return card;
        } catch (Exception e) {
            throw new BizException("can't create card instance for clazz[%s]".formatted(tClass.getName()), e);
        }
    }
}
