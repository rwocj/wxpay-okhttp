package top.rwocj.wx.pay.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 微信退款结果
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WxRefundResult {

    /**
     * 微信支付退款单号
     * 示例值：500000003820190527097326788
     */
    @JsonProperty("refund_id")
    private String refundId;
    /**
     * 商户系统内部的退款单号，商户系统内部唯一，只能是数字、大小写字母_-|*@ ，同一退款单号多次请求只退一笔。
     * 示例值：1217752501201407033233368018
     */
    @JsonProperty("out_refund_no")
    private String outRefundNo;

    /**
     * 微信支付交易订单号
     * 示例值：1217752501201407033233368018
     */
    @JsonProperty("transaction_id")
    private String transactionId;
    /**
     * 商户订单号
     */
    @JsonProperty("out_trade_no")
    private String outTradeNo;
    /**
     * 枚举值：
     * ORIGINAL：原路退款
     * BALANCE：退回到余额
     * OTHER_BALANCE：原账户异常退到其他余额账户
     * OTHER_BANKCARD：原银行卡异常退到其他银行卡
     * 示例值：ORIGINAL
     */
    @JsonProperty("channel")
    private String channel;

    /**
     * 取当前退款单的退款入账方，有以下几种情况：
     * 1）退回银行卡：{银行名称}{卡类型}{卡尾号}
     * 2）退回支付用户零钱:支付用户零钱
     * 3）退还商户:商户基本账户商户结算银行账户
     * 4）退回支付用户零钱通:支付用户零钱通
     * 示例值：招商银行信用卡0403
     */
    @JsonProperty("user_received_account")
    private String userReceivedAccount;
    /**
     * 退款成功时间
     * 退款成功时间，当退款状态为退款成功时有返回。
     * 示例值：2020-12-01T16:18:12+08:00
     */
    @JsonProperty(value = "success_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'+'08:00", timezone = "Asia/Shanghai")
    private Date successTime;
    /**
     * 退款受理时间
     */
    @JsonProperty("create_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'+'08:00", timezone = "Asia/Shanghai")
    private Date createTime;
    /**
     * 退款状态
     * 退款到银行发现用户的卡作废或者冻结了，导致原路退款银行卡失败，可前往商户平台-交易中心，手动处理此笔退款。
     * 枚举值：
     * SUCCESS：退款成功
     * CLOSED：退款关闭
     * PROCESSING：退款处理中
     * ABNORMAL：退款异常
     * 示例值：SUCCESS
     */
    @JsonProperty("status")
    private String status;
    /**
     * 退款所使用资金对应的资金账户类型
     * 枚举值：
     * UNSETTLED : 未结算资金
     * AVAILABLE : 可用余额
     * UNAVAILABLE : 不可用余额
     * OPERATION : 运营户
     * BASIC : 基本账户（含可用余额和不可用余额）
     */
    @JsonProperty("funds_account")
    private String fundsAccount;
    /**
     * 订单金额
     */
    @JsonProperty("amount")
    private Amount amount;
    /**
     * 优惠退款详情
     */
    @JsonProperty("promotion_detail")
    private List<PromotionDetail> promotionDetail;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Amount {
        /**
         * 订单总金额，单位为分
         * 示例值：100
         */
        private Integer total;
        /**
         * 退款金额
         */
        @JsonProperty("refund")
        private Integer refund;
        /**
         * 退款出资的账户类型及金额信息
         */
        @JsonProperty("from")
        private List<RefundFrom> from;
        /**
         * 用户支付金额
         * 现金支付金额，单位为分，只能为整数
         * 示例值：90
         */
        @JsonProperty("payer_total")
        private Integer payerTotal;
        /**
         * 用户退款金额
         */
        @JsonProperty("payer_refund")
        private Integer payerRefund;
        /**
         * 应结退款金额
         * 去掉非充值代金券退款金额后的退款金额，单位为分，退款金额等于申请退款金额减去非充值代金券退款金额，退款金额小于等于申请退款金额
         */
        @JsonProperty("settlement_refund")
        private Integer settlementRefund;

        /**
         * 应结订单金额
         * 应结订单金额等于订单金额减去免充值代金券金额，应结订单金额小于等于订单金额，单位为分
         * 示例值：100
         */
        @JsonProperty("settlement_total")
        private Integer settlementTotal;
        /**
         * 优惠退款金额
         */
        @JsonProperty("discount_refund")
        private Integer discountRefund;
        /**
         * 退款币种
         */
        @JsonProperty("currency")
        private String currency;

        /**
         * 手续费退款金额，单位为分。
         */
        @JsonProperty("refund_fee")
        private Integer refundFee;

    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PromotionDetail {
        /**
         * 券ID
         */
        @JsonProperty("promotion_id")
        private String promotionId;
        /**
         * 优惠范围
         * GLOBAL：全场代金券
         * SINGLE：单品优惠
         */
        @JsonProperty("scope")
        private String scope;
        /**
         * 优惠类型
         * COUPON：充值型代金券，商户需要预先充值营销经费
         * DISCOUNT：免充值型优惠券，商户不需要预先充值营销经费
         */
        @JsonProperty("type")
        private String type;
        /**
         * 优惠券面额
         * 用户享受优惠的金额（优惠券面额=微信出资金额+商家出资金额+其他出资方金额 ）。
         */
        @JsonProperty("amount")
        private int amount;
        /**
         * 优惠退款金额
         * 代金券退款金额小于等于退款金额，退款金额减去代金券或立减优惠退款金额为现金，说明详见《代金券或立减优惠》。
         */
        @JsonProperty("refund_amount")
        private int refundAmount;

        /**
         * 指定商品退款需要传此参数，其他场景无需传递
         */
        @JsonProperty("goods_detail")
        private List<RefundGoodsDetail> goodsDetails;

    }
}
