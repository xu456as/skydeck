package io.skydeck.gserver.socketio.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InputDTO {
    private InputContextDTO inputContext;
    private String data;
}
