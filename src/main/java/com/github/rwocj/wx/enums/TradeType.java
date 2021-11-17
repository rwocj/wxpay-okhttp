package com.github.rwocj.wx.enums;

public enum TradeType {

    JSAPI("公众号支付"),
    NATIVE("扫码支付"),
    APP("APP支付"),
    MICROPAY("付款码支付"),
    MWEB("H5支付"),
    FACEPAY("刷脸支付");

    private final String value;

    TradeType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
