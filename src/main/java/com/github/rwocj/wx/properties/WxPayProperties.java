package com.github.rwocj.wx.properties;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class WxPayProperties {
    /***
     * 微信支付商户号
     */
    @NotEmpty
    private String mchId;

    /**
     * 微信支付证书序列号
     */
    @NotEmpty
    private String certificateSerialNo;

    /**
     * v3接口密钥
     */
    @NotEmpty
    private String apiV3Key;

    /**
     * 微信支付私钥文件resource路径,如classpath:/cert/app.pem
     */
    @NotEmpty
    private String privateKeyPath;

    /**
     * 支付通知地址
     */
    @NotEmpty
    private String notifyUrl;

    /**
     * 退款通知地址
     */
    private String refundNotifyUrl;

}
