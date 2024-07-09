package top.rwocj.wx.pay.properties;

import lombok.Data;
import org.springframework.core.io.Resource;

@Data
public class WxPayProperties {

    /**
     * 微信公众应用appId
     */
    private String appId;

    /**
     * 微信支付商户号
     */
    private String mchId;

    /**
     * 微信支付证书序列号
     */
    private String certificateSerialNo;

    /**
     * v3接口密钥
     */
    private String apiV3Key;

    /**
     * 微信支付私钥文件resource路径,如classpath:/cert/app.pem
     */
    private Resource privateKeyPath;

    /**
     * 支付通知地址
     */
    private String notifyUrl;

    /**
     * 退款通知地址
     */
    private String refundNotifyUrl;

}
