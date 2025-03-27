package top.rwocj.wx.pay.vehicle.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 用户授权查询，建议使用containPlateNum和normal进行判断是否要跳转车主小程序判断，以path判断实测并不准确
 *
 * @author lqb
 * @since 2024/8/14 09:34
 **/
@Data
public class UserAuthorizationInfo {

    /**
     * 用户状态查询是否成功
     */
    private boolean querySuccess;

    /**
     * 是否包含新办的车牌
     */
    @JsonProperty("containPlateNum")
    private Boolean containPlateNum;

    /**
     * 用户状态是否正常
     */
    @JsonProperty("normal")
    private Boolean normal;

    /**
     * 跳转路径
     */
    private String path;

    /**
     * 跳转需要的其他信息
     */
    private UserAuthorizationExtraData extraData;

    public static UserAuthorizationInfo queryFailed() {
        UserAuthorizationInfo userAuthorizationInfo = new UserAuthorizationInfo();
        userAuthorizationInfo.setQuerySuccess(false);
        return userAuthorizationInfo;
    }
}
