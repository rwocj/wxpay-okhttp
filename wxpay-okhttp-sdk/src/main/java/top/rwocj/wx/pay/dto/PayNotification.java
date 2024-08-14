package top.rwocj.wx.pay.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 微信支付回调通知
 *
 * @author lqb
 * @since 2024/7/8 15:00
 **/
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayNotification {

    /**
     * 通知的唯一ID。
     */
    @JsonProperty("id")
    private String id;
    /**
     * 通知创建的时间，遵循rfc3339标准格式，格式为yyyy-MM-DDTHH:mm:ss+TIMEZONE，yyyy-MM-DD表示年月日，T出现在字符串中，表示time元素的开头，HH:mm:ss.表示时分秒，TIMEZONE表示时区（+08:00表示东八区时间，领先UTC 8小时，即北京时间）。例如：2015-05-20T13:29:35+08:00表示北京时间2015年05月20日13点29分35秒。
     */
    @JsonProperty("create_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'+'08:00", timezone = "Asia/Shanghai")
    private Date createTime;
    /**
     * 通知的资源数据类型，支付成功通知为encrypt-resource。
     */
    @JsonProperty("resource_type")
    private String resourceType;
    /**
     * 通知的类型，支付成功通知的类型为TRANSACTION.SUCCESS。
     */
    @JsonProperty("event_type")
    private String eventType;
    /**
     * 回调摘要。
     */
    @JsonProperty("summary")
    private String summary;
    /**
     * 通知资源数据。
     */
    @JsonProperty("resource")
    private ResourceDTO resource;

    @NoArgsConstructor
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ResourceDTO {
        /**
         * 对开启结果数据进行加密的加密算法，目前只支持AEAD_AES_256_GCM。
         */
        @JsonProperty("original_type")
        private String originalType;
        /**
         * Base64编码后的开启/停用结果数据密文。
         */
        @JsonProperty("algorithm")
        private String algorithm;
        /**
         * 附加数据。
         */
        @JsonProperty("ciphertext")
        private String ciphertext;
        /**
         * 原始回调类型，为transaction。
         */
        @JsonProperty("associated_data")
        private String associatedData;
        /**
         * 加密使用的随机串。
         */
        @JsonProperty("nonce")
        private String nonce;
    }
}
