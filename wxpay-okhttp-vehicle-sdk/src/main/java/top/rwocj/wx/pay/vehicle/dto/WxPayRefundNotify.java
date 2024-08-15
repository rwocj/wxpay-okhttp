package top.rwocj.wx.pay.vehicle.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import top.rwocj.wx.pay.common.MD5Util;
import top.rwocj.wx.pay.vehicle.utils.AESUtils;
import top.rwocj.wx.pay.vehicle.utils.XmlUtil;

import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * 退款结果通知
 *
 * @author lqb
 * @since 2024/7/22 14:01
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class WxPayRefundNotify extends AbstractRequest {

    /**
     * 返回状态码
     * SUCCESS/FAIL
     * 此字段是通信标识，非交易标识
     */
    @JacksonXmlProperty(localName = "return_code")
    private String returnCode;

    /**
     * 返回信息，如非空，为错误原因
     * 签名失败
     * 参数格式校验错误
     */
    @JacksonXmlProperty(localName = "return_msg")
    private String returnMsg;

    /**
     * 加密信息
     */
    @JacksonXmlProperty(localName = "req_info")
    @JacksonXmlCData
    private String reqInfo;

    @JsonIgnore
    public WxPayRefundNotifyReqInfo getReqInfo(String key) {
        byte[] bytes = AESUtils.decodeToByte(reqInfo, MD5Util.getMD5(key).toLowerCase());
        String s = new String(bytes, StandardCharsets.UTF_8);
        return XmlUtil.parseObject(s, WxPayRefundNotifyReqInfo.class);
    }

    @JsonIgnore
    public final boolean isHttpSuccess() {
        return "SUCCESS".equals(returnCode);
    }

    @Data
    @JacksonXmlRootElement(localName = "root")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WxPayRefundNotifyReqInfo {

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
         * 微信退款单号
         */
        @JacksonXmlProperty(localName = "refund_id")
        @JacksonXmlCData
        private String refundId;

        /**
         * 商户退款单号
         * 商户系统内部的退款单号，商户系统内部唯一，只能是数字、大小写字母_-|*@ ，同一退款单号多次请求只退一笔。
         */
        @JacksonXmlProperty(localName = "out_refund_no")
        @JacksonXmlCData
        private String outRefundNo;

        /**
         * 订单总金额
         */
        @JacksonXmlProperty(localName = "total_fee")
        private Integer totalFee;

        /**
         * 应结订单金额
         * 应结订单金额=订单金额-免充值代金券金额，应结订单金额<=订单金额。
         */
        @JacksonXmlProperty(localName = "settlement_total_fee")
        private Integer settlementTotalFee;

        /**
         * 申请退款总金额
         */
        @JacksonXmlProperty(localName = "refund_fee")
        private Integer refundFee;

        /**
         * 申请退款总金额
         */
        @JacksonXmlProperty(localName = "settlement_refund_fee")
        private Integer settlementRefundFee;

        /**
         * 退款状态
         * SUCCESS-退款成功
         * CHANGE-退款异常
         * REFUNDCLOSE—退款关闭
         */
        @JacksonXmlProperty(localName = "refund_status")
        @JacksonXmlCData
        private String refundStatus;

        /**
         * 退款成功时间
         * 资金退款至用户账号的时间，格式2017-12-15 09:46:01
         */
        @JacksonXmlProperty(localName = "success_time")
        @JacksonXmlCData
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
        private Date successTime;

        /**
         * 退款入账账户
         * 取当前退款单的退款入账方
         * 1）退回银行卡：
         * {银行名称}{卡类型}{卡尾号}
         * 2）退回支付用户零钱:
         * 支付用户零钱
         * 3）退还商户:
         * 商户基本账户
         * 商户结算银行账户
         * 4）退回支付用户零钱通:
         * 支付用户零钱通
         */
        @JacksonXmlProperty(localName = "refund_recv_accout")
        @JacksonXmlCData
        private String refundRecvAccount;

        /**
         * 退款资金来源
         * REFUND_SOURCE_RECHARGE_FUNDS 可用余额退款/基本账户
         * REFUND_SOURCE_UNSETTLED_FUNDS 未结算资金退款
         */
        @JacksonXmlProperty(localName = "refund_account")
        @JacksonXmlCData
        private String refundAccount;

        /**
         * 退款发起来源
         * API接口
         * VENDOR_PLATFORM商户平台
         */
        @JacksonXmlProperty(localName = "refund_request_source")
        @JacksonXmlCData
        private String refundRequestSource;

        /**
         * 用户退款金额
         * 退款给用户的金额，不包含所有优惠券金额
         */
        @JacksonXmlProperty(localName = "cash_refund_fee")
        private Integer cashRefundFee;

        @JsonIgnore
        public boolean isRefundSuccess() {
            return "SUCCESS".equals(refundStatus);
        }

        @JsonIgnore
        public boolean isRefundClose() {
            return "REFUNDCLOSE".equals(refundStatus);
        }

    }

}
