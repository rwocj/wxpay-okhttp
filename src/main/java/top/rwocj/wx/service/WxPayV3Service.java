package top.rwocj.wx.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import top.rwocj.wx.core.*;
import top.rwocj.wx.dto.*;
import top.rwocj.wx.enums.OrderType;
import top.rwocj.wx.properties.WxProperties;
import top.rwocj.wx.util.AesUtil;
import top.rwocj.wx.util.SignUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;

public class WxPayV3Service {

    private final static Logger log = LoggerFactory.getLogger(WxPayV3Service.class);

    /**
     * 普通商户下单url
     */
    final static String ORDER_URL = "https://api.mch.weixin.qq.com/v3/pay/transactions/";
    /**
     * 电商平台退款url
     */
    final static String REFUND_URL = "https://api.mch.weixin.qq.com/v3/ecommerce/refunds/apply";

    protected final OkHttpClient okHttpClient;

    protected final ObjectMapper objectMapper;

    protected final Validator validator;

    protected final WxProperties wxProperties;

    protected final Sign sign;

    protected final AesUtil aesUtil;

    public WxPayV3Service(OkHttpClient okHttpClient, ObjectMapper objectMapper,
                          Validator validator, WxProperties wxProperties,
                          Sign sign) {
        this.okHttpClient = okHttpClient;
        this.objectMapper = objectMapper;
        this.validator = validator;
        this.wxProperties = wxProperties;
        this.sign = sign;
        this.aesUtil = new AesUtil(wxProperties.getPay().getApiV3Key().getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 普通商户原生下单，得到prepay_id或地址等,适用app/h5/jsapi/native
     * app/jsapi：返回prepay_id
     * h5: 返回h5_url
     * native: 返回code_url
     *
     * @param createOrderRequest 下单请求体
     * @return 预下单id, prepay_id
     * @throws WxPayException 下单失败
     */
    public String createOrder(WxCreateOrderRequest createOrderRequest) throws WxPayException {
        if (createOrderRequest.getAppid() == null) {
            createOrderRequest.setAppid(wxProperties.getAppId());
        }
        if (createOrderRequest.getMchid() == null) {
            createOrderRequest.setMchid(wxProperties.getPay().getMchId());
        }
        if (createOrderRequest.getNotifyUrl() == null) {
            createOrderRequest.setNotifyUrl(wxProperties.getPay().getNotifyUrl());
        }
        OrderType orderType = createOrderRequest.getOrderType();
        String url = ORDER_URL + orderType.getUrl();
        String res = post(url, createOrderRequest);
        try {
            JsonNode jsonNode = objectMapper.readTree(res);
            return orderType.resultFunc.apply(jsonNode);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 电商平台微信退款
     * <a href="https://pay.weixin.qq.com/wiki/doc/apiv3/wxpay/ecommerce/refunds/chapter3_1.shtml">电商平台微信退款接入文档</a>
     *
     * @param refundRequest 退款请求体
     * @return 退款结果
     * @throws WxPayException 退款失败
     */
    public WxRefundRes refund(WxRefundRequest refundRequest) throws WxPayException {
        if (refundRequest.getSpAppid() == null) {
            refundRequest.setSpAppid(wxProperties.getAppId());
        }
        if (refundRequest.getSubMchid() == null) {
            refundRequest.setSubMchid(wxProperties.getPay().getMchId());
        }

        String res = post(REFUND_URL, refundRequest);
        try {
            return objectMapper.readValue(res, WxRefundRes.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * jsapi下单，封装jsapi调用支付需要的参数
     * <a href="https://pay.weixin.qq.com/wiki/doc/apiv3/wxpay/pay/transactions/chapter3_8.shtml">jsapi下单接入文档</a>
     *
     * @param createOrderRequest 下单请求体
     * @return 下单成功后JSAPI调用支付需要的数据
     * @throws WxPayException 下单失败
     */
    public JSAPICreateOrderRes createJSAPIOrder(WxCreateOrderRequest createOrderRequest) throws WxPayException {
        createOrderRequest.setOrderType(OrderType.jsapi);
        return SignUtil.sign(createOrder(createOrderRequest), wxProperties.getAppId(), sign);
    }

    /**
     * 处理微信支付结果，请先验证签名再调用此方法
     *
     * @param data 微信Post过来的加密的数据
     * @return 支付结果
     * @throws WxPayException 处理支付结果失败，如非微信通知的支付结果
     */
    protected WxPayResult buildPayResult(String data) throws WxPayException {
        try {
            JsonNode jsonNode = objectMapper.readTree(data);
            JsonNode resource = jsonNode.get("resource");
            String s = aesUtil.decryptToString(resource.get("associated_data").asText().getBytes(StandardCharsets.UTF_8),
                    resource.get("nonce").asText().getBytes(StandardCharsets.UTF_8), resource.get("ciphertext").asText());
            return objectMapper.readValue(s, WxPayResult.class);
        } catch (GeneralSecurityException | JsonProcessingException e) {
            throw new WxPayException(data, e);
        }
    }

    /**
     * 关单API
     *
     * @param outTradeId 商户订单号
     * @throws WxPayException 关单失败
     */
    public void closeOrder(String outTradeId) throws WxPayException {
        Assert.notNull(outTradeId, "商户订单号不能为nll");
        String url = ORDER_URL + "out-trade-no/" + outTradeId + "/close";
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("mchid", wxProperties.getPay().getMchId());
        post(url, objectNode);
    }

    /**
     * 微信支付订单号查询
     *
     * @param transactionsId 微信订单号，不能为空
     * @return 支付结果
     * @throws WxPayException 查单失败
     */
    public WxPayResult queryOrderByTransactionsId(String transactionsId) throws WxPayException {
        Assert.notNull(transactionsId, "微信订单号不能为nll");
        return queryOrder(ORDER_URL + "id/" + transactionsId + "?mchid=" + wxProperties.getPay().getMchId());
    }

    /**
     * 商户订单号查询
     *
     * @param outTradeId 商户订单号，不能为空
     * @return 支付结果
     * @throws WxPayException 查单失败
     */
    public WxPayResult queryOrderByOutTradeId(String outTradeId) throws WxPayException {
        Assert.notNull(outTradeId, "商户订单号不能为nll");
        return queryOrder(ORDER_URL + "/out-trade-no/" + outTradeId + "?mchid=" + wxProperties.getPay().getMchId());
    }

    private WxPayResult queryOrder(String url) throws WxPayException {
        String result = get(url);
        try {
            return objectMapper.readValue(result, WxPayResult.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 验证微信发送过来的请求，以确保请求来自微信支付
     *
     * @param headers 请求header
     * @param body    请求体
     * @return true表示请求来自微信
     */
    public boolean validateWxRequest(WxHeaders headers, String body) {
        return validator.validate(headers, body);
    }

    public WxPayResult buildPayResult(HttpServletRequest request, String data) throws WxPayException {
        boolean b = validateWxRequest(new HttpServletRequestWxHeaders(request), data);
        if (b) {
            return buildPayResult(data);
        } else {
            throw new WxPayException("验签不能过，非微信支付团队的消息！");
        }
    }

    public String get(String url) throws WxPayException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        log.debug("微信url：{}", url);
        return request(request);
    }


    public String post(String url, Object requestObject) throws WxPayException {
        String content = null;
        try {
            content = objectMapper.writeValueAsString(requestObject);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(MediaType.parse("application/json;charset=utf-8"),
                        content))
                .build();
        log.debug("微信请求体：{}", content);
        return request(request);
    }

    protected String request(Request request) throws WxPayException {
        try (Response execute = okHttpClient.newCall(request).execute()) {
            log.debug("微信响应码：{}", execute.code());
            ResponseBody body = execute.body();
            if (execute.isSuccessful()) {
                if (body != null) {
                    String string = body.string();
                    log.debug("微信响应：{}", string);
                    Headers headers = execute.headers();
                    boolean validate = validator.validate(new OkHttpWxHeaders(execute), string);
                    if (!validate) {
                        throw new WxPayException(String.format("验证响应失败!:响应体：%s,响应headers:%s", string, buildHeader(headers)));
                    } else {
                        return string;
                    }
                } else {
                    return "";
                }
            } else {
                throw new WxPayException(body != null ? body.string() : "请求响应码:" + execute.code());
            }
        } catch (IOException e) {
            throw new WxPayException("微信请求失败：", e);
        }
    }

    private String buildHeader(Headers headers) {
        Map<String, List<String>> stringListMap = headers.toMultimap();
        StringBuilder sb = new StringBuilder();
        stringListMap.forEach((key, value) -> sb.append(key).append("=").append(String.join(",", value)).append(","));
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.lastIndexOf(","));
        }
        return sb.toString();

    }

}
