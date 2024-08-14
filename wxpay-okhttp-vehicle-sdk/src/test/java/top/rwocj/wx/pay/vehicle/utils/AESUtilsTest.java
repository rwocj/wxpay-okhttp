package top.rwocj.wx.pay.vehicle.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * @author lqb
 * @since 2024/7/23 15:50
 **/
class AESUtilsTest {

    @Test
    void decodeToByte() {
        assertDoesNotThrow(() -> {
            AESUtils.decodeToByte("123", "12345");

        });
    }
}
