package io.skydeck.gserver.domain.protocol.request;

import lombok.Data;

import java.util.Set;

@Data
public class CardUseReq {
    private int userId;
    private int cardId;
    private Set<Integer> targets;
    private Object extraInfo;
}
