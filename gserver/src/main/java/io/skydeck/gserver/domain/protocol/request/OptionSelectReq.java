package io.skydeck.gserver.domain.protocol.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class OptionSelectReq {
    private int userId;
    private List<Integer> optIndices;

    @Data
    @Builder
    public static class Info {

        private int minSelectCount;
        private int maxSelectCount;
        private List<String> optionList;
        private int mask = 0XFFFFFFFF;

        public static Info single(List<String> optionList) {
            return Info.builder().minSelectCount(1).maxSelectCount(1).optionList(optionList).build();
        }
        public static Info multiple(List<String> optionList) {
            return Info.builder().minSelectCount(optionList.size()).maxSelectCount(optionList.size()).optionList(optionList).build();
        }

    }
}
