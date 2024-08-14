package top.rwocj.wx.pay.vehicle.utils;

import lombok.experimental.UtilityClass;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.Security;
import java.util.Base64;

/**
 * @author lqb
 * @since 2024/7/23 15:49
 **/
@UtilityClass
public class AESUtils {

    // 算法名称
    static final String KEY_ALGORITHM = "AES";
    // 加解密算法/模式/填充方式
    static final String ALGORITHM_STR = "AES/ECB/PKCS7Padding";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * AES解密
     *
     * @param base64EncodeStr base64位密文
     * @param secret          长度为32的AES密钥
     * @return 解密后的明文
     */
    public static byte[] decodeToByte(String base64EncodeStr, String secret) {
        if (base64EncodeStr == null) {
            return null;
        }
        try {
            Key key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), KEY_ALGORITHM);
            // 加密
            Cipher cipher = Cipher.getInstance(ALGORITHM_STR, "BC");
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(Base64.getDecoder().decode(base64EncodeStr));
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}
