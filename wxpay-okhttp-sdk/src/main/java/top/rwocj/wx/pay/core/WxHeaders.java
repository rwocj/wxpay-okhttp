package top.rwocj.wx.pay.core;

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
     *
     * @return response header 中 Wechatpay-Serial的值
     */
    String getWechatpaySerial();

    /**
     * @return response header 中 Wechatpay-Signature的值
     */
    String getWechatpaySignature();

    /**
     * 微信通知请求header没有Request-ID，返回null会导致验签失败，故默认返回空字符串
     * 其他实现如果不覆盖该方法影响也不大，只是验签时错误日志没有记录request_id
     *
     * @return response header 中Request-ID的值
     */
    default String getRequestID() {
        return "";
    }

    /**
     * @return response header 中 Wechatpay-Timestamp的值
     */
    String getWechatpayTimestamp();

    /**
     * @return response header 中 Wechatpay-Nonce的值
     */
    String getWechatpayNonce();
}
