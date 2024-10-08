package top.rwocj.wx.pay.common;

import lombok.Getter;
import lombok.Setter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 签名验证
 *
 * @author lqb
 */
public class SignVerifier {

    private final static Logger log = LoggerFactory.getLogger(SignVerifier.class);

    private final Map<BigInteger, X509Certificate> certificates = new HashMap<>();

    //证书下载地址
    private static final String CertDownloadPath = "https://api.mch.weixin.qq.com/v3/certificates";

    //上次更新时间
    private volatile Instant instant;

    //证书更新间隔时间，单位为分钟
    @Setter
    private int minutesInterval = TimeInterval.TwelveHours.minutes;

    private final byte[] apiV3Key;

    private final OkHttpClient okHttpClient;

    private final ReentrantLock lock = new ReentrantLock();

    public SignVerifier(byte[] apiV3Key, OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
        this.apiV3Key = apiV3Key;
    }

    public boolean verify(String serialNumber, byte[] message, String signature) {
        autoUpdateCert(serialNumber);
        BigInteger val = new BigInteger(serialNumber, 16);
        return certificates.containsKey(val) && verify(certificates.get(val), message, signature);
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

    private void autoUpdateCert(String serialNumber) {
        BigInteger val = new BigInteger(serialNumber == null ? "0" : serialNumber, 16);
        if (instant == null
                || Duration.between(instant, Instant.now()).toMinutes() >= minutesInterval || !certificates.containsKey(val)) {
            if (lock.tryLock()) {
                try {
                    Request request = new Request.Builder().addHeader("Accept", "*/*").url(CertDownloadPath).get().build();
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
                        log.error("下载证书失败：{}", body == null ? null : body.string());
                    }
                    execute.close();
                    //更新时间
                    instant = Instant.now();
                } catch (GeneralSecurityException | IOException e) {
                    log.warn("Auto update cert failed, exception = " + e);
                } finally {
                    lock.unlock();
                }
            }
        }

    }

    //时间间隔枚举，支持一小时、六小时以及十二小时
    @Getter
    public enum TimeInterval {

        OneHour(60), SixHours(60 * 6), TwelveHours(60 * 12);

        private final int minutes;

        TimeInterval(int minutes) {
            this.minutes = minutes;
        }
    }
}
