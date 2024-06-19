package io.skydeck.gserver.domain.dto;

import io.skydeck.gserver.domain.Player;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class StealWeaponExtraInfo {
    private Map<Player, Player> slashTargetMap = new HashMap<>();
}
