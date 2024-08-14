package top.rwocj.wx.pay.vehicle.core;

import okhttp3.OkHttpClient;

import java.util.function.Consumer;

/**
 * @author lqb
 * @since 2024/7/5 11:33
 **/
public interface OkHttpClientCustomizer extends Consumer<OkHttpClient.Builder> {

}
