package top.rwocj.wx.core;

/**
 * 签名验证
 */
public interface SignVerifier {

    boolean verify(String serialNumber, byte[] message, String signature);

}
