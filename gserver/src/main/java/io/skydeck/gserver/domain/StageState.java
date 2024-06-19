package io.skydeck.gserver.domain;

import io.skydeck.gserver.annotation.DValue;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class StageState {
    @DValue("false")
    private Boolean healthLocked;
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
    private Integer reframeCardCount;
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
    @DValue("false")
    private Boolean skipNextDrawPhase;
    @DValue("false")
    private Boolean skipNextActivePhase;

    private static final Map<String, Field> FIELD_MAP = buildFieldMap();
    private static Map<String, Field> buildFieldMap() {
        Field[] fields = StageState.class.getDeclaredFields();
        Map<String, Field> fieldMap = new HashMap<>();
        for (Field field : fields) {
            DValue dValue = field.getAnnotation(DValue.class);
            if (dValue == null) {
                continue;
            }
            field.setAccessible(true);
            fieldMap.put(field.getName(), field);
        }
        return fieldMap;
    }

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
    public int incCount(String key) throws Exception {
        return incCount(key, 1);
    }
    public int incCount(String key, int count) throws Exception {
        Field field = FIELD_MAP.get(key);
        if (field == null || !Integer.class.equals(field.getType())) {
            return -1;
        }
        int intVal = field.getInt(this);
        field.set(this, intVal + count);
        return intVal;
    }
}
