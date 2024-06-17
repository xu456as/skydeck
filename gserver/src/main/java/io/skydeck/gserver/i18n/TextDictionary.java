package io.skydeck.gserver.i18n;

import java.util.ArrayList;
import java.util.List;

public enum TextDictionary {
    NoOps,
    GainStagePloy,
    DrawOneCard,
    GainDamagingCard,

    HuWeiOpt0,
    HuWeiOpt1,
    PeekHand,
    PeekPrimaryHero,
    PeekViceHero,

    DropOneHealth,
    DiscardGear,
    ExposePrimaryHero,
    ExposeViceHero,
    Steal,
    Dismantle
    ;


    public static List<String> allianceCheerOption(int val) {
        List<String> newList = new ArrayList<>();
        for (int i = 0; i <= val; i++) {
            newList.add(String.valueOf(i));
        }
        return newList;
    }

    public String i18n() {
        //TODO
        return "todo";
    }
}
