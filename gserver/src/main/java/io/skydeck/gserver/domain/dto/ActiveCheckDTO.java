package io.skydeck.gserver.domain.dto;

import io.skydeck.gserver.domain.player.Player;
import io.skydeck.gserver.enums.CardDisposeType;
import io.skydeck.gserver.enums.CardNameType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActiveCheckDTO {
    private Player subject;
    private CardNameType cardQuery;
    private CardDisposeType cardDisposeType;


    public static ActiveCheckDTO subject(Player subject) {
        return ActiveCheckDTO.builder().subject(subject).build();
    }
    public static ActiveCheckDTO queryFor(Player subject, CardNameType cardQuery, CardDisposeType disposeType) {
        return ActiveCheckDTO.builder().subject(subject).cardQuery(cardQuery).cardDisposeType(disposeType).build();
    }
}
