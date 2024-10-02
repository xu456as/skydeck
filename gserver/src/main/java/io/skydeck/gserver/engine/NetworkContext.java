package io.skydeck.gserver.engine;

import io.skydeck.gserver.domain.player.Player;
import io.skydeck.gserver.enums.NCInputType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NetworkContext {
    private GameEngine gameEngine;
    private long inputId;
    private Player player;
    private NCInputType inputType;
}
