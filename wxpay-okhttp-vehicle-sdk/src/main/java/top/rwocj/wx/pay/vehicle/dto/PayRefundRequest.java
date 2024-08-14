package top.rwocj.wx.pay.vehicle.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 申请退款请求参数
 *
 * @author lqb
 * @since 2024/7/22 14:01
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PayRefundRequest extends AbstractRequest {

    /**
     * 微信支付订单号,与商户订单号二选一
     */
    @JacksonXmlProperty(localName = "transaction_id")
    @JacksonXmlCData
    private String transactionId;

    /**
     * 商户订单号，与微信支付订单号二选一
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
     * 订单总金额
     * 单位为分(trade_state为SUCCESS和REFUND时才有返回)
     */
    @JacksonXmlProperty(localName = "total_fee")
    private Integer totalFee;

    /**
     * 退款总金额，单位为分，只能为整数，可部分退款。详见支付金额
     */
    @JacksonXmlProperty(localName = "refund_fee")
    private Integer refundFee;

    /**
     * 退款货币种类
     * 符合ISO 4217标准的三位字母代码，默认人民币:CNY
     */
    @JacksonXmlProperty(localName = "refund_fee_type")
    @JacksonXmlCData
    private String refundFeeType;

    /**
     * 退款原因
     */
    @JacksonXmlProperty(localName = "refund_desc")
    @JacksonXmlCData
    private String refundDesc;

    /**
     * 退款资金来源
     * 仅针对老资金流商户使用
     */
    @JacksonXmlProperty(localName = "refund_account")
    @JacksonXmlCData
    private String refundAccount;

    /**
     * 异步接收微信支付退款结果通知的回调地址，通知URL必须为外网可访问的url，不允许带参数。
     * 公网域名必须为https，如果是走专线接入，使用专线NAT IP或者私有回调域名可使用http。
     * 如果参数中传了notify_url，则商户平台上配置的回调地址将不会生效。
     * 接受扣款结果异步回调通知的url
     */
    @JacksonXmlProperty(localName = "notify_url")
    @JacksonXmlCData
    private String notifyUrl;

    public PayRefundRequest(String transactionId, String outTradeNo, String outRefundNo, Integer totalFee, Integer refundFee) {
        this.transactionId = transactionId;
        this.outTradeNo = outTradeNo;
        this.outRefundNo = outRefundNo;
        this.totalFee = totalFee;
        this.refundFee = refundFee;
    }

    public static PayRefundRequest ofWithOutTradeNo(String outTradeNo, String outRefundNo, Integer totalFee, Integer refundFee) {
        return new PayRefundRequest(null, outTradeNo, outRefundNo, totalFee, refundFee);
    }

    public static PayRefundRequest ofWithTransactionId(String transactionId, String outRefundNo, Integer totalFee, Integer refundFee) {
        return new PayRefundRequest(transactionId, null, outRefundNo, totalFee, refundFee);
    }
}
