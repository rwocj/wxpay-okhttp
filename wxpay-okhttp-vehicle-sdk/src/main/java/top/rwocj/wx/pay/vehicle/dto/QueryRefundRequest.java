package top.rwocj.wx.pay.vehicle.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 查询退款请求参数
 *
 * @author lqb
 * @since 2024/7/22 14:01
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class QueryRefundRequest extends AbstractRequest {

    /**
     * 微信支付订单号,四选1
     */
    @JacksonXmlProperty(localName = "transaction_id")
    @JacksonXmlCData
    private String transactionId;

    /**
     * 商户订单号，四选1
     */
    @JacksonXmlProperty(localName = "out_trade_no")
    @JacksonXmlCData
    private String outTradeNo;

    /**
     * 商户退款单号，四选1
     * 商户系统内部的退款单号，商户系统内部唯一，只能是数字、大小写字母_-|*@ ，同一退款单号多次请求只退一笔。
     */
    @JacksonXmlProperty(localName = "out_refund_no")
    @JacksonXmlCData
    private String outRefundNo;

    /**
     * 微信退款单号，四选1
     */
    @JacksonXmlProperty(localName = "refund_id")
    @JacksonXmlCData
    private String refundId;

    /**
     * 偏移量，当部分退款次数超过10次时可使用，表示返回的查询结果从这个偏移量开始取记录
     */
    @JacksonXmlProperty(localName = "offset")
    private Integer offset;

}
