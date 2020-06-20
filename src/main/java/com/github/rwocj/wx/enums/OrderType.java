package com.github.rwocj.wx.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OrderType {

    app("app", "app下单"), jsapi("jsapi", "jsapi下单"), nativeS("native", "native下单"), h5("h5", "h5下单");

    private final String url;

    private final String remark;
}
