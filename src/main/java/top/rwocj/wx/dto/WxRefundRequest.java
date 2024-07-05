package top.rwocj.wx.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * 微信退款请求体
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class WxRefundRequest {

    /**
     * 二级商户号
     *
     * @required
     */
    @JsonProperty("sub_mchid")
    private String subMchid;
    /**
     * 电商平台APPID
     *
     * @required
     */
    @JsonProperty("sp_appid")
    private String spAppid;
    /**
     * 二级商户APPID
     */
    @JsonProperty("sub_appid")
    private String subAppid;
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
     *
     * @required
     */
    @JsonProperty("out_refund_no")
    private String outRefundNo;
    /**
     * 退款原因
     * 若商户传入，会在下发给用户的退款消息中体现退款原因。
     * 注意：若订单退款金额≤1元，且属于部分退款，则不会在退款消息中体现退款原因
     */
    @JsonProperty("reason")
    private String reason;
    /**
     * 订单金额
     *
     * @required
     */
    @JsonProperty("amount")
    private Amount amount;
    /**
     * 退款结果回调url
     * 异步接收微信支付退款结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。
     * 如果参数中传了notify_url，则商户平台上配置的回调地址将不会生效，优先回调当前传的地址。
     */
    @JsonProperty("notify_url")
    private String notifyUrl;

    public String getSubMchid() {
        return subMchid;
    }

    public void setSubMchid(String subMchid) {
        this.subMchid = subMchid;
    }

    public String getSpAppid() {
        return spAppid;
    }

    public void setSpAppid(String spAppid) {
        this.spAppid = spAppid;
    }

    public String getSubAppid() {
        return subAppid;
    }

    public void setSubAppid(String subAppid) {
        this.subAppid = subAppid;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getOutRefundNo() {
        return outRefundNo;
    }

    public void setOutRefundNo(String outRefundNo) {
        this.outRefundNo = outRefundNo;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Amount {
        /**
         * 退款金额
         * 退款金额，币种的最小单位，只能为整数，不能超过原订单支付金额
         *
         * @required
         */
        @JsonProperty("refund")
        private int refund;
        /**
         * 原订单金额
         *
         * @required
         */
        @JsonProperty("total")
        private int total;
        /**
         * 退款币种
         * 符合ISO 4217标准的三位字母代码，目前只支持人民币：CNY。
         */
        @JsonProperty("currency")
        private String currency;

        public int getRefund() {
            return refund;
        }

        public void setRefund(int refund) {
            this.refund = refund;
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
    }
}
