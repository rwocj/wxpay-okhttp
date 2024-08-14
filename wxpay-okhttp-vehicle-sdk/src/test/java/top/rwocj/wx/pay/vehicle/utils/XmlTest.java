package top.rwocj.wx.pay.vehicle.utils;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import top.rwocj.wx.pay.vehicle.dto.UserStateRequest;

/**
 * @author lqb
 * @since 2024/7/18 13:21
 **/
public class XmlTest {

    @Test
    void test() {
        XmlMapper mapper = new XmlMapper();
        UserStateRequest request = new UserStateRequest();
        request.setTradeScene("PARKING");
        request.setOpenId("oUpF8uMuAJO_M2pxb1Q9zNjWeS6o");
        request.setVersion("2.0");
        request.setAppId("wx8888888888888888");
        request.setMchId("100000981");
        request.setNonceStr("5K8264ILTKCH16CQ2502SI8ZNMTM67VS");
        request.setPlateNumber("粤A00000");
        Assertions.assertDoesNotThrow(() -> {
            System.out.println(mapper.writeValueAsString(request));
        });
    }
}
