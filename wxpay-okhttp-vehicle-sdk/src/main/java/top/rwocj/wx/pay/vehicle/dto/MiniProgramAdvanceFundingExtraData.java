package top.rwocj.wx.pay.vehicle.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 微信垫资还款商户小程序传递给车主服务小程序的数据
 *
 * @author lqb
 * @since 2024/7/30 15:42
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MiniProgramAdvanceFundingExtraData extends AbstractRequest {

    /**
     * 用户在商户appid下的唯一标识
     */
    @JsonProperty("openid")
    private String openid;

    public MiniProgramAdvanceFundingExtraData(String openid) {
        this.openid = openid;
    }
}
