package io.skydeck.gserver.socketio.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class UserJoinedDTO {
    private String userId;
    private String roomId;
    private List<String> restUsers;
}
