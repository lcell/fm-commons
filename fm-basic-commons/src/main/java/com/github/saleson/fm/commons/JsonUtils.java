package com.github.saleson.fm.commons;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author saleson
 * @date 2020-09-19 14:14
 */
@Slf4j
public class JsonUtils {


    private static ObjectMapper objectMapper = new ObjectMapper();
    static {
        objectMapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
    }

    public static String toJson(Object obj){
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.warn("transfer to json occur exception:{} - {}", e.getClass(), e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static <T> T parseJson(String json, Class<T> cls){
        try {
            return objectMapper.readValue(json, cls);
        } catch (JsonProcessingException e) {
            log.warn("parse json '{}' to {} occur exception:{} - {}", json, cls, e.getClass(), e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static <T> T parseJson(byte[] bytes, Class<T> cls){
        try {
            return objectMapper.readValue(bytes, cls);
        } catch (IOException e) {
            log.warn("parse json to {} occur exception:{} - {}", cls, e.getClass(), e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static <T> T parseJson(String json, TypeReference<T> typeRef){
        try {
            return objectMapper.readValue(json, typeRef);
        } catch (JsonProcessingException e) {
            log.warn("parse json '{}' to {} occur exception:{} - {}", json, typeRef.getType(), e.getClass(), e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static <T> T parseJson(byte[] bytes, TypeReference<T> typeRef){
        try {
            return objectMapper.readValue(bytes, typeRef);
        } catch (IOException e) {
            log.warn("parse json to {} occur exception:{} - {}", typeRef.getType(), e.getClass(), e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
