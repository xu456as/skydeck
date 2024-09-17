package io.skydeck.gserver.socketio;

import com.corundumstudio.socketio.SocketIOServer;
import io.skydeck.gserver.engine.GameEngine;
import io.skydeck.gserver.engine.NetworkContext;
import io.skydeck.gserver.engine.NetworkInterface;
import io.skydeck.gserver.socketio.dto.InputContextDTO;
import io.skydeck.gserver.socketio.enums.SIOEventType;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingQueue;

@Component
public class SIONetworkAdapter implements NetworkInterface {
    @Resource
    private SocketIOServer socketIOServer;
    @Resource
    private SIOEventHandler eventHandler;
    @Resource
    private RoomManager roomManager;
    private LinkedBlockingQueue<NetworkContext> notifyQueue = new LinkedBlockingQueue<>();

    @Override
    public void notifyInput(NetworkContext nc) {
        if (nc == null) {
            return;
        }
        notifyQueue.offer(nc);
    }

    @Override
    public Object readInput(NetworkContext nc) {
        GameEngine e = nc.getGameEngine();
        String roomId = roomManager.getRoomByEngine(e.getId());
        if (StringUtils.isBlank(roomId)) {
            throw new RuntimeException("can't find a room by engine[%s]".formatted(e.getId()));
        }
        sendNotify(nc, roomId);
        return eventHandler.awaitInput();
    }

    private void sendNotify(NetworkContext nc, String roomId) {
        InputContextDTO dto = InputContextDTO.builder()
                .playerId(nc.getPlayer().getId())
                .inputId(nc.getInputId())
                .build();
        socketIOServer.getRoomOperations(roomId).sendEvent(SIOEventType.NotifyInput.name(), dto);
    }
}
