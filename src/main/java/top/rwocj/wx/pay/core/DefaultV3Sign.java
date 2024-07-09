package top.rwocj.wx.pay.core;


import java.security.*;
import java.util.Base64;

public class DefaultV3Sign implements Sign {

    private final PrivateKey privateKey;

    public DefaultV3Sign(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    @Override
    public String sign(byte[] message) {
        try {
            Signature sign = Signature.getInstance("SHA256withRSA");
            sign.initSign(privateKey);
            sign.update(message);
            return Base64.getEncoder().encodeToString(sign.sign());
        } catch (SignatureException | InvalidKeyException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
