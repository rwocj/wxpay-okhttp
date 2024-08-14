package top.rwocj.wx.pay.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

/**
 * 对jackson进行一些常用操作的封闭，如json字符串转实体，转List，序列化实体为字符串等，
 * 具体可参见方法注释
 * <p>
 */
@Slf4j
public class JacksonUtil {

    @Getter
    private static final ObjectMapper objectMapper = new ObjectMapper().
            setTimeZone(TimeZone.getTimeZone("GMT+8"))
            .setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    private static final ObjectNode objectNode = objectMapper.createObjectNode();
    private static final ArrayNode arrayNode = objectNode.arrayNode();

    protected JacksonUtil() {

    }

    /**
     * 将字符串转对应的实体
     *
     * @param jsonStr json字符串
     * @param tClass  实体类型
     * @return 转换后的实体
     */
    public static <T> T parseObject(String jsonStr, Class<T> tClass) {
        if (StringUtils.isBlank(jsonStr)) {
            return null;
        }
        try {
            return objectMapper.readValue(jsonStr, tClass);
        } catch (IOException e) {
            log.error("json字符串：{}转实体：{}失败", jsonStr, tClass.getSimpleName(), e);
            throw new IllegalArgumentException(e);
        }
    }

    public static ObjectNode parseObject(String jsonStr) {
        if (StringUtils.isBlank(jsonStr)) {
            return objectNode;
        }
        try {
            return (ObjectNode) objectMapper.readTree(jsonStr);
        } catch (IOException e) {
            log.error("json字符串：{}转换失败", jsonStr, e);
            throw new IllegalArgumentException(e);
        }
    }

    public static ArrayNode parseArray(String jsonStr) {
        if (StringUtils.isBlank(jsonStr)) {
            return arrayNode;
        }
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonStr);
            return ((ArrayNode) jsonNode);
        } catch (IOException e) {
            log.error("json字符串：{}转array失败", jsonStr, e);
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 将字符串转对应的实体列表
     *
     * @param jsonStr json字符串
     * @param tClass  实体类型
     * @return 转换后的实体列表
     */
    public static <T> List<T> parseList(String jsonStr, Class<T> tClass) {
        if (StringUtils.isBlank(jsonStr)) {
            return Collections.emptyList();
        }
        try {
            return objectMapper.readValue(jsonStr, objectMapper.getTypeFactory().constructParametricType(List.class,
                    tClass));
        } catch (IOException e) {
            log.error("json字符串：{}转实体：{}失败", jsonStr, tClass.getSimpleName(), e);
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 序列化实体为json字符串
     *
     * @param o 被序列化的实体
     * @return 序列化后的json字符串
     */
    public static String toJsonString(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof String) {
            return (String) o;
        }
        try {
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            log.error("序列化实体：{}失败", o.getClass().getSimpleName(), e);
            throw new IllegalArgumentException(e);
        }
    }

    public static String getString(JsonNode jsonNode) {
        if (jsonNode == null || jsonNode.isNull()) {
            return null;
        }
        if (jsonNode.isTextual()) {
            return jsonNode.asText();
        }
        if (jsonNode.isArray() || jsonNode.isObject() || jsonNode.isPojo()) {
            return jsonNode.toString();
        }
        return jsonNode.asText();
    }

    public static Long getLong(JsonNode jsonNode) {
        if (jsonNode == null || jsonNode.isNull()) {
            return null;
        }
        return jsonNode.asLong();
    }

    public static Integer getInteger(JsonNode jsonNode) {
        if (jsonNode == null || jsonNode.isNull()) {
            return null;
        }
        return jsonNode.intValue();
    }

    public static Double getDouble(JsonNode jsonNode) {
        if (jsonNode == null || jsonNode.isNull()) {
            return null;
        }
        return jsonNode.asDouble();
    }

    public static Boolean getBoolean(JsonNode jsonNode) {
        if (jsonNode == null || jsonNode.isNull()) {
            return null;
        }
        return jsonNode.booleanValue();
    }

}
