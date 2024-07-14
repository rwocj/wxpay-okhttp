package top.rwocj.wx.pay.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.rwocj.wx.pay.core.SignHelper;
import top.rwocj.wx.pay.core.SignVerifier;
import top.rwocj.wx.pay.core.WxPayV3OkHttpInterceptor;
import top.rwocj.wx.pay.core.WxPayValidator;
import top.rwocj.wx.pay.properties.WxPayProperties;
import top.rwocj.wx.pay.service.WxPayV3Service;
import top.rwocj.wx.pay.util.OkHttpClientBuilderUtil;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.stream.Collectors;

@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(JacksonAutoConfiguration.class)
public class WxPayV3AutoConfiguration {

    private final ObjectMapper objectMapper;

    public WxPayV3AutoConfiguration(ObjectProvider<ObjectMapper> objectProvider) {
        this.objectMapper = objectProvider.getIfAvailable(ObjectMapper::new);
    }

    @Bean
    @ConfigurationProperties(prefix = "wx.pay")
    @ConditionalOnMissingBean
    public WxPayProperties wxPayProperties() {
        return new WxPayProperties();
    }

    @Bean
    @ConditionalOnMissingBean
    public WxPayV3Service wxPayV3Service(ObjectProvider<OkHttpClientCustomizer> okHttpClientCustomizerObjectProvider,
                                         WxPayProperties wxPayProperties) throws Exception {
        KeyStore ks = KeyStore.getInstance("PKCS12");
        char[] charArray = wxPayProperties.getMchId().toCharArray();
        ks.load(wxPayProperties.getP12Path().getInputStream(), charArray);
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
                okHttpClientCustomizerObjectProvider.orderedStream().collect(Collectors.toList())).build();
        //微信通知验证
        SignVerifier signVerifier = new SignVerifier(wxPayProperties.getApiV3Key().getBytes(StandardCharsets.UTF_8), okHttpClient);
        WxPayValidator wxPayValidator = new WxPayValidator(signVerifier);
        return new WxPayV3Service(okHttpClient, objectMapper, wxPayValidator, wxPayProperties, signHelper);
    }

}
