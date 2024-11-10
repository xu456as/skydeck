package io.skydeck.gserver.domain.protocol.request;

import io.skydeck.gserver.domain.card.CardBase;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class AbilitySelectReq {
    private int userId;
    private List<Integer> abilityIdList;

    @Data
    @Builder
    public static class Info {

        private int minSelectCount;
        private int maxSelectCount;
        private List<CardBase> optionList;

        public static CardSelectReq.Info single(List<CardBase> optionList) {
            return CardSelectReq.Info.builder().minSelectCount(1).maxSelectCount(1).optionList(optionList).build();
        }
        public static CardSelectReq.Info multiple(List<CardBase> optionList) {
            return CardSelectReq.Info.builder().minSelectCount(optionList.size()).maxSelectCount(optionList.size()).optionList(optionList).build();
        }

    }
}
