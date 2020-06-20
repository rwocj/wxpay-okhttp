package com.github.rwocj.wx.util;

import lombok.experimental.UtilityClass;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

@UtilityClass
public class OkHttpClientBuilderUtil {

    public static OkHttpClient.Builder wxPayOkHttpClient(Interceptor interceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.hostnameVerifier((hostname, session) -> hostname.endsWith(".mch.weixin.qq.com"))
                .readTimeout(6000, TimeUnit.MILLISECONDS)
                .writeTimeout(10000, TimeUnit.MILLISECONDS)
                .connectTimeout(3000, TimeUnit.MILLISECONDS);
        if (interceptor != null) {
            builder.addInterceptor(interceptor);
        }
        return builder;
    }
}
