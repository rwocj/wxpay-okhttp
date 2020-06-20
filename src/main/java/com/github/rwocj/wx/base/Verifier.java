package com.github.rwocj.wx.base;

import java.security.cert.X509Certificate;

/**
 * 签名验证
 */
public interface Verifier {

    boolean verify(String serialNumber, byte[] message, String signature);

}
