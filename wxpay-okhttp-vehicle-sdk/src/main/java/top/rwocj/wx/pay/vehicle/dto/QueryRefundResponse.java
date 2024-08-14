package top.rwocj.wx.pay.vehicle.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

/**
 * 查询退款响应
 *
 * @author lqb
 * @since 2024/7/22 14:01
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class QueryRefundResponse extends AbstractResponse {

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
     * 退款笔数
     */
    @JacksonXmlProperty(localName = "refund_count")
    private Integer refundCount;

    /**
     * 订单总退款次数
     */
    @JacksonXmlProperty(localName = "total_refund_count")
    private Integer totalRefundCount;

    /**
     * 退款总金额
     */
    @JacksonXmlProperty(localName = "refund_fee")
    private Integer refundFee;

    /**
     * 代金券退款总金额
     * 代金券退款金额<=退款金额，退款金额-代金券或立减优惠退款金额为现金，说明详见代金券或立减优惠
     */
    @JacksonXmlProperty(localName = "coupon_refund_fee")
    private Integer couponRefundFee;

    /**
     * 现金退款金额
     * 现金退款金额，单位为分，只能为整数，详见支付金额
     */
    @JacksonXmlProperty(localName = "cash_refund_fee")
    private Integer cashRefundFee;

    /**
     * 商户退款单号
     * 商户系统内部的退款单号，商户系统内部唯一，只能是数字、大小写字母_-|*@ ，同一退款单号多次请求只退一笔。
     */
    @JacksonXmlProperty(localName = "out_refund_no_0")
    @JacksonXmlCData
    private String outRefundNo0;

    /**
     * 微信退款单号
     */
    @JacksonXmlProperty(localName = "refund_id_0")
    @JacksonXmlCData
    private String refundId0;

    /**
     * 退款渠道
     * ORIGINAL—原路退款
     * BALANCE—退回到余额
     * OTHER_BALANCE—原账户异常退到其他余额账户
     * OTHER_BANKCARD—原银行卡异常退到其他银行卡
     */
    @JacksonXmlProperty(localName = "refund_channel_0")
    private String refund_channel0;

    /**
     * 申请退款金额
     */
    @JacksonXmlProperty(localName = "refund_fee_0")
    private Integer refundFee0;

    /**
     * 退款金额
     */
    @JacksonXmlProperty(localName = "settlement_refund_fee_0")
    private Integer settlementRefundFee0;

    /**
     * 退款状态：
     * SUCCESS—退款成功
     * REFUNDCLOSE—退款关闭，指商户发起退款失败的情况。
     * PROCESSING—退款处理中
     * CHANGE—退款异常，退款到银行发现用户的卡作废或者冻结了，导致原路退款银行卡失败，可前往商户平台（pay.weixin.qq.com）-交易中心，手动处理此笔退款。$n为下标，从0开始编号。
     */
    @JacksonXmlProperty(localName = "refund_status_0")
    @JacksonXmlCData
    private String refundStatus0;

    /**
     * 退款成功时间
     * 资金退款至用户账号的时间，格式2017-12-15 09:46:01
     */
    @JacksonXmlProperty(localName = "refund_success_time_0")
    @JacksonXmlCData
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date refundSuccessTime0;

}
