package io.skydeck.gserver.socketio;

import io.skydeck.gserver.engine.GameEngine;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RoomManager {

    private volatile Map<String, String> user2RoomMap = new HashMap<>();
    private ConcurrentHashMap<String, Room> roomMap = new ConcurrentHashMap<>();

    private synchronized void bindUser(String roomId, String userId) {
        if (user2RoomMap.containsKey(userId)) {
            throw new RuntimeException("user[%s] has joined room[%s], can't join room[%s]"
                    .formatted(userId, user2RoomMap.get(userId), roomId));
        }
        if (!roomMap.containsKey(roomId)) {
            throw new RuntimeException("user[%s] has joined room[%s] because it's not found"
                    .formatted(userId, roomId));
        }
        user2RoomMap.put(userId, roomId);
        roomMap.get(roomId).putUser(userId);
    }
    public synchronized Pair<Boolean, Room> unbindUser(String userId) {
        if (!user2RoomMap.containsKey(userId)) {
            throw new RuntimeException("user[%s] didn't join any room"
                    .formatted(userId));
        }
        String roomId = user2RoomMap.get(userId);
        Room room = roomMap.get(roomId);
        if (room == null) {
            return Pair.of(false, null);
        }
        room.getUsers().remove(userId);
        return Pair.of(StringUtils.equals(room.getCreator(), userId), room);
    }

    public synchronized Room createRoom(String roomId, String userId) {
        if (roomMap.containsKey(roomId)) {
            return roomMap.get(roomId);
        }
        Room room = new Room(userId, roomId);
        roomMap.put(roomId, room);
        return room;
    }
    public synchronized void destroyRoom(String roomId, String userId) {
        if (!roomMap.containsKey(roomId)) {
            return;
        }
        Room room = roomMap.get(roomId);
        if (StringUtils.equals(room.getCreator(), userId)) {
            throw new RuntimeException(
                    "user[%s] can't destroy room[%s] because of creator[%s] mismatch".formatted(
                            userId, roomId, room.getCreator()));
        }
        roomMap.remove(roomId);
        Set<String> users = room.getUsers().keySet();
        for (String user : users) {
            user2RoomMap.remove(user);
        }
    }
    public boolean roomExist(String roomId) {
        return roomMap.contains(roomId);
    }
    public GameEngine getEngineByRoom(String roomId) {
        Room room = roomMap.get(roomId);
        if (room != null) {
            return room.getEngine();
        }
        return null;
    }
    public String getRoomCreator(String roomId) {
        Room room = roomMap.get(roomId);
        if (room != null) {
            return room.getCreator();
        }
        return null;
    }
    public Room getRoom(String roomId) {
        return roomMap.get(roomId);
    }
    public String getRoomByEngine(String engineId) {
        return engineId;
    }


}
