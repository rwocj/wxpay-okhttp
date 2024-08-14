package top.rwocj.wx.pay.vehicle.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import top.rwocj.wx.pay.vehicle.enums.HighwaySceneChannelType;
import top.rwocj.wx.pay.vehicle.enums.TradeScene;

/**
 * 用户授权场景信息传递给车主服务小程序的数据
 *
 * @author lqb
 * @since 2024/7/30 15:42
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserAuthorizationExtraData extends AbstractRequest {

    /**
     * 交易场景
     *
     * @see top.rwocj.wx.pay.vehicle.enums.TradeScene
     */
    @JsonProperty("trade_scene")
    private String tradeScene;

    /**
     * 用户在商户appid下的唯一标识
     */
    @JsonProperty("openid")
    private String openid;

    /**
     * 车牌号
     * 车牌号。仅包括省份+车牌，不包括特殊字符。当场景值为 HIGHWAY 时,plate_number必传
     */
    @JsonProperty("plate_number")
    private String plateNumber;

    /**
     * 物料信息
     * 用户通过扫码进入商户小程序时所扫的物料码信息
     */
    @JsonProperty("material_info")
    private String materialInfo;

    /**
     * 通道类型
     * 高速通道类型，目前可选：ETC、MTC。用户车牌必须具有通道标识时，才允许扣费。当场景值为 HIGHWAY 高速场景时必填
     */
    @JsonProperty("channel_type")
    private String channelType;

    public UserAuthorizationExtraData(TradeScene tradeScene, String openid) {
        this.tradeScene = tradeScene.getCode();
        this.openid = openid;
    }

    public static UserAuthorizationExtraData highwayExtraData(String openid, String plateNumber) {
        UserAuthorizationExtraData userAuthorizationExtraData = new UserAuthorizationExtraData(TradeScene.HIGHWAY, openid);
        userAuthorizationExtraData.setPlateNumber(plateNumber);
        userAuthorizationExtraData.setChannelType(HighwaySceneChannelType.ETC.name());
        return userAuthorizationExtraData;
    }
}
