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

    /**
     * 微信通知请求header没有Request-ID，返回null会导致验签失败，故默认返回空字符串
     * 其他实现如果不覆盖该方法影响也不大，只是验签时错误日志没有记录request_id
     */
    default String getRequestID() {
        return "";
    }

    String getWechatpayTimestamp();

    String getWechatpayNonce();
}
