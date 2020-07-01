package com.github.rwocj.wx.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rwocj.wx.enums.OrderType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * app/native/h5/jsapi下单请求体
 * https://pay.weixin.qq.com/wiki/doc/apiv3/wxpay/pay/transactions/chapter3_2.shtml
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WxCreateOrderRequest {

    /**
     * 下单类型
     *
     * @required
     */
    @JsonIgnore
    private OrderType orderType;

    /**
     * 交易结束时间
     *
     * @mock 2018-06-08T10:34:56+08:00
     */
    @JsonProperty("time_expire")
    private String timeExpire;
    /**
     * 订单金额
     *
     * @required
     */
    private Amount amount;
    /**
     * 直连商户号，如为null,会自动配置配置文件中设置的
     *
     * @mock 1230000109
     * @required
     */
    private String mchid;
    /**
     * 商品描述
     *
     * @mock Image形象店-深圳腾大-QQ公仔
     * @required
     */
    private String description;
    /**
     * 通知地址,如为null,会自动配置配置文件中设置的
     *
     * @mock https://www.weixin.qq.com/wxpay/pay.php
     * @required
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
     *
     * @mock 1217752501201407033233368018
     * @required
     */
    @JsonProperty("out_trade_no")
    private String outTradeNo;
    /**
     * 订单优惠标记
     *
     * @mock WXG
     */
    @JsonProperty("goods_tag")
    private String goodsTag;
    /**
     * 公众号ID,如为null,会自动配置配置文件中设置的
     *
     * @required
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

    public WxCreateOrderRequest() {

    }

    public WxCreateOrderRequest(String description, String outTradeNo, OrderType orderType, Amount amount, Payer payer) {
        this.description = description;
        this.outTradeNo = outTradeNo;
        this.orderType = orderType;
        this.amount = amount;
        this.payer = payer;
    }

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Amount {

        /**
         * 总金额,订单总金额，单位为分。
         *
         * @required
         */
        private int total;

        /**
         * 货币类型
         * CNY：人民币，境内商户号仅支持人民币
         *
         * @mock CNY
         */
        private String currency;

    }

    @Data
    @Builder
    public static class Payer {

        /**
         * 用户标识
         * 用户在直连商户appid下的唯一标识。
         *
         * @mock oUpF8uMuAJO_M2pxb1Q9zNjWeS6o
         * @required
         */
        private String openid;

    }

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
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
         *
         * @mock 600
         * @required
         */
        @JsonProperty("cost_price")
        private Integer costPrice;
        /**
         * 单品列表
         */
        @JsonProperty("goods_detail")
        private List<GoodsDetail> goodsDetail;


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
             *
             * @required
             */
            @JsonProperty("merchant_goods_id")
            private String merchantGoodsId;
            /**
             * 商品单价
             * 商品单价，单位为分
             *
             * @required
             */
            @JsonProperty("unit_price")
            private int unitPrice;

        }
    }

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SceneInfo {

        /**
         * 门店信息
         */
        @JsonProperty("store_info")
        private StoreInfo storeInfo;
        /**
         * 商户端设备号
         * 商户端设备号（门店号或收银设备ID）。
         *
         * @mock 013467007045764
         */
        @JsonProperty("device_id")
        private String deviceId;
        /**
         * 用户终端IP
         * 调用微信支付API的机器IP，支持IPv4和IPv4两种格式的IP地址。
         *
         * @mock 14.23.150.211
         * @required
         */
        @JsonProperty("payer_client_ip")
        private String payerClientIp;

        @Data
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class StoreInfo {

            /**
             * 详细地址
             *
             * @required
             */
            private String address;
            /**
             * 地区编码
             * 地区编码，详细请见省市区编号对照表(https://pay.weixin.qq.com/wiki/doc/apiv3/wxpay/ecommerce/applyments/chapter4_1.shtml)
             *
             * @required
             */
            @JsonProperty("area_code")
            private String areaCode;
            /**
             * 门店名称
             *
             * @required
             */
            private String name;
            /**
             * 门店编号
             * 商户侧门店编号
             */
            private String id;

        }
    }

}
