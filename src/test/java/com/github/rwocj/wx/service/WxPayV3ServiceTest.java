package com.github.rwocj.wx.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.rwocj.wx.base.WxPayException;
import com.github.rwocj.wx.dto.WxCreateOrderRequest;
import com.github.rwocj.wx.dto.WxPayResult;
import com.github.rwocj.wx.dto.WxRefundRequest;
import com.github.rwocj.wx.enums.OrderType;
import com.github.rwocj.wx.properties.WxProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.Resource;
import java.util.ResourceBundle;
import java.util.UUID;

@SpringBootTest
@ActiveProfiles("test")
class WxPayV3ServiceTest {

    @Resource
    private WxPayV3Service wxPayV3Service;

    @Resource
    private WxProperties wxProperties;

    @Test
    void nativeCreateOrder() throws WxPayException {
        WxCreateOrderRequest request = new WxCreateOrderRequest();
        request.setOrderType(OrderType.jsapi);
        request.setDescription("测试商品");
        request.setOutTradeNo(UUID.randomUUID().toString().replaceAll("-", ""));
        request.setAmount(WxCreateOrderRequest.Amount.builder().total(10).build());
        request.setPayer(WxCreateOrderRequest.Payer.builder().openid("oT5Pk5GxcjYfGQ-MCLi0QRp45Quc").build());
        String prepay_id = wxPayV3Service.createOrder(request);
        System.out.println("prepay_id:" + prepay_id);
        Assertions.assertNotNull(prepay_id);

    }

    @Test
    @Disabled
    void refund() throws WxPayException {
        WxRefundRequest refundRequest = new WxRefundRequest();
        refundRequest.setNotifyUrl(wxProperties.getPay().getRefundNotifyUrl());
        refundRequest.setAmount(WxRefundRequest.Amount.builder().refund(1).total(1).build());
        refundRequest.setOutRefundNo(UUID.randomUUID().toString());
        wxPayV3Service.refund(refundRequest);
    }

    @BeforeAll
    static void before() {
        Assertions.assertDoesNotThrow(() -> ResourceBundle.getBundle("application-test"), "未找到application-test.properties文件！");
    }

    @Test
    void queryOrderByTransactionsId() throws WxPayException, JsonProcessingException {
        WxPayResult wxPayResult = wxPayV3Service.queryOrderByTransactionsId("4200000598202006225181965671");
        System.out.println(new ObjectMapper().writeValueAsString(wxPayResult));
    }

    @Test
    void queryOrderByOutTradeId() throws WxPayException, JsonProcessingException {
        WxPayResult wxPayResult = wxPayV3Service.queryOrderByOutTradeId("1000000020200622102353");
        System.out.println(new ObjectMapper().writeValueAsString(wxPayResult));
    }
}
