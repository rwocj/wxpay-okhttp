package com.github.rwocj.wx.properties;

import lombok.Data;


@Data
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
}
