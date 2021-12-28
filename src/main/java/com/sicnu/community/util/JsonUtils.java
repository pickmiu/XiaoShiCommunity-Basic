package com.sicnu.community.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Tangliyi (2238192070@qq.com)
 */
@Slf4j
public class JsonUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * 序列化，将对象转化为json字符串
     * 
     * @param data
     * @return
     */
    public static String toJsonString(Object data) {
        if (data == null) {
            return null;
        }

        String json = null;
        try {
            json = mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            log.error("[{}] toJsonString error：{{}}", data.getClass().getSimpleName(), e);
        }
        return json;
    }

    /**
     * 反序列化，将json字符串转化为对象
     * 
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T parse(@NonNull String json, Class<T> clazz) {
        T t = null;
        try {
            t = mapper.readValue(json, clazz);
        } catch (Exception e) {
            log.error(" parse json [{}] to class [{}] error：{{}}", json, clazz.getSimpleName(), e);
        }
        return t;
    }

    public static <T> T parse(@NonNull String json, TypeReference<T> valueTypeRef) {
        T t = null;
        try {
            t = mapper.readValue(json, valueTypeRef);
        } catch (Exception e) {
            log.error(" parse json [{}] to class [{}] error：{{}}", json, valueTypeRef.getType(), e);
        }
        return t;
    }

    public static <T> T parse(@NonNull String json, JavaType valueType) {
        T t = null;
        try {
            t = mapper.readValue(json, valueType);
        } catch (Exception e) {
            log.error(" parse json [{}] to class [{}] error：{{}}", json, valueType, e);
        }
        return t;
    }

}
