package io.skydeck.gserver.engine;

import io.skydeck.gserver.domain.player.Player;
import lombok.Data;

@Data
public class NetworkContext {
    private GameEngine gameEngine;
    private long inputId;
    private Player player;
}
