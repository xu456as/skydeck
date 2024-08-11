package io.skydeck.gserver.util;

import io.skydeck.gserver.domain.player.Player;
import io.skydeck.gserver.enums.CardType;

public class PlayerUtil {
    public static int gearCount(Player player) {
        int count = player.getEquips().size();
        count += player.getHands().stream().filter(c -> c.type() == CardType.Gear).count();
        return count;
    }

}
