package top.rwocj.wx.core;

import jakarta.servlet.http.HttpServletRequest;

public class HttpJakartaServletRequestWxHeaders implements WxHeaders {

    private final HttpServletRequest request;

    public HttpJakartaServletRequestWxHeaders(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public String getWechatpaySerial() {
        return request.getHeader(WECHATPAY_SERIAL);
    }

    @Override
    public String getWechatpaySignature() {
        return request.getHeader(WECHATPAY_SIGNATURE);
    }

    @Override
    public String getWechatpayTimestamp() {
        return request.getHeader(WECHATPAY_TIMESTAMP);
    }

    @Override
    public String getWechatpayNonce() {
        return request.getHeader(WECHATPAY_NONCE);
    }
}
