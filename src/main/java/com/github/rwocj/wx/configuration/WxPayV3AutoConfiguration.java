package com.github.rwocj.wx.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.rwocj.wx.base.*;
import com.github.rwocj.wx.properties.WxPayProperties;
import com.github.rwocj.wx.properties.WxProperties;
import com.github.rwocj.wx.service.WxPayV3Service;
import com.github.rwocj.wx.util.OkHttpClientBuilderUtil;
import com.github.rwocj.wx.util.PemUtil;
import okhttp3.Interceptor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
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
    @ConditionalOnBean(Sign.class)
    @ConditionalOnMissingBean
    public Credentials wxPayCredentials() throws IOException {
        return new DefaultV3Credentials(wxPaySign());
    }

    @Bean
    @ConditionalOnMissingBean
    public Validator wxPayValidator() throws IOException {
        DefaultCertificatesVerifier defaultCertificatesVerifier
                = new DefaultCertificatesVerifier(wxPayProperties.getApiV3Key().getBytes(StandardCharsets.UTF_8),
                OkHttpClientBuilderUtil.wxPayOkHttpClient(wxPayInterceptor()).build());
        return new DefaultV3Validator(defaultCertificatesVerifier);
    }

    @Bean
    public WxPayV3Service wxPayV3Service() throws IOException {
        return new WxPayV3Service(OkHttpClientBuilderUtil.wxPayOkHttpClient(wxPayInterceptor()).build(),
                objectMapper, wxPayValidator(), wxProperties, wxPaySign());
    }

    @Bean
    public Interceptor wxPayInterceptor() throws IOException {
        return new WxPayV3OkHttpInterceptor(wxPayCredentials(), wxPayProperties.getMchId(), wxPayProperties.getCertificateSerialNo());
    }


}