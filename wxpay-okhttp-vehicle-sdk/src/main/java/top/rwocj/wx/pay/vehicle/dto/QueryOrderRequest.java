package top.rwocj.wx.pay.vehicle.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 查询订单请求参数
 *
 * @author lqb
 * @since 2024/7/22 14:01
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class QueryOrderRequest extends AbstractRequest {

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

}
