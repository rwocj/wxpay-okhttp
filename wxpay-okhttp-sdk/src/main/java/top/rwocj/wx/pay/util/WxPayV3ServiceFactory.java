package top.rwocj.wx.pay.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import okhttp3.OkHttpClient;
import top.rwocj.wx.pay.common.*;
import top.rwocj.wx.pay.core.OkHttpClientCustomizer;
import top.rwocj.wx.pay.property.WxPayProperties;
import top.rwocj.wx.pay.service.WxPayV3Service;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

/**
 * 创建WxPayV3Service
 *
 * @author lqb
 * @since 2024/7/14 16:48
 **/
@UtilityClass
public class WxPayV3ServiceFactory {

    public static WxPayV3Service create(
            InputStream p12InputStream,
            ObjectMapper objectMapper,
            WxPayProperties wxPayProperties) throws Exception {
        return create(Collections.emptyList(), p12InputStream, objectMapper, wxPayProperties);
    }

    public static WxPayV3Service create(List<OkHttpClientCustomizer> okHttpClientCustomizerList,
                                        InputStream p12InputStream,
                                        ObjectMapper objectMapper,
                                        WxPayProperties wxPayProperties) throws Exception {
        CertificateInfo certificateInfo = new CertificateInfo(p12InputStream, wxPayProperties.getMchId());
        //签名助手
        SignHelper signHelper = new SignHelper(certificateInfo.getPrivateKey());
        //初始化OkHttpClient
        WxPayV3OkHttpInterceptor wxPayV3OkHttpInterceptor = new WxPayV3OkHttpInterceptor(signHelper, wxPayProperties.getMchId(), certificateInfo.getSerialNumber());
        OkHttpClient okHttpClient = OkHttpClientBuilderUtil.wxPayOkHttpClient(
                wxPayV3OkHttpInterceptor,
                okHttpClientCustomizerList).build();
        //微信通知验证
        SignVerifier signVerifier = new SignVerifier(wxPayProperties.getApiV3Key().getBytes(StandardCharsets.UTF_8), okHttpClient);
        WxPayValidator wxPayValidator = new WxPayValidator(signVerifier);
        return new WxPayV3Service(okHttpClient, objectMapper, wxPayValidator, wxPayProperties, signHelper);
    }
}
