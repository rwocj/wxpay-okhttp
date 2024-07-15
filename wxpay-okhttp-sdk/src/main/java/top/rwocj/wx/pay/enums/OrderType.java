package top.rwocj.wx.pay.enums;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

import java.util.function.Function;

@Getter
public enum OrderType {

    app("app", "app下单", (node) -> node.get("prepay_id").asText()),
    jsapi_public("jsapi", "公众号支付", (node) -> node.get("prepay_id").asText()),
    jsapi_mini_program("jsapi", "小程序支付", (node) -> node.get("prepay_id").asText()),
    natives("native", "native下单", (node) -> node.get("code_url").asText()),
    h5("h5", "h5下单", (node) -> node.get("h5_url").asText());

    private final String url;

    private final String remark;

    /**
     * 处理结果
     */
    public final Function<JsonNode, String> resultFunc;

    OrderType(String url, String remark, Function<JsonNode, String> resultFunc) {
        this.url = url;
        this.remark = remark;
        this.resultFunc = resultFunc;
    }
}
