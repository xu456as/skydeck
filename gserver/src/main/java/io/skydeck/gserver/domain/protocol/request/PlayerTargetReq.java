package io.skydeck.gserver.domain.protocol.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class PlayerTargetReq {
    private int userId;
    private List<Integer> targetIds;

    @Data
    @Builder
    public static class Info {

        private int minSelectCount;
        private int maxSelectCount;
        private List<Integer> optionList;

        public static Info single(List<Integer> optionList) {
            return Info.builder().minSelectCount(1).maxSelectCount(1).optionList(optionList).build();
        }
        public static Info multiple(List<Integer> optionList) {
            return Info.builder().minSelectCount(optionList.size()).maxSelectCount(optionList.size()).optionList(optionList).build();
        }

    }
}
