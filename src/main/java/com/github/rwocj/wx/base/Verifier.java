package com.github.rwocj.wx.base;

/**
 * 签名验证
 */
public interface Verifier {

    boolean verify(String serialNumber, byte[] message, String signature);

}
