package top.rwocj.wx.pay.vehicle.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import top.rwocj.wx.pay.vehicle.dto.QueryOrderResponse;
import top.rwocj.wx.pay.vehicle.dto.WxHighwayPlateNumberStatusChangeNotify;

/**
 * @author lqb
 * @since 2024/7/18 11:31
 **/
class SignUtilTest {

    static String secret = "Yunnangonglulianwangshoufei12345";

    static XmlMapper mapper = new XmlMapper();

    @Test
    void test() throws JsonProcessingException {
        String text = "<xml><return_code><![CDATA[SUCCESS]]></return_code>\n" +
                "<return_msg><![CDATA[OK]]></return_msg>\n" +
                "<appid><![CDATA[wxb690c7c18ce64bac]]></appid>\n" +
                "<mch_id><![CDATA[1681960559]]></mch_id>\n" +
                "<nonce_str><![CDATA[be5T03f3rn9YzKGJvdguIUId5WRkGCIN]]></nonce_str>\n" +
                "<sign><![CDATA[B3AD2544E6F10DE21C2DBFA1FA3A7A3BBAB01ACF31C7818562C28752336DA781]]></sign>\n" +
                "<result_code><![CDATA[SUCCESS]]></result_code>\n" +
                "<openid><![CDATA[ohMlK5GvAopLHXFaccIkEB1YFDxY]]></openid>\n" +
                "<is_subscribe><![CDATA[N]]></is_subscribe>\n" +
                "<trade_type><![CDATA[PAP]]></trade_type>\n" +
                "<bank_type><![CDATA[CFT]]></bank_type>\n" +
                "<total_fee>1</total_fee>\n" +
                "<cash_fee>1</cash_fee>\n" +
                "<trade_state><![CDATA[SUCCESS]]></trade_state>\n" +
                "<fee_type><![CDATA[CNY]]></fee_type>\n" +
                "<transaction_id><![CDATA[4200002403202408143961557336]]></transaction_id>\n" +
                "<out_trade_no><![CDATA[743788516747513856]]></out_trade_no>\n" +
                "<time_end><![CDATA[20240814191006]]></time_end>\n" +
                "<trade_state_desc><![CDATA[OK]]></trade_state_desc>\n" +
                "<contract_id><![CDATA[01_91A63EAA189DDE32_1723617852_01_636c707cabf2de68_1]]></contract_id>\n" +
                "</xml>";
        QueryOrderResponse queryOrderResponse = mapper.readValue(text, QueryOrderResponse.class);
        Assertions.assertEquals(queryOrderResponse.getSign(), SignUtil.sign(queryOrderResponse, secret));
    }

    @Test
    void test2() throws JsonProcessingException {
        String text = "<xml>\n" +
                "<mch_id>1681960559</mch_id>\n" +
                "<appid>wxb690c7c18ce64bac</appid>\n" +
                "<nonce_str>5K8264ILTKCH16CQ2502SI8ZNMTM67VS</nonce_str>                                    \n" +
                "<sign>71D12E66E630C002D843D71049FC0FA390597901CBE6DFD0FA67F29242321E13 </sign>\n" +
                "<sign_type>HMAC-SHA256</sign_type>\n" +
                "<openid>323232</openid> \n" +
                "<plate_number_info>{\"plate_number_info\":[{\"plate_number\":\"粤B888888\",\"channel_type\":\"ETC\"}]}</plate_number_info >\n" +
                "<vehicle_event_type>NORMAL</vehicle_event_type>\n" +
                "<vehicle_event_createtime>20180101100000</vehicle_event_createtime >\n" +
                "</xml>";
        System.out.println(text);
        WxHighwayPlateNumberStatusChangeNotify entity = mapper.readValue(text, WxHighwayPlateNumberStatusChangeNotify.class);
        Assertions.assertEquals("71D12E66E630C002D843D71049FC0FA390597901CBE6DFD0FA67F29242321E13", SignUtil.sign(entity, secret));
    }

    @Test
    void test3() {
        Assertions.assertEquals("appid=wxb690c7c18ce64bac&mch_id=1681960559&nonce_str=5K8264ILTKCH16CQ2502SI8ZNMTM67VS&plate_number_info={\"plate_number_info\":[{\"plate_number\":\"粤B888888\",\"channel_type\":\"ETC\"}]}&sign_type=HMAC-SHA256&vehicle_event_createtime=20180101100000&vehicle_event_type=NORMAL&key=Yunnangonglulianwangshoufei12345,",
                "appid=wxb690c7c18ce64bac&mch_id=1681960559&nonce_str=5K8264ILTKCH16CQ2502SI8ZNMTM67VS&plate_number_info={\"plate_number_info\":[{\"plate_number\":\"粤B888888\",\"channel_type\":\"ETC\"}]}&sign_type=HMAC-SHA256&vehicle_event_createtime=20180101100000&vehicle_event_type=NORMAL&key=Yunnangonglulianwangshoufei12345,");
    }

    @Test
    void test4() throws JsonProcessingException {
        String text = "<xml><return_code><![CDATA[SUCCESS]]></return_code>\n" +
                "<return_msg><![CDATA[OK]]></return_msg>\n" +
                "<appid><![CDATA[wxb690c7c18ce64bac]]></appid>\n" +
                "<mch_id><![CDATA[1681960559]]></mch_id>\n" +
                "<nonce_str><![CDATA[DMs1ftiWR9OVZE3y]]></nonce_str>\n" +
                "<sign><![CDATA[56733BD32F2DB178BFD895C52F1703CEDD50B4FF70D3E02355BF1F32DC8B03D4]]></sign>\n" +
                "<result_code><![CDATA[SUCCESS]]></result_code>\n" +
                "<transaction_id><![CDATA[4200002423202408199462072023]]></transaction_id>\n" +
                "<out_trade_no><![CDATA[745488469899350016]]></out_trade_no>\n" +
                "<out_refund_no><![CDATA[743010089589280768_1723448209003]]></out_refund_no>\n" +
                "<refund_id><![CDATA[50303700342024081912537039259]]></refund_id>\n" +
                "<refund_channel><![CDATA[]]></refund_channel>\n" +
                "<refund_fee>1</refund_fee>\n" +
                "<coupon_refund_fee>0</coupon_refund_fee>\n" +
                "<total_fee>1</total_fee>\n" +
                "<cash_fee>1</cash_fee>\n" +
                "<coupon_refund_count>0</coupon_refund_count>\n" +
                "<cash_refund_fee>1</cash_refund_fee></xml>";
        JsonNode entity = mapper.readTree(text);
        System.out.println("appid=wxb690c7c18ce64bac&cash_fee=1&cash_refund_fee=1&coupon_refund_count=0&coupon_refund_fee=0&mch_id=1681960559&nonce_str=DMs1ftiWR9OVZE3y&out_refund_no=743010089589280768_1723448209003&out_trade_no=745488469899350016&refund_fee=1&refund_id=50303700342024081912537039259&result_code=SUCCESS&return_code=SUCCESS&return_msg=OK&total_fee=1&transaction_id=4200002423202408199462072023");
        Assertions.assertEquals("56733BD32F2DB178BFD895C52F1703CEDD50B4FF70D3E02355BF1F32DC8B03D4", SignUtil.sign(entity, secret, true));
    }
}
