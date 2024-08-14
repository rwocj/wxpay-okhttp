package top.rwocj.wx.pay.common;

import lombok.Getter;
import lombok.Setter;

import javax.net.ssl.KeyManagerFactory;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

/**
 * @author lqb
 * @since 2024/7/22 15:26
 **/
@Getter
@Setter
public class CertificateInfo {

    private final PrivateKey privateKey;

    private final String serialNumber;

    private final PublicKey publicKey;

    private final KeyManagerFactory keyManagerFactory;

    private final Certificate certificate;

    public CertificateInfo(InputStream inputStream, String password) throws Exception {
        try {
            KeyStore ks = KeyStore.getInstance("PKCS12");
            char[] charArray = password.toCharArray();
            ks.load(inputStream, charArray);
            keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(ks, charArray);
            String keyAlias = null;
            Enumeration<String> aliases = ks.aliases();
            if (aliases.hasMoreElements()) {
                keyAlias = aliases.nextElement();
            }
            this.privateKey = (PrivateKey) ks.getKey(keyAlias, charArray);
            //获取证书序列号
            certificate = ks.getCertificate(keyAlias);
            this.serialNumber = ((X509Certificate) certificate).getSerialNumber().toString(16);
            this.publicKey = certificate.getPublicKey();
        } finally {
            inputStream.close();
        }
    }

}
