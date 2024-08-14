package top.rwocj.wx.pay.common;

import lombok.RequiredArgsConstructor;
import okhttp3.HttpUrl;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;

/**
 * 签名相关
 * <a href="https://wechatpay-api.gitbook.io/wechatpay-api-v3/qian-ming-zhi-nan-1/qian-ming-sheng-cheng">参考文档</a>
 *
 * @author lqb
 * @since 2024/7/14 15:15
 **/
@RequiredArgsConstructor
public class SignHelper {

    private final PrivateKey privateKey;

    /**
     * 认证类型
     * 目前为WECHATPAY2-SHA256-RSA2048
     */
    public static final String AUTHORIZATION_TYPE = "WECHATPAY2-SHA256-RSA2048";

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
    public String getToken(String method, String url, String body, String machId, String certificateSerialNo) {
        if (body == null) {
            body = "";
        }
        String nonceStr = UUID.randomUUID().toString().replaceAll("-", "");
        long timestamp = System.currentTimeMillis() / 1000;
        String message = buildMessage(method, Objects.requireNonNull(HttpUrl.parse(url)), timestamp, nonceStr, body);
        String signature = sign(message.getBytes(StandardCharsets.UTF_8));
        return "mchid=\"" + machId + "\","
                + "nonce_str=\"" + nonceStr + "\","
                + "timestamp=\"" + timestamp + "\","
                + "serial_no=\"" + certificateSerialNo + "\","
                + "signature=\"" + signature + "\"";
    }

    public String sign(byte[] message) {
        try {
            Signature sign = Signature.getInstance("SHA256withRSA");
            sign.initSign(privateKey);
            sign.update(message);
            return Base64.getEncoder().encodeToString(sign.sign());
        } catch (SignatureException | InvalidKeyException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private String buildMessage(String method, HttpUrl url, long timestamp, String nonceStr, String body) {
        String canonicalUrl = url.encodedPath();
        if (url.encodedQuery() != null) {
            canonicalUrl += "?" + url.encodedQuery();
        }
        return method + "\n"
                + canonicalUrl + "\n"
                + timestamp + "\n"
                + nonceStr + "\n"
                + body + "\n";
    }
}
