package top.rwocj.wx.core;

import org.springframework.lang.NonNull;

/**
 * 获取schema,生成签名，组成Authorization
 * https://wechatpay-api.gitbook.io/wechatpay-api-v3/qian-ming-zhi-nan-1/qian-ming-sheng-cheng
 */
public interface Credentials {

    /**
     * 获取schema
     *
     * @return 目前默认为WECHATPAY2-SHA256-RSA2048
     */
    default String getSchema() {
        return "WECHATPAY2-SHA256-RSA2048";
    }

    /**
     * 生成token
     *
     * @param method              请求方法名，如GET,POST
     * @param url                 请求绝对url，如https://xxx
     * @param body                请求体，GET方法为空
     * @param machId              商户Id
     * @param certificateSerialNo 证书序列号
     * @return 签名信息
     */
    String getToken(@NonNull String method, @NonNull String url, String body, @NonNull String machId, @NonNull String certificateSerialNo);
}
