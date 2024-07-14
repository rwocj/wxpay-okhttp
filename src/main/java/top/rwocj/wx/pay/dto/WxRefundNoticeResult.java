package top.rwocj.wx.pay.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 退款通知
 *
 * @author lqb
 * @since 2024/7/14 11:29
 **/
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
public class WxRefundNoticeResult {
    /**
     * 直连商户的商户号，由微信支付生成并下发。
     * 示例值：1900000100
     */
    @JsonProperty("mchid")
    private String mchId;
    /**
     * 商户订单号
     */
    @JsonProperty("transaction_id")
    private String transactionId;
    /**
     * 微信支付订单号
     */
    @JsonProperty("out_trade_no")
    private String outTradeNo;
    /**
     * 微信支付退款单号
     */
    @JsonProperty("refund_id")
    private String refundId;
    /**
     * 商户退款单号
     */
    @JsonProperty("out_refund_no")
    private String outRefundNo;
    /**
     * 退款状态
     * 退款状态，枚举值：
     * SUCCESS：退款成功
     * CLOSED：退款关闭
     * ABNORMAL：退款异常，退款到银行发现用户的卡作废或者冻结了，导致原路退款银行卡失败，可前往【商户平台—>交易中心】，手动处理此笔退款
     * 示例值：SUCCESS
     */
    @JsonProperty("refund_status")
    private String refundStatus;
    /**
     * 退款成功时间
     * 当退款状态为退款成功时返回此参数
     */
    @JsonProperty(value = "success_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'+'08:00")
    private Date successTime;
    /**
     * 退款入账账户
     * 取当前退款单的退款入账方。
     * 1、退回银行卡：{银行名称}{卡类型}{卡尾号}
     * 2、退回支付用户零钱: 支付用户零钱
     * 3、退还商户: 商户基本账户、商户结算银行账户
     * 4、退回支付用户零钱通：支付用户零钱通
     * 示例值：招商银行信用卡0403
     */
    @JsonProperty("user_received_account")
    private String userReceivedAccount;
    /**
     * 金额信息
     */
    @JsonProperty("amount")
    private Amount amount;

    @NoArgsConstructor
    @Data
    public static class Amount {
        /**
         * 订单金额
         * 订单总金额，单位为分，只能为整数，详见支付金额
         * 示例值：999
         */
        @JsonProperty("total")
        private Integer total;
        /**
         * 退款金额
         * 退款金额，币种的最小单位，只能为整数，不能超过原订单支付金额，如果有使用券，后台会按比例退。
         * 示例值：999
         */
        @JsonProperty("refund")
        private Integer refund;
        /**
         * 用户支付金额
         * 用户实际支付金额，单位为分，只能为整数，详见支付金额
         * 示例值：999
         */
        @JsonProperty("payer_total")
        private Integer payerTotal;
        /**
         * 用户退款金额
         * 退款给用户的金额，不包含所有优惠券金额
         * 示例值：999
         */
        @JsonProperty("payer_refund")
        private Integer payerRefund;
    }
}
