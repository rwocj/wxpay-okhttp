package top.rwocj.wx.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.rwocj.wx.base.*;
import top.rwocj.wx.properties.WxProperties;
import top.rwocj.wx.service.WxPayV3Service;
import top.rwocj.wx.util.OkHttpClientBuilderUtil;
import top.rwocj.wx.util.PemUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.util.stream.Collectors;

@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(JacksonAutoConfiguration.class)
public class WxPayV3AutoConfiguration {

    private final ObjectMapper objectMapper;

    public WxPayV3AutoConfiguration(ObjectProvider<ObjectMapper> objectProvider) {
        this.objectMapper = objectProvider.getIfAvailable(ObjectMapper::new);
    }

    @Bean
    @ConfigurationProperties(prefix = "wx")
    @ConditionalOnMissingBean
    public WxProperties wxProperties() {
        return new WxProperties();
    }

    @Bean
    @ConditionalOnMissingBean
    public Sign wxPaySign(WxProperties wxProperties) throws IOException {
        PrivateKey privateKey = PemUtil.loadPrivateKey(wxProperties.getPay().getPrivateKeyPath().getInputStream());
        return new DefaultV3Sign(privateKey);
    }

    @Bean
    @ConditionalOnMissingBean
    public Credentials wxPayCredentials(Sign sign) {
        return new DefaultV3Credentials(sign);
    }

    @Bean
    @ConditionalOnMissingBean
    public WxPayV3OkHttpInterceptor wxPayInterceptor(Credentials credentials, WxProperties wxProperties) {
        return new WxPayV3OkHttpInterceptor(credentials, wxProperties.getPay().getMchId(),
                wxProperties.getPay().getCertificateSerialNo());
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
    public Validator wxPayValidator(OkHttpClient okHttpClient, WxProperties wxProperties) {
        DefaultCertificatesVerifier defaultCertificatesVerifier
                = new DefaultCertificatesVerifier(wxProperties.getPay().getApiV3Key().getBytes(StandardCharsets.UTF_8), okHttpClient);
        return new DefaultV3Validator(defaultCertificatesVerifier);
    }

    @Bean
    @ConditionalOnMissingBean
    public WxPayV3Service wxPayV3Service(OkHttpClient okHttpClient,
                                         Validator validator,
                                         Sign sign, WxProperties wxProperties) {
        return new WxPayV3Service(okHttpClient, objectMapper, validator, wxProperties, sign);
    }


}
