package top.rwocj.wx.pay.vehicle.dto;

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
     * 是否包含新办的车牌
     */
    private boolean containPlateNum;

    /**
     * 用户状态是否正常
     */
    private boolean normal;

    /**
     * 跳转路径
     */
    private String path;

    /**
     * 跳转需要的其他信息
     */
    private UserAuthorizationExtraData extraData;
}
