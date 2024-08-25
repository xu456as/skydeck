package io.skydeck.gserver.domain;

import io.skydeck.gserver.annotation.DValue;
import io.skydeck.gserver.domain.player.PlayerStageState;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class BaseStageState {
    protected static Map<String, Field> FIELD_MAP;
    protected static void buildFieldMap(Class<?> tClass) {
        Field[] fields = tClass.getDeclaredFields();
        Map<String, Field> fieldMap = new HashMap<>();
        for (Field field : fields) {
            DValue dValue = field.getAnnotation(DValue.class);
            if (dValue == null) {
                continue;
            }
            field.setAccessible(true);
            fieldMap.put(field.getName(), field);
        }
        FIELD_MAP = fieldMap;
    }

    public void resetDefault() throws Exception {
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
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
        field.setAccessible(true);
        Integer val = (Integer)field.get(this);
//        int intVal = field.getInt(this);
        field.set(this, val + count);
        return val + count;
    }
}
