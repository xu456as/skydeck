package io.skydeck.gserver.socketio.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Builder
@Data
public class UserLeftDTO{
    private String userId;
    private String roomId;
    private Map<String, String> restUsers;
}
