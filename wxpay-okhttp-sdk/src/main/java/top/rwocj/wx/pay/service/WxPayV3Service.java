package top.rwocj.wx.pay.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.rwocj.wx.pay.core.*;
import top.rwocj.wx.pay.dto.*;
import top.rwocj.wx.pay.enums.OrderType;
import top.rwocj.wx.pay.property.WxPayProperties;
import top.rwocj.wx.pay.util.AesUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;

public class WxPayV3Service {

    private final static Logger log = LoggerFactory.getLogger(WxPayV3Service.class);

    /**
     * 普通商户下单url
     */
    public final static String ORDER_BASE_URL = "https://api.mch.weixin.qq.com/v3/pay/transactions/";
    /**
     * 申请退款url
     */
    public final static String REFUND_URL = "https://api.mch.weixin.qq.com/v3/refund/domestic/refunds";

    protected final OkHttpClient okHttpClient;

    protected final ObjectMapper objectMapper;

    protected final WxPayValidator wxPayValidator;

    protected final WxPayProperties wxPayProperties;

    protected final SignHelper signHelper;

    protected final AesUtil aesUtil;

    public WxPayV3Service(OkHttpClient okHttpClient, ObjectMapper objectMapper,
                          WxPayValidator wxPayValidator, WxPayProperties wxPayProperties,
                          SignHelper signHelper) {
        this.okHttpClient = okHttpClient;
        this.objectMapper = objectMapper;
        this.wxPayValidator = wxPayValidator;
        this.wxPayProperties = wxPayProperties;
        this.signHelper = signHelper;
        this.aesUtil = new AesUtil(wxPayProperties.getApiV3Key().getBytes(StandardCharsets.UTF_8));
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
            OrderType orderType = createOrderRequest.getOrderType();
            String appid = wxPayProperties.getAppIds().get(orderType);
            if (appid == null) {
                throw new WxPayException("未设置" + orderType.getRemark() + "对应的appid");
            }
            createOrderRequest.setAppid(appid);
        }
        if (createOrderRequest.getNotifyUrl() == null) {
            createOrderRequest.setNotifyUrl(wxPayProperties.getNotifyUrl());
        }
        createOrderRequest.setMchId(wxPayProperties.getMchId());
        OrderType orderType = createOrderRequest.getOrderType();
        String url = ORDER_BASE_URL + orderType.getUrl();
        String res = post(url, createOrderRequest);
        try {
            JsonNode jsonNode = objectMapper.readTree(res);
            return orderType.resultFunc.apply(jsonNode);
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
    public WxJSAPICreateOrderRes createJSAPIOrder(WxCreateOrderRequest createOrderRequest) throws WxPayException {
        return signHelper.sign(createOrder(createOrderRequest), wxPayProperties.getAppIds().get(createOrderRequest.getOrderType()));
    }

    /**
     * 关单API
     *
     * @param outTradeId 商户订单号
     * @throws WxPayException 关单失败
     */
    public void closeOrder(String outTradeId) throws WxPayException {
//        Assert.notNull(outTradeId, "商户订单号不能为nll");
        String url = ORDER_BASE_URL + "out-trade-no/" + outTradeId + "/close";
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("mchid", wxPayProperties.getMchId());
        post(url, objectNode);
    }

    /**
     * 查询订单，返回支付结果
     *
     * @param url 请求url，完善的url
     * @return 支付结果
     * @throws WxPayException 查单失败
     */
    public WxPayResult queryOrder(String url) throws WxPayException {
        String result = get(url);
        try {
            return objectMapper.readValue(result, WxPayResult.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 微信支付订单号查询
     *
     * @param transactionsId 微信订单号，不能为空
     * @return 支付结果
     * @throws WxPayException 查单失败
     */
    public WxPayResult queryOrderByTransactionsId(String transactionsId) throws WxPayException {
//        Assert.notNull(transactionsId, "微信订单号不能为nll");
        return queryOrder(ORDER_BASE_URL + "id/" + transactionsId + "?mchid=" + wxPayProperties.getMchId());
    }

    /**
     * 商户订单号查询
     *
     * @param outTradeId 商户订单号，不能为空
     * @return 支付结果
     * @throws WxPayException 查单失败
     */
    public WxPayResult queryOrderByOutTradeId(String outTradeId) throws WxPayException {
//        Assert.notNull(outTradeId, "商户订单号不能为nll");
        return queryOrder(ORDER_BASE_URL + "/out-trade-no/" + outTradeId + "?mchid=" + wxPayProperties.getMchId());
    }

    /**
     * 微信退款
     * <a href="https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_1_9.shtml">申请退款</a>
     *
     * @param refundRequest 退款请求体
     * @return 退款结果
     * @throws WxPayException 退款失败
     */
    public WxRefundResult refund(WxRefundRequest refundRequest) throws WxPayException {
        if (refundRequest.getNotifyUrl() == null && wxPayProperties.getRefundNotifyUrl() != null) {
            refundRequest.setNotifyUrl(wxPayProperties.getRefundNotifyUrl());
        }
        String res = post(REFUND_URL, refundRequest);
        try {
            return objectMapper.readValue(res, WxRefundResult.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 验证微信退款通知，并解密
     *
     * @param request 微信请求
     * @return 解密后的支付结果
     * @throws WxPayException 验签或解密失败
     */
    public WxRefundNoticeResult validateAndDecryptRefundNotification(HttpServletRequest request) throws WxPayException {
        try {
            String body = getRequestBody(request.getInputStream());
            boolean b = wxPayValidator.validate(new HttpServletRequestWxHeaders(request), body);
            if (b) {
                return decryptWxNotice(body, WxRefundNoticeResult.class);
            } else {
                throw new WxPayException("验签不能过，非微信支付团队的消息！");
            }
        } catch (IOException e) {
            throw new WxPayException("验签不能过，非微信支付团队的消息！", e);
        }
    }

    /**
     * 验证微信支付通知，并解密
     *
     * @param request 微信请求
     * @return 解密后的支付结果
     * @throws WxPayException 验签或解密失败
     */
    public WxPayResult validateAndDecryptPayNotification(HttpServletRequest request) throws WxPayException {
        try {
            String body = getRequestBody(request.getInputStream());
            boolean b = wxPayValidator.validate(new HttpServletRequestWxHeaders(request), body);
            if (b) {
                return decryptWxNotice(body, WxPayResult.class);
            } else {
                throw new WxPayException("验签不能过，非微信支付团队的消息！");
            }
        } catch (IOException e) {
            throw new WxPayException("验签不能过，非微信支付团队的消息！", e);
        }
    }

    /**
     * 解密微信支付或退款通知
     *
     * @param data 微信Post过来的加密的数据
     * @param <T>  the class
     * @return 支付结果
     * @throws WxPayException 处理支付结果失败，如非微信通知的支付结果
     */
    protected <T> T decryptWxNotice(String data, Class<T> tClass) throws WxPayException {
        try {
            JsonNode jsonNode = objectMapper.readTree(data);
            JsonNode resource = jsonNode.get("resource");
            String s = aesUtil.decryptToString(resource.get("associated_data").asText().getBytes(StandardCharsets.UTF_8),
                    resource.get("nonce").asText().getBytes(StandardCharsets.UTF_8), resource.get("ciphertext").asText());
            return objectMapper.readValue(s, tClass);
        } catch (GeneralSecurityException | JsonProcessingException e) {
            throw new WxPayException(data, e);
        }
    }

    /**
     * 通用的GET方法，用于请求其他微信接口
     *
     * @param url 完善的请求地址
     * @return 请求结果
     * @throws WxPayException 请求失败
     */
    public String get(String url) throws WxPayException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        return request(request);
    }

    /**
     * 通用的POST方法，用于请求其他微信接口
     *
     * @param url         完善的请求地址
     * @param requestBody 请求数据，最终会序列化为json
     * @return 请求结果
     * @throws WxPayException 请求失败
     */
    public String post(String url, Object requestBody) throws WxPayException {
        String content = null;
        try {
            content = objectMapper.writeValueAsString(requestBody);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(content, MediaType.parse("application/json;charset=utf-8")))
                .build();
        log.debug("微信请求体：{}", content);
        return request(request);
    }

    protected String request(Request request) throws WxPayException {
        log.debug("微信url：{}", request.url());
        try (Response execute = okHttpClient.newCall(request).execute()) {
            log.debug("微信响应码：{}", execute.code());
            ResponseBody body = execute.body();
            if (execute.isSuccessful()) {
                if (body != null) {
                    String string = body.string();
                    log.debug("微信响应：{}", string);
                    Headers headers = execute.headers();
                    boolean validate = wxPayValidator.validate(new OkHttpWxHeaders(execute), string);
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

    private String getRequestBody(InputStream inputStream) throws WxPayException {
        try {
            byte[] bytes = new byte[1024];
            int bytesRead;
            StringBuilder result = new StringBuilder();
            while ((bytesRead = inputStream.read(bytes)) != -1) {
                result.append(new String(bytes, 0, bytesRead, StandardCharsets.UTF_8));
            }
            return result.toString();
        } catch (IOException e) {
            throw new WxPayException("解析请求失败", e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException ignore) {

            }
        }
    }

}
