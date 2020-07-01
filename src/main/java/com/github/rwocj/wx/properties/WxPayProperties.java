package com.github.rwocj.wx.properties;

import lombok.Data;


@Data
public class WxPayProperties {
    /**
     * 微信支付商户号
     *
     * @required
     */
    private String mchId;

    /**
     * 微信支付证书序列号
     *
     * @required
     */
    private String certificateSerialNo;

    /**
     * v3接口密钥
     *
     * @required
     */
    private String apiV3Key;

    /**
     * 微信支付私钥文件resource路径,如classpath:/cert/app.pem
     *
     * @required
     */
    private String privateKeyPath;

    /**
     * 支付通知地址
     *
     * @required
     */
    private String notifyUrl;

    /**
     * 退款通知地址
     */
    private String refundNotifyUrl;

}
