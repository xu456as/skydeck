package io.skydeck.gserver.socketio;

import io.skydeck.gserver.engine.GameEngine;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Room {
    private String roomId;
    private String creator;

    private GameEngine engine;
    private final Map<String, Integer/*playerId*/> users = new HashMap<>();


    public Room(String creatorId, String roomId) {
        this.creator = creatorId;
        this.roomId = roomId;
        this.engine = new GameEngine();
        this.engine.setId(roomId);
        putUser(creatorId);
    }

    public Room putUser(String userId) {
        users.put(userId, -1);
        return this;
    }

}
