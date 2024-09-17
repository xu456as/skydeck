package io.skydeck.gserver.socketio;

import io.skydeck.gserver.engine.GameEngine;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RoomManager {
    private ConcurrentHashMap<String, String> engine2RoomMap = new ConcurrentHashMap<>();
    private volatile Map<String, GameEngine> room2EngineMap = new HashMap<>();
    private volatile Map<String, String> room2CreatorMap = new HashMap<>();


    private volatile Map<String, String> user2PlayerMap = new HashMap<>();
    private volatile Map<String, String> player2UserMap = new HashMap<>();
    private ConcurrentHashMap<String, Room> roomMap = new ConcurrentHashMap<>();

    private synchronized void bindUser(String roomId, String userId) {

    }

    public synchronized GameEngine createRoom(String roomId, String userId) {
        if (room2EngineMap.containsKey(roomId)) {
            return room2EngineMap.get(roomId);
        }
        GameEngine e = new GameEngine();
        room2EngineMap.put(roomId, e);
        engine2RoomMap.put(e.getId(), roomId);
        room2CreatorMap.put(roomId, userId);
        return e;
    }
    public GameEngine getEngineByRoom(String roomId) {
        return room2EngineMap.get(roomId);
    }
    public String getRoomCreator(String roomId) {
        return room2CreatorMap.getOrDefault(roomId, "");
    }
    public String getRoomByEngine(String engineId) {
        return engine2RoomMap.getOrDefault(engineId, "");
    }
    public synchronized void destroyRoom(String roomId, String userId) {
        if (!room2EngineMap.containsKey(roomId)) {
            return;
        }
        if (!room2CreatorMap.getOrDefault(roomId, "").equals(userId)) {
            return;
        }
        GameEngine e = room2EngineMap.get(roomId);
        engine2RoomMap.remove(e.getId());
        room2EngineMap.remove(roomId);
        room2CreatorMap.remove(roomId);
    }

}
