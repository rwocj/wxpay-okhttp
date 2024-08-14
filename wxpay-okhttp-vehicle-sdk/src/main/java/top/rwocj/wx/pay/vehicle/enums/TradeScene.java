package top.rwocj.wx.pay.vehicle.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 交易场景
 *
 * @author lqb
 * @since 2024/7/18 10:55
 **/
@Getter
@AllArgsConstructor
public enum TradeScene {
    PARKING("PARKING", "车场停车场景"),
    PARKING_SPACE("PARKING SPACE", "车位停车场景"),
    GAS("GAS", "加油场景"),
    HIGHWAY("HIGHWAY", "高速场景"),
    BRIDGE("BRIDGE", "路桥场景"),
    ;
    private final String code;

    private final String remark;
}
