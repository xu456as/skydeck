package io.skydeck.gserver.socketio.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class InputContextDTO {
    private Long inputId;
    private Integer playerId;

}
