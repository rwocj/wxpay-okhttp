package com.github.rwocj.wx.properties;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Validated
public class WxProperties {

    /**
     * 微信公众应用appId
     */
    @NotEmpty
    private String appId;

    @NotNull
    private WxPayProperties pay;
}
