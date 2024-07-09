package top.rwocj.wx.pay.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 微信退款结果
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WxRefundRes {

    /**
     * 微信退款单号
     *
     * @required
     */
    @JsonProperty("refund_id")
    private String refundId;
    /**
     * 商户退款单号
     *
     * @required
     */
    @JsonProperty("out_refund_no")
    private String outRefundNo;
    /**
     * 退款创建时间
     *
     * @required
     */
    @JsonProperty("create_time")
    private String createTime;
    /**
     * 订单金额
     *
     * @required
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
         * 退款金额
         *
         * @required
         */
        @JsonProperty("refund")
        private int refund;
        /**
         * 用户退款金额
         *
         * @required
         */
        @JsonProperty("payer_refund")
        private int payerRefund;
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
         *
         * @required
         */
        @JsonProperty("scope")
        private String scope;
        /**
         * 优惠类型
         * COUPON：充值型代金券，商户需要预先充值营销经费
         * DISCOUNT：免充值型优惠券，商户不需要预先充值营销经费
         *
         * @required
         */
        @JsonProperty("type")
        private String type;
        /**
         * 优惠券面额
         * 用户享受优惠的金额（优惠券面额=微信出资金额+商家出资金额+其他出资方金额 ）。
         *
         * @required
         */
        @JsonProperty("amount")
        private int amount;
        /**
         * 优惠退款金额
         * 代金券退款金额<=退款金额，退款金额-代金券或立减优惠退款金额为现金，说明详见《代金券或立减优惠》。
         *
         * @required
         */
        @JsonProperty("refund_amount")
        private int refundAmount;

    }
}
