package top.rwocj.wx.pay.vehicle.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import top.rwocj.wx.pay.vehicle.enums.TradeScene;

/**
 * @author lqb
 * @since 2024/7/18 10:42
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserStateRequest extends AbstractRequest {

    /**
     * 交易场景
     * 委托代扣的交易场景值，目前支持 ：
     * 1. PARKING：车场停车场景 ；
     * 2. PARKING SPACE 车位停车场景；
     * 3. GAS 加油场景；
     * 4. HIGHWAY 高速场景；
     * 该值会向微信用户进行展示
     *
     * @see top.rwocj.wx.pay.vehicle.enums.TradeScene
     */
    @JacksonXmlProperty(localName = "trade_scene")
    @JacksonXmlCData
    private String tradeScene;

    /**
     * 跳转场景
     * 商户跳转的业务场景，不传默认是小程序，也支持APP和公众号H5跳转
     * APP:通过APP跳转
     * H5:通过公众号H5跳转
     *
     * @see top.rwocj.wx.pay.vehicle.enums.JumpScene
     */
    @JacksonXmlProperty(localName = "jump_scene")
    @JacksonXmlCData
    private String jumpScene;

    /**
     * openid
     * 用户在商户appid下的唯一标识
     */
    @JacksonXmlProperty(localName = "openid")
    @JacksonXmlCData
    private String openId;

//    /**
//     * 用户在子商户appid下的唯一标识。
//     */
//    @JacksonXmlProperty(localName = "sub_openid")
//    @JacksonXmlCData
//    private String subOpenId;

    /**
     * 车牌号
     * 车牌号。仅包括省份+车牌，不包括特殊字符。
     */
    @JacksonXmlProperty(localName = "plate_number")
    @JacksonXmlCData
    private String plateNumber;

    /**
     * 版本号
     * 3.0：车场停车（PARKING）场景
     * 2.0：其他场景
     */
    @JacksonXmlProperty(localName = "version")
    @JacksonXmlCData
    private String version = VERSION_3;

    /**
     * @param openId 用户在商户appid下的唯一标识
     */
    public static UserStateRequest highwayRequestWithOpenId(String openId) {
        UserStateRequest userStateRequest = highwayRequest();
        userStateRequest.setOpenId(openId);
        return userStateRequest;
    }

    /**
     * @param plateNumber 车牌号
     */
    public static UserStateRequest highwayRequestWithPlateNumber(String plateNumber) {
        UserStateRequest userStateRequest = highwayRequest();
        userStateRequest.setPlateNumber(plateNumber);
        return userStateRequest;
    }

    /**
     * 适用于高速场景
     */
    public static UserStateRequest highwayRequest() {
        UserStateRequest userStateRequest = new UserStateRequest();
        userStateRequest.setTradeScene(TradeScene.HIGHWAY.name());
        userStateRequest.setVersion(VERSION_2);
        return userStateRequest;
    }

}
