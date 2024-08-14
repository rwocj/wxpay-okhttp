package top.rwocj.wx.pay.vehicle.enums;

/**
 * @author lqb
 * @since 2024/7/22 13:30
 **/
public enum BillType {
    /**
     * 返回当日所有订单信息，默认值
     */
    ALL,

    /**
     * 返回当日成功支付的订单
     */
    SUCCESS,

    /**
     * 返回当日退款订单
     */
    REFUND,

    /**
     * 返回当日充值退款订单
     */
    RECHARGE_REFUND
}
