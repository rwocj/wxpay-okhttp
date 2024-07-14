package top.rwocj.wx.pay.core;

import jakarta.servlet.http.HttpServletRequest;

public class JakartaHttpServletRequestWxHeaders implements WxHeaders {

    private final HttpServletRequest request;

    public JakartaHttpServletRequestWxHeaders(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public String getWechatpaySerial() {
        return request.getHeader(WxHeaders.WECHATPAY_SERIAL);
    }

    @Override
    public String getWechatpaySignature() {
        return request.getHeader(WxHeaders.WECHATPAY_SIGNATURE);
    }

    @Override
    public String getWechatpayTimestamp() {
        return request.getHeader(WxHeaders.WECHATPAY_TIMESTAMP);
    }

    @Override
    public String getWechatpayNonce() {
        return request.getHeader(WxHeaders.WECHATPAY_NONCE);
    }
}
