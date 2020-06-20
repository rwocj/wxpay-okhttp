package com.github.rwocj.wx.base;

import lombok.AllArgsConstructor;
import okhttp3.HttpUrl;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@AllArgsConstructor
public class DefaultV3Credentials implements Credentials {

    private final Sign sign;

    @Override
    public String getSchema() {
        return "WECHATPAY2-SHA256-RSA2048";
    }

    @Override
    public String getToken(String method, String url, String body, String machId, String certificateSerialNo) {
        if (body == null) {
            body = "";
        }
        String nonceStr = UUID.randomUUID().toString().replaceAll("-", "");
        long timestamp = System.currentTimeMillis() / 1000;
        String message = buildMessage(method, HttpUrl.parse(url), timestamp, nonceStr, body);
        String signature = sign.sign(message.getBytes(StandardCharsets.UTF_8));
        return "mchid=\"" + machId + "\","
            + "nonce_str=\"" + nonceStr + "\","
            + "timestamp=\"" + timestamp + "\","
            + "serial_no=\"" + certificateSerialNo + "\","
            + "signature=\"" + signature + "\"";
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
