package io.skydeck.gserver.domain.protocol.request;

import lombok.Data;

import java.util.Set;

@Data
public class CardSacrificeReq {
    private int userId;
    private int cardId;
}
