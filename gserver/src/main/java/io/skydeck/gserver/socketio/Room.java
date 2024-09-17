package io.skydeck.gserver.socketio;

import io.skydeck.gserver.engine.GameEngine;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class Room {
    private String roomId;
    private String creator;

    private final Map<String, Integer/*playerId*/> users = new HashMap<>();

    private final GameEngine engine = new GameEngine();

    public Room putUser(String userId) {
        users.put(userId, -1);
        return this;
    }

}
