package com.github.rwocj.wx.base;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.security.*;
import java.util.Base64;

@AllArgsConstructor
public class DefaultV3Sign implements Sign {

    private final PrivateKey privateKey;

    @Override
    @SneakyThrows(value = NoSuchAlgorithmException.class)
    public String sign(byte[] message) {
        Signature sign = Signature.getInstance("SHA256withRSA");
        try {
            sign.initSign(privateKey);
            sign.update(message);
            return Base64.getEncoder().encodeToString(sign.sign());
        } catch (SignatureException | InvalidKeyException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
