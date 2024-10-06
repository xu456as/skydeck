package io.skydeck.gserver.socketio.dto;

import io.skydeck.gserver.enums.NetworkFeedbackType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FeedbackContextDTO {
    private Long inputId;
    private Integer playerId;
    private NetworkFeedbackType feedbackType;
    private String message;
}
