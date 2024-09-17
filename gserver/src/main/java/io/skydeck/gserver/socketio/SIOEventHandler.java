package io.skydeck.gserver.socketio;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import io.skydeck.gserver.socketio.dto.InputContextDTO;
import io.skydeck.gserver.socketio.dto.InputDTO;
import io.skydeck.gserver.socketio.dto.UserJoinedDTO;
import io.skydeck.gserver.socketio.dto.UserLeftDTO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import static io.skydeck.gserver.socketio.enums.SIOEventType.*;

@Component
@Slf4j
public class SIOEventHandler {
    @Resource
    private SocketIOServer socketIOServer;

//    private Map<String, String> user2RoomMap = new HashMap<>();
//    private Map<String, List<String>> room2UserListMap = new HashMap<>();
    private final Object mutex = new Object();

    @OnConnect
    public void onConnect(SocketIOClient client) {
        // TODO Auto-generated method stub
        String sa = client.getRemoteAddress().toString();
        String clientIp = sa.substring(1, sa.indexOf(":")); // deviceIp
        log.info("client[{}] connected", clientIp);
        Map<String, List<String>> params = client.getHandshakeData().getUrlParams();
        String roomId = getHandshakeParam(client, "roomId");
        String userId = getHandshakeParam(client, "userId");
        if (StringUtils.isBlank(roomId) || StringUtils.isBlank(userId)) {
            client.sendEvent(ConnectError.name(), "", "no roomId or no userId");
            client.disconnect();
            return;
        }
        synchronized (mutex) {
            if (!userJoinRoom(client, userId, roomId)) {
                client.sendEvent(ConnectError.name(), "", "user can't join this room");
                client.disconnect();
                return;
            }
        }
        UserJoinedDTO dto = UserJoinedDTO.builder()
                .userId(userId)
                .roomId(roomId)
                .restUsers(user2RoomMap).build();
        client.sendEvent(ConnectOk.name(), dto);
    }
    private boolean userJoinRoom(SocketIOClient client, String userId, String roomId) {
        if (!user2RoomMap.containsKey(userId)) {
            user2RoomMap.put(userId, roomId);
            room2UserListMap.putIfAbsent(roomId, new ArrayList<>());
            room2UserListMap.get(roomId).add(userId);
            client.joinRoom(roomId);
            UserJoinedDTO dto = UserJoinedDTO.builder()
                    .userId(userId)
                    .roomId(roomId)
                    .restUsers(user2RoomMap).build();
            socketIOServer.getRoomOperations(roomId).sendEvent(UserJoined.name(),
                    dto);
            return true;
        }
        String oldRoomId = user2RoomMap.get(userId);
        if (!oldRoomId.equals(roomId)) {
            log.warn("user[{}] is in room[{}], can't join another room[{}]", userId, oldRoomId, roomId);
            return false;
        }
        return true;
    }
    private void userLeaveRoom(String userId) {
        String roomId = user2RoomMap.get(userId);
        if (StringUtils.isBlank(roomId)) {
            return;
        }
        List<String> userList = room2UserListMap.get(roomId);
        if (CollectionUtils.isNotEmpty(userList)) {
            userList.remove(userId);
            if (userList.isEmpty()) {
                room2UserListMap.remove(roomId);
            }
        }
        user2RoomMap.remove(userId);

    }
    private String getHandshakeParam(SocketIOClient client, String key) {
        Map<String, List<String>> params = client.getHandshakeData().getUrlParams();
        return Optional.ofNullable(params.get(key)).orElse(Collections.emptyList()).stream()
                .findFirst()
                .orElse(null);
    }


    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        // TODO Auto-generated method stub
        String sa = client.getRemoteAddress().toString();
        String clientIp = sa.substring(1, sa.indexOf(":"));// 获取设备ip
        log.info("client[{}] disconnected", clientIp);
    }
    @OnEvent(value = "LeaveRoom")
    public void onLeaveRoom(SocketIOClient client, AckRequest ackRequest, String data) {
        String roomId = getHandshakeParam(client, "roomId");
        String userId = getHandshakeParam(client, "userId");
        if (StringUtils.isBlank(roomId) || StringUtils.isBlank(userId)) {
            client.sendEvent(ConnectError.name(), "", "no roomId or no userId");
            client.disconnect();
            return;
        }
        synchronized (mutex) {
            userLeaveRoom(userId);
            UserJoinedDTO dto = UserJoinedDTO.builder()
                    .userId(userId)
                    .roomId(roomId)
                    .restUsers(user2RoomMap).build();
            socketIOServer.getRoomOperations(roomId).sendEvent(UserLeft.name(), dto);
        }
        client.disconnect();
    }
    @OnEvent(value = "BizInput")
    public void onBizInput(SocketIOClient client, AckRequest ackRequest, InputContextDTO context, String data) {
        String roomId = getHandshakeParam(client, "roomId");
        String userId = getHandshakeParam(client, "userId");
        if (StringUtils.isBlank(roomId) || StringUtils.isBlank(userId)) {
            client.sendEvent(ConnectError.name(), "", "no roomId or no userId");
            client.disconnect();
            return;
        }

        InputDTO dto = InputDTO.builder()
                .inputContext(context)
                .data(data)
                .build();
        inputQueue.offer(dto);
//        System.out.println(context);
//        System.out.println(data);
    }

    @OnEvent(value = "test")
    public void onEvent(SocketIOClient client, AckRequest ackRequest, String data) {
        // 客户端推送advert_info事件时，onData接受数据，这里是string类型的json数据，还可以为Byte[],object其他类型
        String sa = client.getRemoteAddress().toString();
        String clientIp = sa.substring(1, sa.indexOf(":"));// 获取客户端连接的ip
        Map<String, List<String>> params = client.getHandshakeData().getUrlParams();// 获取客户端url参数
        System.out.println(clientIp + "：客户端：************" + data);
//        JSONObject gpsData = (JSONObject) JSONObject.parse(data);
//        String userIds = gpsData.get("userName") + "";
//        String taskIds = gpsData.get("password") + "";
        client.sendEvent("text1", "后台得到了数据");
    }

    private LinkedBlockingQueue<InputDTO> inputQueue = new LinkedBlockingQueue<>();
    public Object awaitInput() {
        //todo
        return null;
    }
}
