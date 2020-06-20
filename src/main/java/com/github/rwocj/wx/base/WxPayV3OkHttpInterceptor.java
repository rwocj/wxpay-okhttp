package com.github.rwocj.wx.base;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class WxPayV3OkHttpInterceptor implements Interceptor {

    static final String os = System.getProperty("os.name") + "/" + System.getProperty("os.version");

    static final String version = System.getProperty("java.version");

    private final Credentials credentials;

    private final String machId;

    private final String certificateSerialNo;

    public WxPayV3OkHttpInterceptor(Credentials credentials, String machId, String certificateSerialNo) {
        this.credentials = credentials;
        this.machId = machId;
        this.certificateSerialNo = certificateSerialNo;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request originalRequest = chain.request();

        Request.Builder builder = originalRequest.newBuilder();

        addHeader(builder);

        addAuthorization(builder, originalRequest);

        return chain.proceed(builder.build());
    }

    protected void addHeader(Request.Builder newBuilder) {
        String userAgent = String.format(
            "WechatPay-OkHttpClient/%s (%s) Java/%s",
            getClass().getPackage().getImplementationVersion(),
            os,
            version == null ? "Unknown" : version);
        newBuilder.addHeader("User-Agent", userAgent)
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/json");

    }

    protected void addAuthorization(Request.Builder newBuilder, Request originalRequest) throws IOException {
        String bodyStr = getBodyStr(newBuilder, originalRequest);
        String token = credentials.getToken(originalRequest.method(), originalRequest.url().toString(), bodyStr, machId, certificateSerialNo);
        newBuilder.header("Authorization", credentials.getSchema() + " " + token);
    }

    private String getBodyStr(Request.Builder newBuilder, Request originalRequest) throws IOException {
        String bodyStr = "";
        String method = originalRequest.method();
        if (!"GET".equals(method)) {
            //获得请求体
            RequestBody originalRequestBody = originalRequest.body();
            if (originalRequestBody != null) {
                try (Buffer buffer = new Buffer()) {
                    originalRequestBody.writeTo(buffer);
                    bodyStr = buffer.readString(StandardCharsets.UTF_8);
                    RequestBody requestBody = RequestBody.create(originalRequestBody.contentType(), bodyStr);
                    switch (method) {
                        case "PUT":
                            newBuilder.put(requestBody);
                            break;
                        case "DELETE":
                            newBuilder.delete(requestBody);
                            break;
                        case "PATCH":
                            newBuilder.patch(requestBody);
                            break;
                        default:
                            newBuilder.post(requestBody);
                    }
                }

            }
        }
        return bodyStr;
    }
}
