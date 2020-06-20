package com.github.rwocj.wx.base;

/**
 * 签名接口，因为不只一个地方用到，所以单独提取出来
 */
public interface Sign {

    /**
     * 签名
     *
     * @param message 需要被签名的信息
     * @return 签名后信息
     */
    String sign(byte[] message);
}
