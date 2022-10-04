package com.example.logging.formatter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Map;

public class AuditLogFormatter {

    public static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"));
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES,true);
        mapper.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING,true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        mapper.configure(DeserializationFeature.USE_LONG_FOR_INTS,true);
        mapper.registerModule(new JavaTimeModule());
    }
    public static String toJsonString(Object object) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }

    public static <T> T toObject(String content, Class<T> valueType) throws JsonParseException, JsonMappingException, IOException {
        return mapper.readValue(content, valueType);
    }

    public static <T> T toObject(Object content, Class<T> valueType) throws JsonParseException, JsonMappingException, IOException {
        return mapper.convertValue(content, valueType);
    }

    public static <T> T toObject(Map<String, Object> map, Class<T> valueType) {

        return mapper.convertValue(map, valueType);
    }

}
