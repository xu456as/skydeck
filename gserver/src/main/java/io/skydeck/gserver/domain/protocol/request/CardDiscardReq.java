package io.skydeck.gserver.domain.protocol.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class CardDiscardReq {
    private Integer userId;
    private Integer targetId;
    private List<Integer> cardIdList;

    @Data
    @Builder
    public static class Info {
        private int discardCount;
    }
}
