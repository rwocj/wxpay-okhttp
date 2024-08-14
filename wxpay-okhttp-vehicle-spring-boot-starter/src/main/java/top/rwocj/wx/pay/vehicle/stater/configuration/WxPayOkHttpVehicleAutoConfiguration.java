package top.rwocj.wx.pay.vehicle.stater.configuration;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import top.rwocj.wx.pay.common.CertificateInfo;
import top.rwocj.wx.pay.common.OkHttpClientBuilderUtil;
import top.rwocj.wx.pay.vehicle.core.OkHttpClientCustomizer;
import top.rwocj.wx.pay.vehicle.core.SSLOkHttpClientCustomizer;
import top.rwocj.wx.pay.vehicle.property.WxPayVehicleProperties;
import top.rwocj.wx.pay.vehicle.service.WxPayVehicleService;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(JacksonAutoConfiguration.class)
public class WxPayOkHttpVehicleAutoConfiguration implements ResourceLoaderAware {

    private final XmlMapper xmlMapper;

    private ResourceLoader resourceLoader;

    public WxPayOkHttpVehicleAutoConfiguration(ObjectProvider<XmlMapper> xmlMapperObjectProvider) {
        this.xmlMapper = xmlMapperObjectProvider.getIfAvailable((XmlMapper::new));
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Bean
    @ConfigurationProperties(prefix = "wx.pay.vehicle")
    @ConditionalOnMissingBean
    public WxPayVehicleProperties wxPayVehicleProperties() {
        return new WxPayVehicleProperties();
    }

    @Bean
    @ConditionalOnMissingBean
    public WxPayVehicleService wxPayVehicleService(ObjectProvider<OkHttpClientCustomizer> okHttpClientCustomizerObjectProvider,
                                                   WxPayVehicleProperties wxPayVehicleProperties) throws Exception {
        List<OkHttpClientCustomizer> okHttpClientCustomizers = okHttpClientCustomizerObjectProvider.orderedStream().collect(Collectors.toList());
        OkHttpClient noCertificateRequiredOkHttpClient = OkHttpClientBuilderUtil.wxPayOkHttpClient(null, okHttpClientCustomizers).build();
        OkHttpClient certificateRequiredOkHttpClient = null;
        if (wxPayVehicleProperties.getP12Path() != null) {
            CertificateInfo certificateInfo = new CertificateInfo(resourceLoader.getResource(wxPayVehicleProperties.getP12Path()).getInputStream(), wxPayVehicleProperties.getMchId());
            ArrayList<Consumer<OkHttpClient.Builder>> customizers = new ArrayList<>();
            customizers.add(new SSLOkHttpClientCustomizer(certificateInfo));
            customizers.addAll(okHttpClientCustomizers);
            certificateRequiredOkHttpClient = OkHttpClientBuilderUtil.wxPayOkHttpClient(
                    null,
                    customizers).build();
        }
        return new WxPayVehicleService(noCertificateRequiredOkHttpClient, certificateRequiredOkHttpClient, xmlMapper, wxPayVehicleProperties);
    }

}
