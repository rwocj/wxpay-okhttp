package com.github.rwocj.wx.base;

/**
 * 根据v3接口规则，获取请求求或响应体header相关信息
 */
public interface WxHeaders {

    String WECHATPAY_SERIAL = "Wechatpay-Serial";

    String WECHATPAY_SIGNATURE = "Wechatpay-Signature";

    String REQUEST_ID = "Request-ID";

    String WECHATPAY_TIMESTAMP = "Wechatpay-Timestamp";

    String WECHATPAY_NONCE = "Wechatpay-Nonce";

    /**
     * 获取 Wechatpay-Serial header
     */
    String getWechatpaySerial();

    String getWechatpaySignature();

    String getRequestID();

    String getWechatpayTimestamp();

    String getWechatpayNonce();
}
