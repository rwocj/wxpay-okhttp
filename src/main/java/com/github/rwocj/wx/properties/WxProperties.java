package com.github.rwocj.wx.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ConfigurationProperties(prefix = "wx")
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
