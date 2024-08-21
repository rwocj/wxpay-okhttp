package top.rwocj.wx.pay.vehicle.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import top.rwocj.wx.pay.vehicle.enums.HighwaySceneChannelType;
import top.rwocj.wx.pay.vehicle.enums.TradeScene;

/**
 * 用户授权/开通车主服务请求参数
 *
 * @author lqb
 * @since 2024/7/22 14:29
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserAuthorizationRequest extends AbstractRequest {

    /**
     * openid
     * 用户在商户appid下的唯一标识
     * 此参数必传，用户在商户appid下的唯一标识。openid和sub_openid可以选传其中之一，如果选择传sub_openid,则必须传sub_appid
     */
    @JacksonXmlProperty(localName = "openid")
    @JacksonXmlCData
    private String openId;

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
     * 车牌号
     * 车牌号。仅包括省份+车牌，不包括特殊字符。
     */
    @JacksonXmlProperty(localName = "plate_number")
    @JacksonXmlCData
    private String plateNumber;

    /**
     * 高速通道类型，目前可选：ETC、MTC。商户扣费前必须确认当前车牌的标识属性，用户车牌必须具有该通道标识时，才允许扣费。
     */
    @JacksonXmlProperty(localName = "channel_type")
    @JacksonXmlCData
    private String channelType;

    public UserAuthorizationRequest(String openId, String plateNumber, String tradeScene, String channelType) {
        this.openId = openId;
        this.plateNumber = plateNumber;
        this.tradeScene = tradeScene;
        this.channelType = channelType;
    }

    public static UserAuthorizationRequest highwayRequestWithOpenId(String openId, String plateNumber, HighwaySceneChannelType channelType) {
        return new UserAuthorizationRequest(openId, plateNumber, TradeScene.HIGHWAY.name(), channelType.name());
    }
}
