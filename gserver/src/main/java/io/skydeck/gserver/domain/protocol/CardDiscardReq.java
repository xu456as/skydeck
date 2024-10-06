package io.skydeck.gserver.domain.protocol;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class CardDiscardReq {
    private Integer offenderId;
    private Integer defenderId;
    private List<Integer> cardIdList;

    @Data
    @Builder
    public static class DiscardInfo {
        private int discardCount;
    }
}
