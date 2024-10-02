package io.skydeck.gserver.socketio;

import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.socketio.dto.InputDTO;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

@Data
public class Room {
    private String roomId;
    private String creator;

    private GameEngine engine;
    private final Map<String, Integer/*playerId*/> users = new HashMap<>();
    private final LinkedBlockingQueue<InputDTO> inputQueue = new LinkedBlockingQueue<>();


    public Room(String creatorId, String roomId, GameEngine e) {
        this.creator = creatorId;
        this.roomId = roomId;
        this.engine = e;
        this.engine.setId(roomId);
        putUser(creatorId);
    }

    public Room putUser(String userId) {
        users.put(userId, -1);
        return this;
    }

}
