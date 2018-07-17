package org.ileler.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletResponse;

/**
 * Author: kerwin612
 */
public final class JsonUtils {

    private static ObjectMapper objectMapper = new ObjectMapper();
    
    static{
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * 对象转JSON
     * 
     * @param value
     *            对象
     * @return
     */
    public static String toJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
        return null;
    }

    /**
     * 
     * @param response
     *            response
     * @param contentType
     *            contentType
     * @param value
     *            对象
     */
    public static void toJson(HttpServletResponse response, String contentType,
                              Object value) {
        Assert.hasText(contentType, "contentType string is null or empty");
        Assert.notNull(response, "response is null");
        try {
            response.setContentType(contentType);
            objectMapper.writeValue(response.getWriter(), value);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    /**
     * JSON转对象
     * 
     * @param json
     *            json
     * @param valueType
     *            类型
     * @param <T>
     *            类型
     * @return
     */
    public static <T> T toObject(String json, Class<T> valueType) {
        Assert.hasText(json, "json string is null or empty");
        Assert.notNull(valueType, "valueType is null");

        try {
            return objectMapper.readValue(json, valueType);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
        return null;
    }

    /**
     * JSON转对象
     * 
     * @param json
     *            json
     * @param typeReference
     *            类型
     * @param <T>
     *            类型
     * @return
     */
    public static <T> T toObject(String json, TypeReference<?> typeReference) {
        Assert.hasText(json, "json string is null or empty");
        Assert.notNull(typeReference, "typeReference is null");
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
        return null;
    }

    /**
     * JSON转对象
     * 
     * @param json
     *            json json
     * @param javaType
     *            类型
     * @param <T>
     *            类型
     * @return
     */
    public static <T> T toObject(String json, JavaType javaType) {
        Assert.hasText(json, "json string is null or empty");
        Assert.notNull(javaType, "javaType is null");
        try {
            return objectMapper.readValue(json, javaType);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
        return null;
    }
}
