package com.github.rwocj.wx.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.rwocj.wx.base.*;
import com.github.rwocj.wx.dto.JSAPICreateOrderRes;
import com.github.rwocj.wx.dto.WxCreateOrderRequest;
import com.github.rwocj.wx.enums.OrderType;
import com.github.rwocj.wx.properties.WxProperties;
import com.github.rwocj.wx.util.SignUtil;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@AllArgsConstructor
@Slf4j
public class WxPayV3Service {

    final static String ORDER_URL = "https://api.mch.weixin.qq.com/v3/pay/transactions/";

    private final OkHttpClient okHttpClient;

    private final ObjectMapper objectMapper;

    private final Validator validator;

    private final WxProperties wxProperties;

    private final Sign sign;

    private final org.springframework.validation.Validator hibernateValidator;

    /**
     * 原生下单，得到prepay_id,适用app/h5/jsapi/navtive
     *
     * @param createOrderRequest 请求体
     * @return 预下单id, prepay_id
     * @throws WxPayException 下单失败
     */
    @SneakyThrows(JsonProcessingException.class)
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

        validateOrderRequest(createOrderRequest);

        OrderType orderType = createOrderRequest.getOrderType();
        Request request = new Request.Builder()
                .url(ORDER_URL + orderType.getUrl())
                .post(RequestBody.create(MediaType.parse("application/json;charset=utf-8"),
                        objectMapper.writeValueAsString(createOrderRequest)))
                .build();
        try {
            Response execute = okHttpClient.newCall(request).execute();
            ResponseBody body = execute.body();

            if (body != null) {
                String string = body.string();
                log.debug("下单请求响应：{}", string);
                if (execute.isSuccessful()) {
                    Headers headers = execute.headers();
                    boolean validate = validator.validate(new OkHttpWxHeaders(execute), string);
                    if (!validate) {
                        throw new WxPayException(String.format("验证响应失败!:响应体：%s,响应headers:%s", string, buildHeader(headers)));
                    } else {
                        return objectMapper.readTree(string).get("prepay_id").asText();
                    }
                } else {
                    throw new WxPayException(string);
                }
            } else {
                throw new WxPayException("下单请求响应为空!");
            }
        } catch (IOException e) {
            throw new WxPayException("微信支付下单失败：", e);
        }

    }

    /**
     * jsapi下单，封装jsapi调用支付需要的参数
     * https://pay.weixin.qq.com/wiki/doc/apiv3/wxpay/pay/transactions/chapter3_8.shtml
     */
    public JSAPICreateOrderRes createJSAPIOrder(WxCreateOrderRequest createOrderRequest) throws WxPayException {
        createOrderRequest.setOrderType(OrderType.jsapi);
        return SignUtil.sign(createOrder(createOrderRequest), wxProperties.getAppId(), sign);
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

    private void validateOrderRequest(Object target) throws WxPayException {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(target, target.getClass().getSimpleName(), false, 0);
        ValidationUtils.invokeValidator(hibernateValidator, target, errors);
        if (errors.hasErrors()) {
            List<ObjectError> allErrors = errors.getAllErrors();
            throw new WxPayException("支付请求参数有误：" + allErrors.stream().map(objectError -> {
                FieldError fieldError = (FieldError) objectError;
                return new ValidateResult(fieldError.getObjectName(), fieldError.getField(), fieldError.getDefaultMessage(), fieldError.getRejectedValue());
            }).collect(toList()).toString());
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
