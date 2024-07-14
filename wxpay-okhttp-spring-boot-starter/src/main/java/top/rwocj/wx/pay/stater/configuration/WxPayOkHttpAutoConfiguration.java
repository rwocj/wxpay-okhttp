package top.rwocj.wx.pay.stater.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import top.rwocj.wx.pay.configuration.OkHttpClientCustomizer;
import top.rwocj.wx.pay.property.WxPayProperties;
import top.rwocj.wx.pay.service.WxPayV3Service;
import top.rwocj.wx.pay.util.WxPayV3ServiceFactory;

import java.util.stream.Collectors;

@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(JacksonAutoConfiguration.class)
@ConditionalOnMissingBean
public class WxPayOkHttpAutoConfiguration implements ResourceLoaderAware {

    private final ObjectMapper objectMapper;

    private ResourceLoader resourceLoader;

    public WxPayOkHttpAutoConfiguration(ObjectProvider<ObjectMapper> objectProvider) {
        this.objectMapper = objectProvider.getIfAvailable(ObjectMapper::new);
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
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
        return WxPayV3ServiceFactory.create(okHttpClientCustomizerObjectProvider.orderedStream().collect(Collectors.toList()),
                resourceLoader.getResource(wxPayProperties.getP12Path()).getInputStream(),
                objectMapper, wxPayProperties
        );
    }

}
