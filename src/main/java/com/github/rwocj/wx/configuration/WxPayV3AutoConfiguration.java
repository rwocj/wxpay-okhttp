package com.github.rwocj.wx.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.rwocj.wx.base.*;
import com.github.rwocj.wx.properties.WxProperties;
import com.github.rwocj.wx.service.WxPayV3Service;
import com.github.rwocj.wx.util.OkHttpClientBuilderUtil;
import com.github.rwocj.wx.util.PemUtil;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.validation.MessageInterpolatorFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;

@Configuration
public class WxPayV3AutoConfiguration {

    private final ObjectMapper objectMapper;

    private final org.springframework.validation.Validator hibernateValidator;

    public WxPayV3AutoConfiguration(ObjectProvider<ObjectMapper> objectProvider,
                                    ObjectProvider<org.springframework.validation.Validator> hibernateValidatorProvider) {
        this.objectMapper = objectProvider.getIfAvailable(ObjectMapper::new);
        this.hibernateValidator = hibernateValidatorProvider.getIfAvailable(() -> {
            LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
            MessageInterpolatorFactory interpolatorFactory = new MessageInterpolatorFactory();
            factoryBean.setMessageInterpolator(interpolatorFactory.getObject());
            return factoryBean;
        });
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
        PrivateKey privateKey = PemUtil.loadPrivateKey(new ClassPathResource(wxProperties.getPay().getPrivateKeyPath().replace("classpath:", "")).getInputStream());
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
    public Validator wxPayValidator(WxPayV3OkHttpInterceptor wxPayV3OkHttpInterceptor, WxProperties wxProperties) {
        DefaultCertificatesVerifier defaultCertificatesVerifier
                = new DefaultCertificatesVerifier(wxProperties.getPay().getApiV3Key().getBytes(StandardCharsets.UTF_8),
                OkHttpClientBuilderUtil.wxPayOkHttpClient(wxPayV3OkHttpInterceptor).build());
        return new DefaultV3Validator(defaultCertificatesVerifier);
    }

    @Bean
    @ConditionalOnMissingBean
    public WxPayV3Service wxPayV3Service(WxPayV3OkHttpInterceptor wxPayV3OkHttpInterceptor,
                                         Validator validator,
                                         Sign sign, WxProperties wxProperties) {
        return new WxPayV3Service(OkHttpClientBuilderUtil.wxPayOkHttpClient(wxPayV3OkHttpInterceptor).build(),
                objectMapper, validator, wxProperties, sign, hibernateValidator);
    }


}
