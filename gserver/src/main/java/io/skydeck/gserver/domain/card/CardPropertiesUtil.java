package io.skydeck.gserver.domain.card;

import io.skydeck.gserver.domain.CardProperties;
import io.skydeck.gserver.domain.card.basic.CardSlash;
import io.skydeck.gserver.enums.*;

import java.util.regex.Pattern;

public class CardPropertiesUtil {
    public static CardParseRcd parseCsv(String csv) {
        String[] strList = csv.trim().split(Pattern.quote(","));
        DamageNature nature = DamageNature.Normal;
        if (strList.length >= 4) {
            nature = DamageNature.valueOf(strList[3]);
        }
        boolean union = false;
        if (strList.length >= 5) {
            union = Boolean.parseBoolean(strList[4]);
        }
        boolean gInvincible = false;
        if (strList.length >= 6) {
            gInvincible = Boolean.parseBoolean(strList[5]);
        }
        String bonus = "";
        if (strList.length >= 7) {
            bonus = strList[6];
        }
        return new CardParseRcd(
                Integer.parseInt(strList[1]),
                Suit.valueOf(strList[2]),
                nature,
                union,
                gInvincible,
                bonus
        );
    }
}
