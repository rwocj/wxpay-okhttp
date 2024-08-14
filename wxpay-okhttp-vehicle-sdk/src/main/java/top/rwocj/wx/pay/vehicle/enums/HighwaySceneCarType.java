package top.rwocj.wx.pay.vehicle.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lqb
 * @since 2024/7/22 10:17
 **/
@Getter
@AllArgsConstructor
public enum HighwaySceneCarType {

    BUS("客车"),
    TRUCK("货车");

    private final String remark;
}
