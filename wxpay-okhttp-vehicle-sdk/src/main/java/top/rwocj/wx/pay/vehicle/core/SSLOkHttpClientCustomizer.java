package top.rwocj.wx.pay.vehicle.core;

import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import top.rwocj.wx.pay.common.CertificateInfo;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @author lqb
 * @since 2024/8/14 10:05
 **/
@RequiredArgsConstructor
public class SSLOkHttpClientCustomizer implements OkHttpClientCustomizer {

    private final CertificateInfo certificateInfo;

    @Override
    public void accept(OkHttpClient.Builder builder) {
        try {
            // 初始化SSL上下文
            SSLContext sslContext = SSLContext.getInstance("TLS");
            //api证书keyManager
            KeyManager[] keyManagers = certificateInfo.getKeyManagerFactory().getKeyManagers();
            TrustAllCerts trustAllCerts = new TrustAllCerts();
            sslContext.init(keyManagers, new TrustManager[]{trustAllCerts}, null);
            builder.sslSocketFactory(sslContext.getSocketFactory(), trustAllCerts);
        } catch (Exception e) {
            throw new RuntimeException("初始化ssl客户端失败", e);
        }
    }

    /**
     * 用于信任所有证书
     */
    private static class TrustAllCerts implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }
}
