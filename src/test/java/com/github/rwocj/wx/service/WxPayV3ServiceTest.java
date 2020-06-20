package com.github.rwocj.wx.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.rwocj.wx.base.*;
import com.github.rwocj.wx.dto.WxCreateOrderRequest;
import com.github.rwocj.wx.enums.OrderType;
import com.github.rwocj.wx.properties.WxPayProperties;
import com.github.rwocj.wx.properties.WxProperties;
import com.github.rwocj.wx.util.OkHttpClientBuilderUtil;
import com.github.rwocj.wx.util.PemUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

class WxPayV3ServiceTest {

    private static WxPayV3Service wxPayV3Service;

    private static WxProperties wxProperties;

    @Test
    void nativeCreateOrder() throws WxPayException {
        WxCreateOrderRequest request = new WxCreateOrderRequest();
        request.setAppid(wxProperties.getAppId());
        request.setOrderType(OrderType.jsapi);
        request.setDescription("测试商品");
        request.setOutTradeNo("12313123123");
        request.setAmount(WxCreateOrderRequest.Amount.builder().total(100).build());
        request.setPayer(WxCreateOrderRequest.Payer.builder().openid("oT5Pk5GxcjYfGQ-MCLi0QRp45Quc").build());
        String prepay_id = wxPayV3Service.nativeCreateOrder(request);
        Assertions.assertNotNull(prepay_id);

    }

    @BeforeAll
    static void before() throws IOException {
        ResourceBundle bundle = ResourceBundle.getBundle("application-test");
        Assertions.assertDoesNotThrow(() -> MissingResourceException.class, "未找到application-test.properties文件！");

        wxProperties = new WxProperties();
        wxProperties.setAppId(bundle.getString("wx.app-id"));
        Assertions.assertNotNull(wxProperties.getAppId(), "appid不能为空");

        WxPayProperties wxPayProperties = new WxPayProperties();
        wxPayProperties.setMchId(bundle.getString("wx.pay.mchId"));
        wxPayProperties.setCertificateSerialNo(bundle.getString("wx.pay.certificate-serial-no"));
        wxPayProperties.setApiV3Key(bundle.getString("wx.pay.api-v3-key"));
        wxPayProperties.setPrivateKeyPath(bundle.getString("wx.pay.private-key-path"));
        wxPayProperties.setNotifyUrl(bundle.getString("wx.pay.notify-url"));
        wxPayProperties.setRefundNotifyUrl(bundle.getString("wx.pay.refund-notify-url"));
        wxProperties.setPay(wxPayProperties);

        Assertions.assertNotNull(wxPayProperties.getMchId(), "商户id不能为空");
        Assertions.assertNotNull(wxPayProperties.getCertificateSerialNo(), "支付证书序列号不能为空");
        Assertions.assertNotNull(wxPayProperties.getApiV3Key(), "v3密钥不能为空");
        Assertions.assertNotNull(wxPayProperties.getPrivateKeyPath(), "私钥文件路不能为空");
        Assertions.assertNotNull(wxPayProperties.getNotifyUrl(), "支付通知url不能为空");

        try {
            WxPayV3OkHttpInterceptor wxPayV3OkHttpInterceptor = new WxPayV3OkHttpInterceptor(wxPayCredentials(wxPayProperties), wxPayProperties.getMchId(), wxPayProperties.getCertificateSerialNo());
            wxPayV3Service = new WxPayV3Service(OkHttpClientBuilderUtil.wxPayOkHttpClient(wxPayV3OkHttpInterceptor).build(),
                    new ObjectMapper(), wxPayValidator(wxPayProperties, wxPayV3OkHttpInterceptor), wxProperties, wxPaySign(wxPayProperties));
        } catch (IOException e) {
            System.out.println("初始化wxPayV3Service失败!");
            throw e;
        }

    }

    public static Sign wxPaySign(WxPayProperties wxPayProperties) throws IOException {
        PrivateKey privateKey = PemUtil.loadPrivateKey(new ClassPathResource(wxPayProperties.getPrivateKeyPath().replace("classpath:", "")).getInputStream());
        return new DefaultV3Sign(privateKey);
    }

    public static Credentials wxPayCredentials(WxPayProperties wxPayProperties) throws IOException {
        return new DefaultV3Credentials(wxPaySign(wxPayProperties));
    }

    public static Validator wxPayValidator(WxPayProperties wxPayProperties, WxPayV3OkHttpInterceptor wxPayV3OkHttpInterceptor) {
        DefaultCertificatesVerifier defaultCertificatesVerifier = new DefaultCertificatesVerifier(wxPayProperties.getApiV3Key().getBytes(StandardCharsets.UTF_8),
                OkHttpClientBuilderUtil.wxPayOkHttpClient(wxPayV3OkHttpInterceptor).build());
        return new DefaultV3Validator(defaultCertificatesVerifier);
    }

}
