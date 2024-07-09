package top.rwocj.wx.pay.core;

import okhttp3.HttpUrl;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;

public class DefaultV3Credentials implements Credentials {

    private final Sign sign;

    public DefaultV3Credentials(Sign sign) {
        this.sign = sign;
    }

    @Override
    public String getToken(String method, String url, String body, String machId, String certificateSerialNo) {
        if (body == null) {
            body = "";
        }
        String nonceStr = UUID.randomUUID().toString().replaceAll("-", "");
        long timestamp = System.currentTimeMillis() / 1000;
        String message = buildMessage(method, Objects.requireNonNull(HttpUrl.parse(url)), timestamp, nonceStr, body);
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
