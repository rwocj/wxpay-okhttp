package top.rwocj.wx.pay.common;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * okhttp拦截器，适用于需要api证书的请求
 */
public class WxPayV3OkHttpInterceptor implements Interceptor {

    static final String os = System.getProperty("os.name") + "/" + System.getProperty("os.version");

    static final String version = System.getProperty("java.version");

    private final SignHelper signHelper;

    private final String machId;

    private final String certificateSerialNo;

    public WxPayV3OkHttpInterceptor(SignHelper signHelper, String machId, String certificateSerialNo) {
        this.signHelper = signHelper;
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
                "wxpay-okhttp/%s (%s) Java/%s",
                getClass().getPackage().getImplementationVersion(),
                os,
                version == null ? "Unknown" : version);
        newBuilder.addHeader("User-Agent", userAgent);

    }

    protected void addAuthorization(Request.Builder newBuilder, Request originalRequest) throws IOException {
        String bodyStr = getBodyStr(newBuilder, originalRequest);
        String token = signHelper.getToken(originalRequest.method(), originalRequest.url().toString(), bodyStr, machId, certificateSerialNo);
        newBuilder.header("Authorization", SignHelper.AUTHORIZATION_TYPE + " " + token);
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
                    RequestBody requestBody = RequestBody.create(bodyStr, originalRequestBody.contentType());
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
