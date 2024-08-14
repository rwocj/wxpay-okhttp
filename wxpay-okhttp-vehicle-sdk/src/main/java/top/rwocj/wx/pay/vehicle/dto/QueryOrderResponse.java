package top.rwocj.wx.pay.vehicle.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

/**
 * 查询订单响应
 *
 * @author lqb
 * @since 2024/7/22 14:01
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class QueryOrderResponse extends AbstractResponse {

    /**
     * 设备号
     * 微信支付分配的终端设备号，
     */
    @JacksonXmlProperty(localName = "device_info")
    @JacksonXmlCData
    private String deviceInfo;

    /**
     * openid
     * 用户在商户appid或子商户appid下的唯一标识。
     */
    @JacksonXmlProperty(localName = "openid")
    @JacksonXmlCData
    private String openId;

    /**
     * 委托代扣协议id
     * 签约成功后微信返回的委托代扣协议id
     */
    @JacksonXmlProperty(localName = "contract_id")
    @JacksonXmlCData
    private String contractId;

    /**
     * 是否关注公众账号
     * 用户是否关注公众账号，Y-关注，N-未关注，仅在公众账号类型支付有效
     */
    @JacksonXmlProperty(localName = "is_subscribe")
    @JacksonXmlCData
    public String isSubscribe;

    /**
     * 交易类型
     * PAP
     */
    @JacksonXmlProperty(localName = "trade_type")
    @JacksonXmlCData
    private String tradeType;

    /**
     * 交易状态
     * SUCCESS—支付成功
     * PAY_FAIL--支付失败(其他原因，如银行返回失败)
     */
    @JacksonXmlProperty(localName = "trade_state")
    @JacksonXmlCData
    private String tradeState;

    /**
     * 交易状态描述
     * 对当前查询订单状态的描述和下一步操作的指引
     */
    @JacksonXmlProperty(localName = "trade_state_desc")
    @JacksonXmlCData
    private String tradeStateDesc;

    /**
     * 付款银行
     * 银行类型，采用字符串类型的银行标识，银行类型见
     * <a href="https://pay.weixin.qq.com/wiki/doc/api/vehicle_v2_sl.php?chapter=4_2">银行列表</a>
     */
    @JacksonXmlProperty(localName = "bank_type")
    @JacksonXmlCData
    private String bankType;

    /**
     * 订单总金额
     * 单位为分(trade_state为SUCCESS和REFUND时才有返回)
     */
    @JacksonXmlProperty(localName = "total_fee")
    private Integer totalFee;

    /**
     * 货币类型
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
     * 现金支付货币类型
     * 符合ISO 4217标准的三位字母代码，默认人民币:CNY
     */
    @JacksonXmlProperty(localName = "cash_fee_type")
    @JacksonXmlCData
    private String cashFeeType;

    /**
     * 应结订单金额
     * 应结订单金额=订单金额-免充值代金券金额，应结订单金额<=订单金额。
     */
    @JacksonXmlProperty(localName = "settlement_total_fee")
    private Integer settlementTotalFee;

    /**
     * 代金券或立减优惠金额
     * 代金券或立减优惠金额<=订单总金额，订单总金额-代金券或立减优惠金额=现金支付金额，详见支付金额
     */
    @JacksonXmlProperty(localName = "coupon_fee")
    private Integer couponFee;

    /**
     * 代金券或立减优惠使用数量
     */
    @JacksonXmlProperty(localName = "coupon_count")
    private Integer couponCount;

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
     * 商家数据包,原样返回
     */
    @JacksonXmlProperty(localName = "attach")
    @JacksonXmlCData
    private String attach;

    /**
     * 支付完成时间
     * 格式为yyyyMMddHHmmss
     */
    @JacksonXmlProperty(localName = "time_end")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMddHHmmss", timezone = "Asia/Shanghai")
    @JacksonXmlCData
    private Date timeEnd;

    /**
     * 是否交易成功
     */
    @JsonIgnore
    public boolean isTradeSuccess() {
        return "SUCCESS".equals(tradeState);
    }
}
