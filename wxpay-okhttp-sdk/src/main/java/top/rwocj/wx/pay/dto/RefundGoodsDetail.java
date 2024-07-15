package top.rwocj.wx.pay.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 退款商品信息
 *
 * @author lqb
 * @since 2024/7/14 11:21
 **/
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RefundGoodsDetail {

    /**
     * 商户侧商品编码
     * 由半角的大小写字母、数字、中划线、下划线中的一种或几种组成
     * 示例值：1217752501201407033233368018
     */
    @JsonProperty("merchant_goods_id")
    private String merchantGoodsId;
    /**
     * 微信支付商品编码
     * 微信支付定义的统一商品编号（没有可不传）
     * 示例值：1001
     */
    @JsonProperty("wechatpay_goods_id")
    private String wechatpayGoodsId;
    /**
     * 商品名称
     * 商品的实际名称
     * 示例值：iPhone6s 16G
     */
    @JsonProperty("goods_name")
    private String goodsName;
    /**
     * 商品单价
     * 商品单价金额，单位为分
     * 示例值：528800
     */
    @JsonProperty("unit_price")
    private Integer unitPrice;
    /**
     * 商品退款金额
     * 商品退款金额，单位为分
     * 示例值：528800
     */
    @JsonProperty("refund_amount")
    private Integer refundAmount;
    /**
     * 商品退货数量
     * 单品的退款数量
     * 示例值：1
     */
    @JsonProperty("refund_quantity")
    private Integer refundQuantity;

    public RefundGoodsDetail(String merchantGoodsId, Integer unitPrice, Integer refundAmount, Integer refundQuantity) {
        this.merchantGoodsId = merchantGoodsId;
        this.unitPrice = unitPrice;
        this.refundAmount = refundAmount;
        this.refundQuantity = refundQuantity;
    }
}
