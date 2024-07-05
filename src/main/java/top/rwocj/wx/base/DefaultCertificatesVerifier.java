package top.rwocj.wx.base;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.rwocj.wx.util.WxPayUtil;

import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class DefaultCertificatesVerifier implements Verifier {

    private final static Logger log = LoggerFactory.getLogger(DefaultCertificatesVerifier.class);

    private final HashMap<BigInteger, X509Certificate> certificates = new HashMap<>();

    //证书下载地址
    private static final String CertDownloadPath = "https://api.mch.weixin.qq.com/v3/certificates";

    //上次更新时间
    private volatile Instant instant;

    //证书更新间隔时间，单位为分钟
    private int minutesInterval = TimeInterval.TwelveHours.minutes;

    private final byte[] apiV3Key;

    private final OkHttpClient okHttpClient;

    private final ReentrantLock lock = new ReentrantLock();

    public DefaultCertificatesVerifier(byte[] apiV3Key, OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
        this.apiV3Key = apiV3Key;
    }

    @Override
    public boolean verify(String serialNumber, byte[] message, String signature) {
        if (instant == null
                || Duration.between(instant, Instant.now()).toMinutes() >= minutesInterval) {
            if (lock.tryLock()) {
                try {
                    autoUpdateCert();
                    //更新时间
                    instant = Instant.now();
                } catch (GeneralSecurityException | IOException e) {
                    log.warn("Auto update cert failed, exception = " + e);
                } finally {
                    lock.unlock();
                }
            }
        }
        BigInteger val = new BigInteger(serialNumber, 16);
        return certificates.containsKey(val) && verify(certificates.get(val), message, signature);
    }

    public void setMinutesInterval(int minutesInterval) {
        this.minutesInterval = minutesInterval;
    }

    private boolean verify(X509Certificate certificate, byte[] message, String signature) {
        try {
            Signature sign = Signature.getInstance("SHA256withRSA");
            sign.initVerify(certificate);
            sign.update(message);
            return sign.verify(Base64.getDecoder().decode(signature));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("当前Java环境不支持SHA256withRSA", e);
        } catch (SignatureException e) {
            throw new RuntimeException("签名验证过程发生了错误", e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException("无效的证书", e);
        }
    }

    private void autoUpdateCert() throws IOException, GeneralSecurityException {
        Request request = new Request.Builder().url(CertDownloadPath).get().build();
        Response execute = okHttpClient.newCall(request).execute();
        ResponseBody body = execute.body();
        if (execute.isSuccessful()) {
            if (body != null) {
                String data = body.string();
                List<X509Certificate> x509Certificates = WxPayUtil.deserializeToCerts(apiV3Key, data);
                for (X509Certificate x509Certificate : x509Certificates) {
                    this.certificates.put(x509Certificate.getSerialNumber(), x509Certificate);
                }
            }
        } else {
            log.error("下载证书失败：{}", body);
        }

        execute.close();
    }

    //时间间隔枚举，支持一小时、六小时以及十二小时
    public enum TimeInterval {
        OneHour(60), SixHours(60 * 6), TwelveHours(60 * 12);

        private final int minutes;

        TimeInterval(int minutes) {
            this.minutes = minutes;
        }

        public int getMinutes() {
            return minutes;
        }
    }
}
