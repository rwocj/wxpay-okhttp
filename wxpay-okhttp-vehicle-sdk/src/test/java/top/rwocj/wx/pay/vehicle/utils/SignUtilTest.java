package top.rwocj.wx.pay.vehicle.utils;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.jupiter.api.Assertions;
import top.rwocj.wx.pay.vehicle.dto.UserStateRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author lqb
 * @since 2024/7/18 11:31
 **/
class SignUtilTest {

    static String secret = "12345678901234567890123456789012";

    @org.junit.jupiter.api.Test
    void sign() {
        XmlMapper mapper = new XmlMapper();
        UserStateRequest request = new UserStateRequest();
        request.setTradeScene("PARKING");
        request.setOpenId("oUpF8uMuAJO_M2pxb1Q9zNjWeS6o");
        request.setVersion("2.0");
        request.setAppId("wx8888888888888888");
        request.setMchId("100000981");
        request.setNonceStr("5K8264ILTKCH16CQ2502SI8ZNMTM67VS");
        request.setPlateNumber("粤A00000");
        String sign = SignUtil.sign(request, secret, false);
        assertEquals("7EEB2B7DD9AA3023B40970E101E160FD000A278E5663B2B9662C7CFA846EDB9A", sign);
        request.setSign(sign);
        Assertions.assertDoesNotThrow(() -> System.out.println(mapper.writeValueAsString(request)));

    }
}
