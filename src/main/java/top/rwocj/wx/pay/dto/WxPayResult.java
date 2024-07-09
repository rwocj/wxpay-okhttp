package top.rwocj.wx.pay.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import top.rwocj.wx.pay.enums.TradeState;
import top.rwocj.wx.pay.enums.TradeType;

import java.util.Date;
import java.util.List;

/**
 * 对应微信通知的支付结果
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WxPayResult {

    /**
     * 微信支付订单号
     */
    @JsonProperty(value = "transaction_id")
    private String transactionId;
    /**
     * 订单金额
     */
    @JsonProperty(value = "amount", required = true)
    private Amount amount;
    /**
     * 直连商户号
     */
    @JsonProperty(value = "mchid", required = true)
    private String mchid;
    /**
     * 交易状态
     */
    @JsonProperty(value = "trade_state", required = true)
    private TradeState tradeState;
    /**
     * 交易状态描述
     *
     * @mock 支付失败，请重新下单支付
     */
    @JsonProperty(value = "trade_state_desc", required = true)
    private String tradeStateDesc;
    /**
     * 付款银行
     */
    @JsonProperty("bank_type")
    private String bankType;
    /**
     * 支付完成时间
     *
     * @mock 2018-06-08T10:34:56+08:00
     */
    @JsonProperty(value = "success_time", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'+'08:00")
    private Date successTime;
    /**
     * 支付者
     */
    @JsonProperty("payer")
    private Payer payer;
    /**
     * 商户订单号
     */
    @JsonProperty(value = "out_trade_no", required = true)
    private String outTradeNo;
    /**
     * 公众号ID
     */
    @JsonProperty(value = "appid", required = true)
    private String appid;
    /**
     * 交易类型
     */
    @JsonProperty("trade_type")
    private TradeType tradeType;
    /**
     * 附加数据
     */
    @JsonProperty("attach")
    private String attach;
    /**
     * 场景信息
     */
    @JsonProperty("scene_info")
    private SceneInfo sceneInfo;
    /**
     * 优惠详情
     */
    @JsonProperty("promotion_detail")
    private List<PromotionDetail> promotionDetail;

    /**
     * 订单金额
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Amount {
        /**
         * 用户支付金额
         */
        @JsonProperty("payer_total")
        private int payerTotal;
        /**
         * 总金额
         */
        @JsonProperty("total")
        private int total;
        /**
         * 货币类型
         */
        @JsonProperty("currency")
        private String currency;
        /**
         * 用户支付币种
         */
        @JsonProperty("payer_currency")
        private String payerCurrency;

    }

    /**
     * 场景信息
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SceneInfo {
        /**
         * 商户端设备号
         */
        @JsonProperty("device_id")
        private String deviceId;

    }

    /**
     * 优惠详情
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PromotionDetail {
        /**
         * 优惠券面额
         */
        @JsonProperty(value = "amount", required = true)
        private int amount;
        /**
         * 微信出资
         */
        @JsonProperty("wechatpay_contribute")
        private int wechatpayContribute;
        /**
         * 券ID
         */
        @JsonProperty(value = "coupon_id", required = true)
        private String couponId;
        /**
         * 优惠范围
         * GLOBAL：全场代金券
         * SINGLE：单品优惠
         */
        @JsonProperty("scope")
        private String scope;
        /**
         * 商户出资
         */
        @JsonProperty("merchant_contribute")
        private int merchantContribute;
        /**
         * 优惠名称
         */
        @JsonProperty("name")
        private String name;
        /**
         * 其他出资
         */
        @JsonProperty("other_contribute")
        private int otherContribute;
        /**
         * 优惠币种
         * CNY：人民币，境内商户号仅支持人民币。
         */
        @JsonProperty("currency")
        private String currency;
        /**
         * 优惠类型
         * CASH：充值
         * NOCASH：预充值
         */
        @JsonProperty("type")
        private String type;
        /**
         * 活动ID
         */
        @JsonProperty("stock_id")
        private String stockId;
        /**
         * 单品列表
         */
        @JsonProperty("goods_detail")
        private List<GoodsDetail> goodsDetail;

        /**
         * 单品列表
         */
        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class GoodsDetail {
            /**
             * 商品备注
             */
            @JsonProperty("goods_remark")
            private String goodsRemark;
            /**
             * 商品数量
             */
            @JsonProperty(value = "quantity", required = true)
            private int quantity;
            /**
             * 商品优惠金额
             */
            @JsonProperty(value = "discount_amount", required = true)
            private int discountAmount;
            /**
             * 商品编码
             */
            @JsonProperty(value = "goods_id", required = true)
            private String goodsId;
            /**
             * 商品单价
             */
            @JsonProperty(value = "unit_price", required = true)
            private int unitPrice;

        }
    }
}
