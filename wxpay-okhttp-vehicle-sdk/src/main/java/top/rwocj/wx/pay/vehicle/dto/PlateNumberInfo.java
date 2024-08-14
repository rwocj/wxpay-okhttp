package top.rwocj.wx.pay.vehicle.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author lqb
 * @since 2024/7/18 14:16
 **/
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlateNumberInfo {

    /**
     * 车牌号。仅包括省份+车牌，不包括特殊字符。
     */
    @JsonProperty("plate_number")
    private String plateNumber;
    /**
     * 车牌通道信息标识，仅在HIGHWAY高速场景下进行返回。可选：MTC，ETC，ETC;MTC。同时具有2个标识时，以分号间隔标识
     */
    @JsonProperty("channel_type")
    private String channelType;
    /**
     * 在3.0接口时，如果该车牌是免密支付方式且是常用车牌则会返回该字段，对返回该字段的用户，商户可以引导用户开通微信支付分停车。
     * 特别提示：原引导升级无感支付路径已不再支持，可引导常用车牌用户开通微信支付分停车，具体请求参数和方式见：微信支付分停车服务产品介绍文档
     */
    @JsonProperty("common_use_flag")
    private String commonUseFlag;

    @JsonIgnore
    public boolean isCommonUse() {
        return "COMMON_USE".equals(commonUseFlag);
    }
}
