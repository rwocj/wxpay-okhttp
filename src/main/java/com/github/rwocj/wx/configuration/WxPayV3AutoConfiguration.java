package com.github.rwocj.wx.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.rwocj.wx.base.*;
import com.github.rwocj.wx.properties.WxPayProperties;
import com.github.rwocj.wx.properties.WxProperties;
import com.github.rwocj.wx.service.WxPayV3Service;
import com.github.rwocj.wx.util.OkHttpClientBuilderUtil;
import com.github.rwocj.wx.util.PemUtil;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;

@Configuration
@EnableConfigurationProperties(WxProperties.class)
public class WxPayV3AutoConfiguration {

    private final WxProperties wxProperties;

    private final WxPayProperties wxPayProperties;

    private final ObjectMapper objectMapper;

    public WxPayV3AutoConfiguration(WxProperties wxProperties, ObjectProvider<ObjectMapper> objectProvider) {
        this.wxProperties = wxProperties;
        this.wxPayProperties = wxProperties.getPay();
        this.objectMapper = objectProvider.getIfAvailable(ObjectMapper::new);
    }

    @Bean
    @ConditionalOnMissingBean
    public Sign wxPaySign() throws IOException {
        PrivateKey privateKey = PemUtil.loadPrivateKey(new ClassPathResource(wxPayProperties.getPrivateKeyPath().replace("classpath:", "")).getInputStream());
        return new DefaultV3Sign(privateKey);
    }

    @Bean
    @ConditionalOnMissingBean
    public Credentials wxPayCredentials(Sign sign) {
        return new DefaultV3Credentials(sign);
    }

    @Bean
    @ConditionalOnMissingBean
    public WxPayV3OkHttpInterceptor wxPayInterceptor(Credentials credentials) {
        return new WxPayV3OkHttpInterceptor(credentials, wxPayProperties.getMchId(), wxPayProperties.getCertificateSerialNo());
    }

    @Bean
    @ConditionalOnMissingBean
    public Validator wxPayValidator(WxPayV3OkHttpInterceptor wxPayV3OkHttpInterceptor) {
        DefaultCertificatesVerifier defaultCertificatesVerifier
                = new DefaultCertificatesVerifier(wxPayProperties.getApiV3Key().getBytes(StandardCharsets.UTF_8),
                OkHttpClientBuilderUtil.wxPayOkHttpClient(wxPayV3OkHttpInterceptor).build());
        return new DefaultV3Validator(defaultCertificatesVerifier);
    }

    @Bean
    @ConditionalOnMissingBean
    public WxPayV3Service wxPayV3Service(WxPayV3OkHttpInterceptor wxPayV3OkHttpInterceptor,
                                         Validator validator,
                                         Sign sign) {
        return new WxPayV3Service(OkHttpClientBuilderUtil.wxPayOkHttpClient(wxPayV3OkHttpInterceptor).build(),
                objectMapper, validator, wxProperties, sign);
    }


}
