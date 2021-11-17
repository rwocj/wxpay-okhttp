package com.github.rwocj.wx.properties;

public class WxProperties {

    /**
     * 微信公众应用appId
     *
     * @required
     */
    private String appId;

    /**
     * @required
     */
    private WxPayProperties pay;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public WxPayProperties getPay() {
        return pay;
    }

    public void setPay(WxPayProperties pay) {
        this.pay = pay;
    }
}
