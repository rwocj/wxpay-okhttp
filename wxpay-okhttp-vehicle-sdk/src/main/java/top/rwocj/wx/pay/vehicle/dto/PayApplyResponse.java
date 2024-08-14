package top.rwocj.wx.pay.vehicle.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 申请扣款响应
 *
 * @author lqb
 * @since 2024/7/18 16:31
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PayApplyResponse extends AbstractResponse {

    /**
     * 设备号
     * 终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB"
     */
    @JacksonXmlProperty(localName = "device_info")
    @JacksonXmlCData
    private String deviceInfo;

}
