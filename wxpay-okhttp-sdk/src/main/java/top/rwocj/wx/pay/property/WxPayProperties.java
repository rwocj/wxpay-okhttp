package top.rwocj.wx.pay.property;

import lombok.Data;
import top.rwocj.wx.pay.enums.OrderType;

import java.util.Map;

@Data
public class WxPayProperties {

    /**
     * 应用appId
     */
    private Map<OrderType, String> appIds;

    /**
     * 微信支付商户号
     */
    private String mchId;

    /**
     * v3接口密钥
     */
    private String apiV3Key;

    /**
     * 微信支付api证书路径,如classpath:/cert/app.p12
     */
    private String p12Path;

    /**
     * 支付通知地址
     */
    private String notifyUrl;

    /**
     * 退款通知地址
     */
    private String refundNotifyUrl;

}
