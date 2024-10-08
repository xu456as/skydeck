package io.skydeck.gserver.domain.dto;


import io.skydeck.gserver.domain.card.CardBase;
import io.skydeck.gserver.domain.settlement.CardSettlement;
import io.skydeck.gserver.domain.player.Player;
import io.skydeck.gserver.domain.settlement.SettlementBase;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;



public class CardUseDTO {
    @Getter
    @Setter
    private Player player;
    @Getter
    @Setter
    private CardBase card;
    @Getter
    private Map<Player, Integer> targets = new HashMap<>();
    @Getter
    @Setter
    private Object extraInfo;
    @Getter
    @Setter
    private SettlementBase counterSettlement;

    public CardUseDTO addTarget(Player target) {
        targets.put(target, 1);
        return this;
    }
    public CardUseDTO addTargets(Collection<Player> targetCol) {
        if (CollectionUtils.isEmpty(targetCol)) {
            return this;
        }
        for (Player p : targetCol) {
            targets.put(p, 1);
        }
        return this;
    }
}
