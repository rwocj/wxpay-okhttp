package com.github.rwocj.wx.base;

import okhttp3.Response;

public class OkHttpWxHeaders implements WxHeaders {

    private final Response response;

    public OkHttpWxHeaders(Response response) {
        this.response = response;
    }

    @Override
    public String getWechatpaySerial() {
        return response.header(WECHATPAY_SERIAL);
    }

    @Override
    public String getWechatpaySignature() {
        return response.header(WECHATPAY_SIGNATURE);
    }

    @Override
    public String getRequestID() {
        return response.header(REQUEST_ID);
    }

    @Override
    public String getWechatpayTimestamp() {
        return response.header(WECHATPAY_TIMESTAMP);
    }

    @Override
    public String getWechatpayNonce() {
        return response.header(WECHATPAY_NONCE);
    }
}
