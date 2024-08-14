package top.rwocj.wx.pay.vehicle.property;

import lombok.Data;

@Data
public class WxPayVehicleProperties {

    /**
     * 公众账号id,服务商商户号绑定的APPID
     */
    private String appId;

//    /**
//     * 子商户公众账号id,子商户号绑定的服务号，小程序，APP的appid(需要在服务商的商户平台为子商户绑定)
//     */
//    private String subAppId;

    /**
     * 服务商商户号
     */
    private String mchId;

//    /**
//     * 子商户号
//     */
//    private String subMchId;

    /**
     * 接口密钥
     */
    private String apiKey;

    /**
     * 微信支付api证书路径,如classpath:/cert/app.p12
     */
    private String p12Path;

    /**
     * 申请扣款通知地址
     */
    private String payApplyNotifyUrl;

    /**
     * 申请退款通知地址
     */
    private String payRefundNotifyUrl;
}
