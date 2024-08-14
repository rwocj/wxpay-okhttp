package top.rwocj.wx.pay.vehicle.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 跳转场景
 *
 * @author lqb
 * @since 2024/7/18 10:57
 **/
@Getter
@AllArgsConstructor
public enum JumpScene {

    APP("PARKING", "通过APP跳转"),
    H5("H5", "通过公众号H5跳转"),
    MINI_PROGRAM(null, "通过小程序跳转"),
    ;
    private final String code;

    private final String remark;
}
