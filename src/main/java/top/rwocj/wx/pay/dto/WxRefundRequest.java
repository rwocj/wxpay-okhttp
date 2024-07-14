package top.rwocj.wx.pay.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 微信退款请求体
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class WxRefundRequest {

    /**
     * 微信订单号
     */
    @JsonProperty("transaction_id")
    private String transactionId;
    /**
     * 商户订单号
     */
    @JsonProperty("out_trade_no")
    private String outTradeNo;
    /**
     * 商户退款单号
     * 商户系统内部的退款单号，商户系统内部唯一，只能是数字、大小写字母_-|*@，同一退款单号多次请求只退一笔
     * 退款失败的话，再使用相同的退款单号请求
     * 同一退款单号多次请求只退一笔
     */
    @JsonProperty("out_refund_no")
    private String outRefundNo;
    /**
     * 退款原因
     * 若商户传入，会在下发给用户的退款消息中体现退款原因
     * 注意：若订单退款金额≤1元，且属于部分退款，则不会在退款消息中体现退款原因
     */
    @JsonProperty("reason")
    private String reason;
    /**
     * 退款结果回调url
     * 异步接收微信支付退款结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。
     * 如果参数中传了notify_url，则商户平台上配置的回调地址将不会生效，优先回调当前传的地址。
     */
    @JsonProperty("notify_url")
    private String notifyUrl;

    /**
     * 若传递此参数则使用对应的资金账户退款，否则默认使用未结算资金退款（仅对老资金流商户适用）
     * 枚举值：
     * AVAILABLE：可用余额账户
     */
    @JsonProperty("funds_account")
    private String fundsAccount;
    /**
     * 订单金额
     */
    @JsonProperty("amount")
    private Amount amount;

    /**
     * 指定商品退款需要传此参数，其他场景无需传递
     */
    @JsonProperty("goods_detail")
    private List<RefundGoodsDetail> goodsDetails;

    public WxRefundRequest(String transactionId, String outRefundNo, Amount amount) {
        this.transactionId = transactionId;
        this.outRefundNo = outRefundNo;
        this.amount = amount;
    }

    public WxRefundRequest(Amount amount, String outRefundNo, String outTradeNo) {
        this.outTradeNo = outTradeNo;
        this.outRefundNo = outRefundNo;
        this.amount = amount;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Amount {
        /**
         * 退款金额
         * 退款金额，币种的最小单位，只能为整数，不能超过原订单支付金额
         */
        @JsonProperty("refund")
        private int refund;

        /**
         * 退款需要从指定账户出资时，传递此参数指定出资金额（币种的最小单位，只能为整数）。
         * 同时指定多个账户出资退款的使用场景需要满足以下条件：
         * 1、未开通退款支出分离产品功能；
         * 2、订单属于分账订单，且分账处于待分账或分账中状态。
         * 参数传递需要满足条件：
         * 1、基本账户可用余额出资金额与基本账户不可用余额出资金额之和等于退款金额；
         * 2、账户类型不能重复。
         * 上述任一条件不满足将返回错误
         */
        @JsonProperty("from")
        private List<RefundFrom> from;

        /**
         * 原订单金额
         */
        @JsonProperty("total")
        private int total;
        /**
         * 退款币种
         * 符合ISO 4217标准的三位字母代码，目前只支持人民币：CNY。
         */
        @JsonProperty("currency")
        private String currency;

        public Amount(int refund, int total) {
            this.refund = refund;
            this.total = total;
            this.currency = "CNY";
        }
    }

}
