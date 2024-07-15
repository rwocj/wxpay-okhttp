package top.rwocj.wx.pay.core;

import okhttp3.OkHttpClient;

/**
 * @author lqb
 * @since 2024/7/5 11:33
 **/
public interface OkHttpClientCustomizer {

    void customize(OkHttpClient.Builder builder);
}
