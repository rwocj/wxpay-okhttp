package top.rwocj.wx.pay.vehicle.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author lqb
 * @since 2024/10/9 15:22
 **/
@Data
@AllArgsConstructor
public class UserState {

    /**
     * 用户状态是否正常并且包含车牌
     */
    @JsonProperty("isUserNormalAndContainsPlateNum")
    private boolean userNormalAndContainsPlateNum;

    /**
     * 用户openid
     */
    private String openid;
}
