package top.rwocj.wx.pay.vehicle.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 申请退款响应
 *
 * @author lqb
 * @since 2024/7/22 14:01
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PayRefundResponse extends AbstractResponse {

    /**
     * 微信支付订单号
     */
    @JacksonXmlProperty(localName = "transaction_id")
    @JacksonXmlCData
    private String transactionId;

    /**
     * 商户订单号
     */
    @JacksonXmlProperty(localName = "out_trade_no")
    @JacksonXmlCData
    private String outTradeNo;

    /**
     * 商户退款单号
     * 商户系统内部的退款单号，商户系统内部唯一，只能是数字、大小写字母_-|*@ ，同一退款单号多次请求只退一笔。
     */
    @JacksonXmlProperty(localName = "out_refund_no")
    @JacksonXmlCData
    private String outRefundNo;

    /**
     * 微信退款单号
     */
    @JacksonXmlProperty(localName = "refund_id")
    @JacksonXmlCData
    private String refundId;

    /**
     * 申请退款总金额
     */
    @JacksonXmlProperty(localName = "refund_fee")
    private Integer refundFee;

    /**
     * 申请退款总金额
     */
    @JacksonXmlProperty(localName = "settlement_refund_fee")
    private Integer settlementRefundFee;

    /**
     * 订单总金额
     */
    @JacksonXmlProperty(localName = "total_fee")
    private Integer totalFee;

    /**
     * 应结订单金额
     * 应结订单金额=订单金额-免充值代金券金额，应结订单金额<=订单金额。
     */
    @JacksonXmlProperty(localName = "settlement_total_fee")
    private Integer settlementTotalFee;

    /**
     * 货币种类
     * 符合ISO 4217标准的三位字母代码，默认人民币:CNY
     */
    @JacksonXmlProperty(localName = "fee_type")
    @JacksonXmlCData
    private String feeType;

    /**
     * 现金支付金额
     * 现金支付金额订单现金支付金额，详见支付金额
     */
    @JacksonXmlProperty(localName = "cash_fee")
    private Integer cashFee;

    /**
     * 现金支付币种
     * 符合ISO 4217标准的三位字母代码，默认人民币:CNY
     */
    @JacksonXmlProperty(localName = "cash_fee_type")
    @JacksonXmlCData
    private String cashFeeType;

    /**
     * 现金退款金额
     * 现金退款金额，单位为分，只能为整数，详见支付金额
     */
    @JacksonXmlProperty(localName = "cash_refund_fee")
    private Integer cashRefundFee;

    /**
     * 代金券退款总金额
     * 代金券退款金额<=退款金额，退款金额-代金券或立减优惠退款金额为现金，说明详见代金券或立减优惠
     */
    @JacksonXmlProperty(localName = "coupon_refund_fee")
    private Integer couponRefundFee;

    /**
     * 退款代金券使用数量
     */
    @JacksonXmlProperty(localName = "coupon_refund_count")
    private Integer couponRefundCount;

}
