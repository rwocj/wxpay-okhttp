package top.rwocj.wx.pay.vehicle.utils;

import java.io.IOException;

/**
 * @author lqb
 * @since 2024/8/13 14:28
 **/
public interface CommonUtil {

    static boolean isGzipCompressed(byte[] bytes) throws IOException {
        if (bytes == null || bytes.length < 2) {
            return false;
        }
        return bytes[0] == (byte) 31 && bytes[1] == (byte) 139;
    }
}
