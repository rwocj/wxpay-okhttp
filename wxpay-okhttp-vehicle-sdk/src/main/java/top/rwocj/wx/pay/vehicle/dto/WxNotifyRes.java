package top.rwocj.wx.pay.vehicle.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 微信通知响应,用于通知微信商户的处理结果
 *
 * @author lqb
 * @since 2024/7/22 14:17
 **/
@JacksonXmlRootElement(localName = "xml")
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class WxNotifyRes {

    @JacksonXmlProperty(localName = "return_code")
    private final String returnCode;
    @JacksonXmlProperty(localName = "return_msg")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final String returnMsg;

    public static WxNotifyRes fail(String returnMsg) {
        return new WxNotifyRes("FAIL", returnMsg);
    }

    public static WxNotifyRes success() {
        return new WxNotifyRes("SUCCESS", null);
    }
}
