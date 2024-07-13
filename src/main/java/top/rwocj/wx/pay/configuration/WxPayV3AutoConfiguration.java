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
import top.rwocj.wx.pay.core.*;
import top.rwocj.wx.pay.properties.WxPayProperties;
import top.rwocj.wx.pay.service.WxPayV3Service;
import top.rwocj.wx.pay.util.OkHttpClientBuilderUtil;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
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
    public Sign wxPaySign(WxPayProperties wxPayProperties) throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException {
        KeyStore ks = KeyStore.getInstance("PKCS12");
        char[] charArray = wxPayProperties.getMchId().toCharArray();
        ks.load(wxPayProperties.getP12Path().getInputStream(), charArray);
        String keyAlias = null;
        Enumeration<String> aliases = ks.aliases();
        if (aliases.hasMoreElements()) {
            keyAlias = aliases.nextElement();
        }
        PrivateKey privateKey = (PrivateKey) ks.getKey(keyAlias, charArray);
        return new DefaultV3Sign(privateKey);
    }

    @Bean
    @ConditionalOnMissingBean
    public Credentials wxPayCredentials(Sign sign) {
        return new DefaultV3Credentials(sign);
    }

    @Bean
    @ConditionalOnMissingBean
    public WxPayV3OkHttpInterceptor wxPayInterceptor(Credentials credentials, WxPayProperties wxPayProperties) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
        KeyStore ks = KeyStore.getInstance("PKCS12");
        char[] charArray = wxPayProperties.getMchId().toCharArray();
        ks.load(wxPayProperties.getP12Path().getInputStream(), charArray);
        String keyAlias = null;
        Enumeration<String> aliases = ks.aliases();
        if (aliases.hasMoreElements()) {
            keyAlias = aliases.nextElement();
        }
        Certificate certificate = ks.getCertificate(keyAlias);
        BigInteger serialNumber = ((X509Certificate) certificate).getSerialNumber();
        return new WxPayV3OkHttpInterceptor(credentials, wxPayProperties.getMchId(), serialNumber.toString(16));
    }

    @Bean
    @ConditionalOnMissingBean
    public OkHttpClient okHttpClient(WxPayV3OkHttpInterceptor wxPayV3OkHttpInterceptor,
                                     ObjectProvider<OkHttpClientCustomizer> okHttpClientCustomizerObjectProvider) {
        return OkHttpClientBuilderUtil.wxPayOkHttpClient(
                wxPayV3OkHttpInterceptor,
                okHttpClientCustomizerObjectProvider.orderedStream().collect(Collectors.toList())).build();
    }

    @Bean
    @ConditionalOnMissingBean
    public Validator wxPayValidator(OkHttpClient okHttpClient, WxPayProperties wxPayProperties) {
        SignVerifier defaultCertificatesSignVerifier
                = new DefaultCertificatesSignVerifier(wxPayProperties.getApiV3Key().getBytes(StandardCharsets.UTF_8), okHttpClient);
        return new DefaultV3Validator(defaultCertificatesSignVerifier);
    }

    @Bean
    @ConditionalOnMissingBean
    public WxPayV3Service wxPayV3Service(OkHttpClient okHttpClient,
                                         Validator validator,
                                         Sign sign, WxPayProperties wxPayProperties) {
        return new WxPayV3Service(okHttpClient, objectMapper, validator, wxPayProperties, sign);
    }


}
