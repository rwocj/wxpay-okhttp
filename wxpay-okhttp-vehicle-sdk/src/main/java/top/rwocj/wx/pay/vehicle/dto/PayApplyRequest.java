package top.rwocj.wx.pay.vehicle.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import top.rwocj.wx.pay.common.JacksonUtil;
import top.rwocj.wx.pay.vehicle.enums.HighwaySceneCarType;
import top.rwocj.wx.pay.vehicle.enums.HighwaySceneChannelType;
import top.rwocj.wx.pay.vehicle.enums.TradeScene;

import java.util.Date;

/**
 * 申请扣款请求
 *
 * @author lqb
 * @since 2024/7/18 16:31
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PayApplyRequest extends AbstractRequest {

    /**
     * 商品或支付单简要描述
     */
    @JacksonXmlProperty(localName = "body")
    @JacksonXmlCData
    private String body;

    /**
     * 商品详情,商品名称明细列表
     */
    @JacksonXmlProperty(localName = "detail")
    @JacksonXmlCData
    private String detail;

    /**
     * 附加数据
     * 附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
     */
    @JacksonXmlProperty(localName = "attach")
    @JacksonXmlCData
    private String attach;

    /**
     * 商户订单号
     * 商户系统内部的订单号,32个字符内、可包含字母,
     */
    @JacksonXmlProperty(localName = "out_trade_no")
    @JacksonXmlCData
    private String outTradeNo;

    /**
     * 总金额
     * 订单总金额，单位为分
     */
    @JacksonXmlProperty(localName = "total_fee")
    private Integer totalFee;

    /**
     * 货币类型
     * 符合ISO 4217标准的三位字母代码，默认人民币:CNY
     */
    @JacksonXmlProperty(localName = "fee_type")
    @JacksonXmlCData
    private String feeType;

    /**
     * 终端IP
     * 调用微信支付API的机器IP
     */
    @JacksonXmlProperty(localName = "spbill_create_ip")
    @JacksonXmlCData
    private String spbillCreateIp;

    /**
     * 商品标记
     * 商品标记，代金券或立减优惠功能的参数，说明详见代金券或立减优惠
     */
    @JacksonXmlProperty(localName = "goods_tag")
    @JacksonXmlCData
    private String goodsTag;

    /**
     * 通知地址
     * 接受扣款结果异步回调通知的url
     */
    @JacksonXmlProperty(localName = "notify_url")
    @JacksonXmlCData
    private String notifyUrl;

    /**
     * 交易类型
     * 交易类型PAP-微信委托代扣支付
     */
    @JacksonXmlProperty(localName = "trade_type")
    @JacksonXmlCData
    private String tradeType = "PAP";

    /**
     * 版本号，固定值
     * 3.0：车场停车（PARKING）场景
     * 2.0：其他场景
     */
    @JacksonXmlProperty(localName = "version")
    @JacksonXmlCData
    private String version;

    /**
     * 交易场景
     * 委托代扣的交易场景值，目前支持 ：
     * 1. PARKING：车场停车场景
     * 2. PARKING SPACE；车位停车场景
     * 3. GAS 加油场景
     * 4. HIGHWAY 高速场景
     * 5. BRIDGE 路桥场景
     * 该值催缴时会向微信用户进行展示
     *
     * @see top.rwocj.wx.pay.vehicle.enums.TradeScene
     */
    @JacksonXmlProperty(localName = "trade_scene")
    @JacksonXmlCData
    private String tradeScene;

    /**
     * openid
     * 用户在商户appid或子商户appid下的唯一标识。
     * 在PARKING场景为PROACTIVE时，才会校验车牌与openid的关系。
     * 当传递用户标识时，微信支付将校验用户和车牌绑定关系通过后才允许进行扣费
     * 如下几种情况下该值必填：
     * （1）场景为：PARKING SPACE时，用户标识必填;
     * （2）在PARKING 场景下，deduct_mode字段为PROACTIVE时，用户标识必填。
     */
    @JacksonXmlProperty(localName = "openid")
    @JacksonXmlCData
    private String openId;

    /**
     * 分账标识
     * Y-是，需要分账
     * N-否，不分账
     * 字母要求大写，不传默认不分账
     * 分账详细说明见《分账API》文档
     *
     * @see <a href="https://pay.weixin.qq.com/wiki/doc/api/allocation_sl.php?chapter=24_3&index=3">分账API</a>
     */
    @JacksonXmlProperty(localName = "profit_sharing")
    @JacksonXmlCData
    private String profitSharing;

    /**
     * 场景信息
     * 场景信息值，格式为json，不同业务场景设置不同的值，具体如后面所列。
     */
    @JacksonXmlProperty(localName = "scene_info")
    @JacksonXmlCData
    private String sceneInfo;

    /**
     * @param body           商品或支付单简要描述
     * @param outTradeNo     商户订单号
     * @param totalFee       订单总金额，单位为分
     * @param spbillCreateIp 调用微信支付API的机器IP
     * @param version        版本号，固定值,3.0：车场停车（PARKING）场景,2.0：其他场景
     * @param tradeScene     交易场景,@see top.rwocj.wx.pay.vehicle.enums.TradeScene
     * @param sceneInfo      场景信息
     * @see TradeScene
     */
    public PayApplyRequest(String body, String outTradeNo, Integer totalFee, String spbillCreateIp, String version, String tradeScene, String sceneInfo) {
        this.body = body;
        this.outTradeNo = outTradeNo;
        this.totalFee = totalFee;
        this.spbillCreateIp = spbillCreateIp;
        this.version = version;
        this.tradeScene = tradeScene;
        this.sceneInfo = sceneInfo;
    }

    /**
     * 高速场景
     *
     * @param body           商品或支付单简要描述
     * @param outTradeNo     商户订单号
     * @param totalFee       订单总金额，单位为分
     * @param spbillCreateIp 调用微信支付API的机器IP
     * @param highwayScene   高速场景信息
     */
    public static PayApplyRequest highwayRequest(String body, String outTradeNo, Integer totalFee, String spbillCreateIp, HighwayScene highwayScene) {
        ObjectNode objectNode = JacksonUtil.getObjectMapper().createObjectNode();
        objectNode.putPOJO("scene_info", highwayScene);
        return new PayApplyRequest(body, outTradeNo, totalFee, spbillCreateIp, VERSION_2, TradeScene.HIGHWAY.name(), JacksonUtil.toJsonString(objectNode));
    }

    /**
     * 车场停车场景
     */
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ParkingScene {

        /**
         * 入场时间
         */
        @JsonProperty(value = "start_time")
        @JsonFormat(pattern = "yyyyMMddHHmmss", timezone = "Asia/Shanghai")
        private Date startTime;

        /**
         * 出场时间
         */
        @JsonProperty(value = "end_time")
        @JsonFormat(pattern = "yyyyMMddHHmmss", timezone = "Asia/Shanghai")
        private Date endTime;

        /**
         * 计费的时间长。单位为秒
         */
        @JsonProperty(value = "charging_time")
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private Integer chargingTime;

        /**
         * 车牌号
         * 车牌号。仅包括省份+车牌，不包括特殊字符。
         */
        @JsonProperty("plate_number")
        private String plateNumber;

        /**
         * 停车车辆的类型，可选值：大型车、小型车
         */
        @JsonProperty("car_type")
        private String carType;

        /**
         * 停车场名称
         */
        @JsonProperty("parking_name")
        private String parkingName;

        /**
         * 发起扣费方式
         * 发起扣费方式，枚举值：
         * PROACTIVE：表示用户主动发起的免密支付
         * AUTOPAY：表示用户无感的无感支付
         */
        @JsonProperty("deduct_mode")
        private String deductMode;

        /**
         * 支持的扣费方式
         * 该字段用于控制微信下发给用户的模板消息
         * 枚举值为：
         * DEDUCT_PROACTIVE_ONLY：仅支持免密
         * DEDUCT_AUTOPAY_ONLY：仅支持无感
         * DEDUCT_ALL：支持免密和无感
         */
        @JsonProperty("support_deduct_mode")
        private String supportDeductMode;

        public ParkingScene(Date startTime, Integer chargingTime, String plateNumber, String parkingName, String deductMode, String supportDeductMode) {
            this.startTime = startTime;
            this.chargingTime = chargingTime;
            this.plateNumber = plateNumber;
            this.parkingName = parkingName;
            this.deductMode = deductMode;
            this.supportDeductMode = supportDeductMode;
        }
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class HighwayScene {

        /**
         * 交易时间
         * 即用户进入高速时间，格式为yyyyMMddHHmmss，该值催缴时会向微信用户进行展示
         * 交易时间最大支持90天内的订单
         */
        @JsonProperty(value = "start_time")
        @JsonFormat(pattern = "yyyyMMddHHmmss", timezone = "Asia/Shanghai")
        private Date startTime;

        /**
         * 结束时间
         * 即用户出高速时间，格式为yyyyMMddHHmmss，该值催缴时会向微信用户进行展示
         */
        @JsonProperty(value = "end_time")
        @JsonFormat(pattern = "yyyyMMddHHmmss", timezone = "Asia/Shanghai")
        private Date endTime;

        /**
         * 车牌号
         * 车牌号。仅包括省份+车牌，不包括特殊字符。
         */
        @JsonProperty("plate_number")
        private String plateNumber;

        /**
         * 车辆的类型，可选值：客车、货车
         */
        @JsonProperty("car_type")
        private String carType;

        /**
         * 高速收费入口站的名称
         */
        @JsonProperty("entrance_name")
        private String entranceName;

        /**
         * 高速收费出口站的名称
         */
        @JsonProperty("exit_name")
        private String exitName;

        /**
         * 当前车辆核载人数，纯数字
         */
        @JsonProperty("carrying_capacity")
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private Integer carryingCapacity;

        /**
         * 当前车辆核载人数区间，格式数字-数字
         */
        @JsonProperty("carrying_range")
        private String carryingRange;

        /**
         * 高速通道类型，目前可选：ETC、MTC。商户扣费前必须确认当前车牌的标识属性，用户车牌必须具有该通道标识时，才允许扣费。
         */
        @JsonProperty("channel_type")
        private String channelType;

        public HighwayScene(Date startTime, Date endTime, String plateNumber, String carType, String entranceName, String exitName, String channelType) {
            this.startTime = startTime;
            this.endTime = endTime;
            this.plateNumber = plateNumber;
            this.carType = carType;
            this.entranceName = entranceName;
            this.exitName = exitName;
            this.channelType = channelType;
        }

        public HighwayScene(Date startTime, Date endTime, String plateNumber, HighwaySceneCarType carType, String entranceName, String exitName, HighwaySceneChannelType highwaySceneChannelType) {
            this.startTime = startTime;
            this.endTime = endTime;
            this.plateNumber = plateNumber;
            this.carType = carType.getRemark();
            this.entranceName = entranceName;
            this.exitName = exitName;
            this.channelType = highwaySceneChannelType.name();
        }
    }

}
