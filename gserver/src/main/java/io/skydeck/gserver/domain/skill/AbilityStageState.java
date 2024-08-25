package io.skydeck.gserver.domain.skill;

import io.skydeck.gserver.annotation.DValue;
import io.skydeck.gserver.domain.BaseStageState;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AbilityStageState extends BaseStageState {
    @DValue("0")
    private Integer useCount;

    static {
        buildFieldMap(AbilityStageState.class);
    }

}