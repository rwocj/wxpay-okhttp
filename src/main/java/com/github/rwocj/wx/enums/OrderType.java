package com.github.rwocj.wx.enums;

public enum OrderType {

    app("app", "app下单"), jsapi("jsapi", "jsapi下单"), nativeS("native", "native下单"), h5("h5", "h5下单");

    private final String url;

    private final String remark;

    OrderType(String url, String remark) {
        this.url = url;
        this.remark = remark;
    }

    public String getUrl() {
        return url;
    }

    public String getRemark() {
        return remark;
    }
}
