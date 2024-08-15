package top.rwocj.wx.pay.vehicle.utils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.rwocj.wx.pay.vehicle.annotation.IgnoreSign;
import top.rwocj.wx.pay.vehicle.dto.AbstractRequest;
import top.rwocj.wx.pay.vehicle.dto.AbstractResponse;
import top.rwocj.wx.pay.vehicle.dto.HttpCommonField;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author lqb
 * @since 2024/7/18 11:21
 **/
@UtilityClass
public class SignUtil {

    private static final Map<Class<?>, List<Field>> fieldMap = new ConcurrentHashMap<>();
    private static final Logger log = LoggerFactory.getLogger(SignUtil.class);

    /**
     * 生成签名
     *
     * @param obj    待签名对象
     * @param secret 签名密钥
     * @return 签名
     */
    public static <T extends AbstractRequest> String signRequest(T obj, String secret) {
        return sign(obj, secret, false);
    }

    /**
     * 生成签名
     *
     * @param obj    待签名对象
     * @param secret 签名密钥
     * @return 签名
     */
    public static <T extends AbstractResponse> String signResponse(T obj, String secret) {
        return sign(obj, secret, true);
    }

    /**
     * 生成签名
     *
     * @param obj    待签名对象
     * @param secret 签名密钥
     * @return 签名
     */
    public static <T extends HttpCommonField> String sign(T obj, String secret, boolean ignoreSignType) {
        if (obj == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        List<Field> fieldList = getAllFields(obj.getClass()).stream()
                .filter(field -> !field.isAnnotationPresent(IgnoreSign.class) && !Modifier.isStatic(field.getModifiers()))
                .sorted(Comparator.comparing(SignUtil::getKey)).collect(Collectors.toList());
        try {
            for (Field field : fieldList) {
                field.setAccessible(true);
                String key = getKey(field);
                if (ignoreSignType && "sign_type".equals(key)) {
                    continue;
                }
                Object o = field.get(obj);
                if (o instanceof Date) {
                    JsonFormat jsonFormat = field.getAnnotation(JsonFormat.class);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(jsonFormat != null && StringUtils.isNotBlank(jsonFormat.pattern()) ? jsonFormat.pattern() : "yyyy-MM-dd HH:mm:ss");
                    simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
                    o = simpleDateFormat.format(o);
                }
                if (o != null) {
                    sb.append(key).append("=").append(o).append("&");
                }
            }
            sb.append("key=").append(secret);
            String sign = hmacSHA256(sb.toString(), secret).toUpperCase();
            log.debug("待签名字符串：{},签名:{}", sb, sign);
            return sign;
        } catch (IllegalAccessException e) {
            throw new RuntimeException("生成签名失败", e);
        }
    }

    /**
     * 生成签名
     *
     * @param obj    待签名对象
     * @param secret 签名密钥
     * @return 签名
     */
    public static <T extends HttpCommonField> String sign(JsonNode obj, String secret, boolean ignoreSignType) {
        if (obj == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        Iterator<String> fieldNamesIterator = obj.fieldNames();
        List<String> fieldNames = new LinkedList<>();
        fieldNamesIterator.forEachRemaining(s -> {
            if (!Objects.equals(s, "sign")) {
                if (!(ignoreSignType && s.equals("sign_type"))) {
                    fieldNames.add(s);
                }
            }
        });
        List<String> sortedFieldNames = fieldNames.stream().sorted().collect(Collectors.toList());
        for (String key : sortedFieldNames) {
            JsonNode o = obj.get(key);
            if (o == null || o.isNull()) {
                continue;
            }
            String text = o.asText();
            sb.append(key).append("=").append(text).append("&");
        }
        sb.append("key=").append(secret);
        String sign = hmacSHA256(sb.toString(), secret).toUpperCase();
        log.debug("待签名字符串：{},签名:{}", sb, sign);
        return sign;
    }

    /**
     * hmacSHA256摘要
     *
     * @param data   数据
     * @param secret 密码
     * @return 摘要
     */
    public static String hmacSHA256(String data, String secret) {
        try {
            // Create HMAC-SHA256 key from the given secret
            SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(), "HmacSHA256");

            // Get an instance of Mac object implementing HMAC-SHA256
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(secretKeySpec);

            // Calculate the HMAC value
            byte[] hmacBytes = mac.doFinal(data.getBytes());

            // Convert result into a hexadecimal string
            StringBuilder sb = new StringBuilder(hmacBytes.length * 2);
            for (byte b : hmacBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to calculate HMAC-SHA256", e);
        }
    }

    private static List<Field> getAllFields(Class<?> t) {
        return fieldMap.computeIfAbsent(t, clazz -> _getAllFields(clazz, new LinkedList<>()));
    }

    private static List<Field> _getAllFields(Class<?> t, List<Field> fields) {
        fields.addAll(Arrays.asList(t.getDeclaredFields()));
        if (Object.class != t.getSuperclass()) {
            return _getAllFields(t.getSuperclass(), fields);
        }
        return fields;
    }

    private static String getKey(Field field) {
        JacksonXmlProperty annotation = field.getAnnotation(JacksonXmlProperty.class);
        JsonProperty annotation1 = field.getAnnotation(JsonProperty.class);
        return annotation == null ? (annotation1 == null ? field.getName() : annotation1.value()) : annotation.localName();
    }
}
