package top.rwocj.wx.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import top.rwocj.wx.enums.TradeState;
import top.rwocj.wx.enums.TradeType;

import java.util.List;

/**
 * 对应微信通知的支付结果
 */
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
    private String successTime;
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

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public String getMchid() {
        return mchid;
    }

    public void setMchid(String mchid) {
        this.mchid = mchid;
    }

    public TradeState getTradeState() {
        return tradeState;
    }

    public void setTradeState(TradeState tradeState) {
        this.tradeState = tradeState;
    }

    public String getTradeStateDesc() {
        return tradeStateDesc;
    }

    public void setTradeStateDesc(String tradeStateDesc) {
        this.tradeStateDesc = tradeStateDesc;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public String getSuccessTime() {
        return successTime;
    }

    public void setSuccessTime(String successTime) {
        this.successTime = successTime;
    }

    public Payer getPayer() {
        return payer;
    }

    public void setPayer(Payer payer) {
        this.payer = payer;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public TradeType getTradeType() {
        return tradeType;
    }

    public void setTradeType(TradeType tradeType) {
        this.tradeType = tradeType;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public SceneInfo getSceneInfo() {
        return sceneInfo;
    }

    public void setSceneInfo(SceneInfo sceneInfo) {
        this.sceneInfo = sceneInfo;
    }

    public List<PromotionDetail> getPromotionDetail() {
        return promotionDetail;
    }

    public void setPromotionDetail(List<PromotionDetail> promotionDetail) {
        this.promotionDetail = promotionDetail;
    }

    /**
     * 订单金额
     */
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

        public int getPayerTotal() {
            return payerTotal;
        }

        public void setPayerTotal(int payerTotal) {
            this.payerTotal = payerTotal;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getPayerCurrency() {
            return payerCurrency;
        }

        public void setPayerCurrency(String payerCurrency) {
            this.payerCurrency = payerCurrency;
        }
    }

    /**
     * 支付者信息
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Payer {
        /**
         * 用户标识
         */
        @JsonProperty(value = "openid", required = true)
        private String openid;

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }
    }

    /**
     * 场景信息
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SceneInfo {
        /**
         * 商户端设备号
         */
        @JsonProperty("device_id")
        private String deviceId;

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }
    }

    /**
     * 优惠详情
     */
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

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public int getWechatpayContribute() {
            return wechatpayContribute;
        }

        public void setWechatpayContribute(int wechatpayContribute) {
            this.wechatpayContribute = wechatpayContribute;
        }

        public String getCouponId() {
            return couponId;
        }

        public void setCouponId(String couponId) {
            this.couponId = couponId;
        }

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }

        public int getMerchantContribute() {
            return merchantContribute;
        }

        public void setMerchantContribute(int merchantContribute) {
            this.merchantContribute = merchantContribute;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getOtherContribute() {
            return otherContribute;
        }

        public void setOtherContribute(int otherContribute) {
            this.otherContribute = otherContribute;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getStockId() {
            return stockId;
        }

        public void setStockId(String stockId) {
            this.stockId = stockId;
        }

        public List<GoodsDetail> getGoodsDetail() {
            return goodsDetail;
        }

        public void setGoodsDetail(List<GoodsDetail> goodsDetail) {
            this.goodsDetail = goodsDetail;
        }

        /**
         * 单品列表
         */
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

            public String getGoodsRemark() {
                return goodsRemark;
            }

            public void setGoodsRemark(String goodsRemark) {
                this.goodsRemark = goodsRemark;
            }

            public int getQuantity() {
                return quantity;
            }

            public void setQuantity(int quantity) {
                this.quantity = quantity;
            }

            public int getDiscountAmount() {
                return discountAmount;
            }

            public void setDiscountAmount(int discountAmount) {
                this.discountAmount = discountAmount;
            }

            public String getGoodsId() {
                return goodsId;
            }

            public void setGoodsId(String goodsId) {
                this.goodsId = goodsId;
            }

            public int getUnitPrice() {
                return unitPrice;
            }

            public void setUnitPrice(int unitPrice) {
                this.unitPrice = unitPrice;
            }
        }
    }
}
