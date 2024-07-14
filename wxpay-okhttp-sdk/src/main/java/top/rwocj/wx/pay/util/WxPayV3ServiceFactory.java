package top.rwocj.wx.pay.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import okhttp3.OkHttpClient;
import top.rwocj.wx.pay.configuration.OkHttpClientCustomizer;
import top.rwocj.wx.pay.core.SignHelper;
import top.rwocj.wx.pay.core.SignVerifier;
import top.rwocj.wx.pay.core.WxPayV3OkHttpInterceptor;
import top.rwocj.wx.pay.core.WxPayValidator;
import top.rwocj.wx.pay.property.WxPayProperties;
import top.rwocj.wx.pay.service.WxPayV3Service;

import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Enumeration;
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
        KeyStore ks = KeyStore.getInstance("PKCS12");
        char[] charArray = wxPayProperties.getMchId().toCharArray();
        ks.load(p12InputStream, charArray);
        String keyAlias = null;
        Enumeration<String> aliases = ks.aliases();
        if (aliases.hasMoreElements()) {
            keyAlias = aliases.nextElement();
        }
        PrivateKey privateKey = (PrivateKey) ks.getKey(keyAlias, charArray);
        //签名助手
        SignHelper signHelper = new SignHelper(privateKey);
        //获取证书序列号
        Certificate certificate = ks.getCertificate(keyAlias);
        BigInteger serialNumber = ((X509Certificate) certificate).getSerialNumber();
        //初始化OkHttpClient
        WxPayV3OkHttpInterceptor wxPayV3OkHttpInterceptor = new WxPayV3OkHttpInterceptor(signHelper, wxPayProperties.getMchId(), serialNumber.toString(16));
        OkHttpClient okHttpClient = OkHttpClientBuilderUtil.wxPayOkHttpClient(
                wxPayV3OkHttpInterceptor,
                okHttpClientCustomizerList).build();
        //微信通知验证
        SignVerifier signVerifier = new SignVerifier(wxPayProperties.getApiV3Key().getBytes(StandardCharsets.UTF_8), okHttpClient);
        WxPayValidator wxPayValidator = new WxPayValidator(signVerifier);
        return new WxPayV3Service(okHttpClient, objectMapper, wxPayValidator, wxPayProperties, signHelper);
    }
}
