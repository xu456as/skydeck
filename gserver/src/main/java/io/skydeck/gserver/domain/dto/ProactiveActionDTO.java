package io.skydeck.gserver.domain.dto;

import io.skydeck.gserver.domain.card.CardBase;
import io.skydeck.gserver.domain.card.GearCardBase;
import io.skydeck.gserver.domain.player.Player;
import io.skydeck.gserver.domain.settlement.SettlementBase;
import lombok.Data;

import java.util.List;

@Data
public class ProactiveActionDTO {
    private Player user;
    private List<CardBase> handSelected;
    private List<GearCardBase> equipSelected;
    private List<CardBase> judgeSelected;
    private List<Player> targets;

    private SettlementBase output;
    private String abilityTriggered;
}
