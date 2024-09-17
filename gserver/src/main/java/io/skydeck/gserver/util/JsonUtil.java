package io.skydeck.gserver.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;

public class JsonUtil {
    public static ObjectMapper INST = new ObjectMapper();
    static {
        INST.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        INST.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }
}
