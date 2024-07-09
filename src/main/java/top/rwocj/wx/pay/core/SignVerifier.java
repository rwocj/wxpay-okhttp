package top.rwocj.wx.pay.core;

/**
 * 签名验证
 */
public interface SignVerifier {

    boolean verify(String serialNumber, byte[] message, String signature);

}
