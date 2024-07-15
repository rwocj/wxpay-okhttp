package top.rwocj.wx.pay.util;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import top.rwocj.wx.pay.core.OkHttpClientCustomizer;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class OkHttpClientBuilderUtil {

    private OkHttpClientBuilderUtil() {

    }

    public static OkHttpClient.Builder wxPayOkHttpClient(Interceptor interceptor, List<OkHttpClientCustomizer> customizers) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.hostnameVerifier((hostname, session) -> hostname.endsWith(".mch.weixin.qq.com"))
                .readTimeout(6000, TimeUnit.MILLISECONDS)
                .writeTimeout(10000, TimeUnit.MILLISECONDS)
                .connectTimeout(3000, TimeUnit.MILLISECONDS);
        if (interceptor != null) {
            builder.addInterceptor(interceptor);
        }
        if (customizers != null) {
            for (OkHttpClientCustomizer customizer : customizers) {
                customizer.customize(builder);
            }
        }
        return builder;
    }
}
