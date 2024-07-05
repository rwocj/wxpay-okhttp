package top.rwocj.wx.enums;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.function.Function;

public enum OrderType {

    app("app", "app下单", (node) -> node.get("prepay_id").asText()),
    jsapi("jsapi", "jsapi下单", (node) -> node.get("prepay_id").asText()),
    nativeS("native", "native下单", (node) -> node.get("code_url").asText()),
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

    public String getUrl() {
        return url;
    }

    public String getRemark() {
        return remark;
    }
}
