package top.rwocj.wx.base;

import javax.servlet.http.HttpServletRequest;

public class HttpServletRequestWxHeaders implements WxHeaders {

    private final HttpServletRequest request;

    public HttpServletRequestWxHeaders(HttpServletRequest request) {
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
