package top.rwocj.wx.pay.vehicle.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * 对jackson进行一些常用操作的封闭，如xml字符串转实体，转List，序列化实体为字符串等，
 * 具体可参见方法注释
 */
@Slf4j
public class XmlUtil {

    @Getter
    private static final XmlMapper xmlMapper = new XmlMapper();

    protected XmlUtil() {

    }

    /**
     * 将字符串转对应的实体
     *
     * @param xml    xml
     * @param tClass 实体类型
     * @return 转换后的实体
     */
    public static <T> T parseObject(String xml, Class<T> tClass) {
        if (StringUtils.isBlank(xml)) {
            return null;
        }
        try {
            return xmlMapper.readValue(xml, tClass);
        } catch (IOException e) {
            log.error("xml：{}转实体：{}失败", xml, tClass.getSimpleName(), e);
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 将字符串转对应的实体列表
     *
     * @param xml    xml
     * @param tClass 实体类型
     * @return 转换后的实体列表
     */
    public static <T> List<T> parseList(String xml, Class<T> tClass) {
        if (StringUtils.isBlank(xml)) {
            return Collections.emptyList();
        }
        try {
            return xmlMapper.readValue(xml, xmlMapper.getTypeFactory().constructParametricType(List.class,
                    tClass));
        } catch (IOException e) {
            log.error("xml：{}转实体：{}失败", xml, tClass.getSimpleName(), e);
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 序列化实体为xml字符串
     *
     * @param o 被序列化的实体
     * @return 序列化后的xml字符串
     */
    public static String toString(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof String) {
            return (String) o;
        }
        try {
            return xmlMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            log.error("序列化实体：{}失败", o.getClass().getSimpleName(), e);
            throw new IllegalArgumentException(e);
        }
    }
}
