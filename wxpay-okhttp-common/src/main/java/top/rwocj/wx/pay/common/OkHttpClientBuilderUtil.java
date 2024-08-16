package top.rwocj.wx.pay.common;

import lombok.experimental.UtilityClass;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@UtilityClass
public class OkHttpClientBuilderUtil {

    public static OkHttpClient.Builder wxPayOkHttpClient(Interceptor interceptor, List<? extends Consumer<OkHttpClient.Builder>> customizers) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.hostnameVerifier((hostname, session) -> hostname.endsWith(".weixin.qq.com"))
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(10000, TimeUnit.MILLISECONDS)
                .connectTimeout(3000, TimeUnit.MILLISECONDS);
        if (interceptor != null) {
            builder.addInterceptor(interceptor);
        }
        if (customizers != null) {
            for (Consumer<OkHttpClient.Builder> customizer : customizers) {
                customizer.accept(builder);
            }
        }
        return builder;
    }
}
