package top.rwocj.wx.pay.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.rwocj.wx.pay.enums.OrderType;

import java.util.Date;
import java.util.List;

/**
 * app/native/h5/jsapi下单请求体
 * <a href="https://pay.weixin.qq.com/wiki/doc/apiv3/wxpay/pay/transactions/chapter3_2.shtml">...</a>
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WxCreateOrderRequest {

    /**
     * 下单类型
     */
    @JsonIgnore
    private OrderType orderType;
    /**
     * 交易结束时间
     */
    @JsonProperty("time_expire")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'+'08:00", timezone = "Asia/Shanghai")
    private Date timeExpire;
    /**
     * 订单金额
     */
    private Amount amount;
    /**
     * 直连商户号，不需要手动设置，会自动配置
     */
    @JsonProperty("mchid")
    private String mchId;
    /**
     * 商品描述
     */
    private String description;
    /**
     * 通知地址,如为null,会自动设置配置文件中的
     */
    @JsonProperty("notify_url")
    private String notifyUrl;
    /**
     * 支付者信息
     */
    private Payer payer;
    /**
     * 商户订单号
     * 商户系统内部订单号，只能是数字、大小写字母_-*且在同一个商户号下唯一，详见【商户订单号】。
     * 特殊规则：最小字符长度为6
     */
    @JsonProperty("out_trade_no")
    private String outTradeNo;
    /**
     * 订单优惠标记
     */
    @JsonProperty("goods_tag")
    private String goodsTag;
    /**
     * 传入true时，支付成功消息和支付详情页将出现开票入口。需要在微信支付商户平台或微信公众平台开通电子发票功能，传此字段才可生效。
     */
    @JsonProperty("support_fapiao")
    private Boolean supportFapiao;
    /**
     * 应用ID,如为null,会自动设置配置文件中的，如同一个商户需要支持不同的支付方式，可手动设置其他支付方式的appid
     */
    private String appid;
    /**
     * 附加数据
     * 附加数据，在查询API和支付通知中原样返回，可作为自定义参数使用
     */
    private String attach;
    /**
     * 优惠功能详情
     */
    private Detail detail;
    /**
     * 场景信息
     */
    @JsonProperty("scene_info")
    private SceneInfo sceneInfo;

    public WxCreateOrderRequest(String description, String outTradeNo, OrderType orderType, int total, String openid) {
        this(description, outTradeNo, orderType, new Amount(total), new Payer(openid));
    }

    public WxCreateOrderRequest(String description, String outTradeNo, OrderType orderType, Amount amount, Payer payer) {
        this.description = description;
        this.outTradeNo = outTradeNo;
        this.orderType = orderType;
        this.amount = amount;
        this.payer = payer;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NoArgsConstructor
    public static class Amount {

        /**
         * 总金额,订单总金额，单位为分。
         */
        private int total;

        /**
         * 货币类型
         * CNY：人民币，境内商户号仅支持人民币
         */
        private String currency;

        public Amount(int total) {
            this.total = total;
            this.currency = "CNY";
        }

    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Data
    public static class Detail {

        /**
         * 商品小票ID
         */
        @JsonProperty("invoice_id")
        private String invoiceId;
        /**
         * 订单原价
         * 1、商户侧一张小票订单可能被分多次支付，订单原价用于记录整张小票的交易金额。
         * 2、当订单原价与支付金额不相等，则不享受优惠。
         * 3、该字段主要用于防止同一张小票分多次支付，以享受多次优惠的情况，正常支付订单不必上传此参数
         */
        @JsonProperty("cost_price")
        private Integer costPrice;
        /**
         * 单品列表
         */
        @JsonProperty("goods_detail")
        private List<GoodsDetail> goodsDetail;

        public Detail(Integer costPrice) {
            this.costPrice = costPrice;
        }

        @Data
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class GoodsDetail {

            /**
             * 商品实际名称
             */
            @JsonProperty("goods_name")
            private String goodsName;
            /**
             * 微信侧商品编码
             * 微信支付定义的统一商品编号（没有可不传）
             */
            @JsonProperty("wechatpay_goods_id")
            private String wechatpayGoodsId;
            /**
             * 商品数量
             */
            private int quantity;
            /**
             * 商户侧商品编码
             * 由半角的大小写字母、数字、中划线、下划线中的一种或几种组成。
             */
            @JsonProperty("merchant_goods_id")
            private String merchantGoodsId;
            /**
             * 商品单价
             * 商品单价，单位为分
             */
            @JsonProperty("unit_price")
            private int unitPrice;

            public GoodsDetail(String merchantGoodsId) {
                this.merchantGoodsId = merchantGoodsId;
            }
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Data
    public static class SceneInfo {

        /**
         * 门店信息
         */
        @JsonProperty("store_info")
        private StoreInfo storeInfo;
        /**
         * 商户端设备号
         * 商户端设备号（门店号或收银设备ID）。
         */
        @JsonProperty("device_id")
        private String deviceId;
        /**
         * 用户终端IP
         * 调用微信支付API的机器IP，支持IPv4和IPv4两种格式的IP地址。
         */
        @JsonProperty("payer_client_ip")
        private String payerClientIp;

        public SceneInfo(String payerClientIp) {
            this.payerClientIp = payerClientIp;
        }

        @Data
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class StoreInfo {

            /**
             * 详细地址
             */
            private String address;
            /**
             * 地区编码
             * 地区编码，详细请见省市区编号对照表(<a href="https://pay.weixin.qq.com/wiki/doc/apiv3/wxpay/ecommerce/applyments/chapter4_1.shtml">...</a>)
             */
            @JsonProperty("area_code")
            private String areaCode;
            /**
             * 门店名称
             */
            private String name;
            /**
             * 门店编号
             * 商户侧门店编号
             */
            private String id;

            public StoreInfo(String address, String areaCode, String name) {
                this.address = address;
                this.areaCode = areaCode;
                this.name = name;
            }

        }
    }

}
