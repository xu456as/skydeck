package io.skydeck.gserver.domain;

import io.skydeck.gserver.annotation.DValue;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.List;

@Data
public class StageState {
    @DValue("false")
    private Boolean inLimbo;
    @DValue("1")
    private Integer slashQuota;
    @DValue("0")
    private Integer extraSlashTarget;
    @DValue("1")
    private Integer liquorQuota;
    @DValue("0")
    private Integer extraHandQuota;
    @DValue("false")
    private Boolean gainCardFromDeck;
    @DValue("false")
    private Boolean causeInDanger;
    @DValue("0")
    private Integer lostCardCount;
    @DValue("0")
    private Integer killCount;
    @DValue("0")
    private Integer useCardCount;
    @DValue("0")
    private Integer sacrificeCardCount;
    @DValue("0")
    private Integer discardCardCount;
    @DValue("0")
    private Integer useSlashCount;
    @DValue("0")
    private Integer useLiquorCount;
    @DValue("false")
    private Boolean drunk;
    @DValue("0")
    private Integer damageTakenCount;
    @DValue("0")
    private Integer damageDealtCount;
    @DValue("false")
    private Boolean primaryHeroSilent;
    @DValue("false")
    private Boolean viceHeroSilent;
    @DValue("false")
    private Boolean playerSilent;
    @DValue("")
    private String cardDisabled;
    @DValue("false")
    private Boolean recoverDisabled;
    @DValue("0")
    private Integer handGiftCount;

    public void resetDefault() throws Exception {
        Field[] fields = StageState.class.getDeclaredFields();
        for (Field field : fields) {
            DValue dValue = field.getAnnotation(DValue.class);
            if (dValue == null) {
                continue;
            }
            String val = dValue.value();
            Class type = field.getType();
            if (Integer.class.equals(type)) {
                Integer intVal = Integer.parseInt(val);
                field.set(this, intVal);
            } else if (Boolean.class.equals(type)) {
                Boolean boolVal = Boolean.parseBoolean(val);
                field.set(this, boolVal);
            } else if (String.class.equals(type)) {
                field.set(this, val);
            }
        }
    }
}
