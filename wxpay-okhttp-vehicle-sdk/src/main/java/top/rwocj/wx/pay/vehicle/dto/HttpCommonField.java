package top.rwocj.wx.pay.vehicle.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * 请求和响应公共字段，作为响应时，只有当通信成功时才会返回这些字段
 *
 * @author lqb
 * @since 2024/7/18 13:59
 **/
@Data
public abstract class HttpCommonField {

    /**
     * 公众账号id
     * 服务商商户号绑定的APPID
     */
    @JacksonXmlProperty(localName = "appid")
    @JsonProperty("appid")
    @JacksonXmlCData
    private String appId;

    /**
     * 商户号
     * 服务商商户号
     */
    @JacksonXmlProperty(localName = "mch_id")
    @JsonProperty("mch_id")
    @JacksonXmlCData
    private String mchId;

    /**
     * 随机串
     * 随机字符串，不长于32位。
     */
    @JacksonXmlProperty(localName = "nonce_str")
    @JsonProperty("nonce_str")
    @JacksonXmlCData
    private String nonceStr;

    /**
     * 签名类型
     * 签名类型，默认为HMAC-SHA256
     */
    @JacksonXmlProperty(localName = "sign_type")
    @JsonProperty("sign_type")
    @JacksonXmlCData
    private final String signType = "HMAC-SHA256";

    /**
     * 签名
     */
    @JacksonXmlProperty(localName = "sign")
    @JsonProperty("sign")
    @JacksonXmlCData
    private String sign;
}
