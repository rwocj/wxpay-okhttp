package top.rwocj.wx.pay.common;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * md5加密工具类
 */
public interface MD5Util {

    /**
     * 获取单个文件的MD5值！
     *
     * @param inStr 文件内容
     */
    static String getMD5(String inStr) {
        return inStr == null ? null : getMD5(inStr.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * @param file:要加密的文件
     * @return MD5摘要码
     */
    static String getMD5(File file) {
        try (FileInputStream fis = new FileInputStream(file)) {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[2048];
            int length;
            while ((length = fis.read(buffer)) != -1) {
                md.update(buffer, 0, length);
            }
            byte[] b = md.digest();
            return HexUtil.byte2Hex(b);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static String getMD5(byte[] bytes) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(bytes);
            byte[] b = md.digest();
            return HexUtil.byte2Hex(b);
        } catch (NoSuchAlgorithmException ignore) {
        }
        return null;
    }

}
