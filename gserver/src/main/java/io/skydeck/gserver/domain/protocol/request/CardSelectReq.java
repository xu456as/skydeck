package io.skydeck.gserver.domain.protocol.request;

import io.skydeck.gserver.domain.card.CardBase;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class CardSelectReq {
    private int userId;
    private List<Integer> cardIdList;

    @Data
    @Builder
    public static class Info {

        private int minSelectCount;
        private int maxSelectCount;
        private List<CardBase> optionList;

        public static Info single(List<CardBase> optionList) {
            return Info.builder().minSelectCount(1).maxSelectCount(1).optionList(optionList).build();
        }
        public static Info multiple(List<CardBase> optionList) {
            return Info.builder().minSelectCount(optionList.size()).maxSelectCount(optionList.size()).optionList(optionList).build();
        }

    }
}
