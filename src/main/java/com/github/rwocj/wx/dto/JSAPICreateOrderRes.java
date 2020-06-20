package com.github.rwocj.wx.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * jsapi下单响应体
 * https://developers.weixin.qq.com/miniprogram/dev/api/open-api/payment/wx.requestPayment.html
 * https://pay.weixin.qq.com/wiki/doc/apiv3/wxpay/pay/transactions/chapter3_8.shtml
 */
@Data
public class JSAPICreateOrderRes {

    private String appId;
    private String timeStamp;
    private String nonceStr;
    @JsonProperty("package")
    private String packageValue;
    private String signType;
    private String paySign;
}
