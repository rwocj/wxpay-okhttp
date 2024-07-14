package top.rwocj.wx.pay.util;

import top.rwocj.wx.pay.core.Sign;
import top.rwocj.wx.pay.dto.WxJSAPICreateOrderRes;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class SignUtil {

    private SignUtil() {

    }

    public static WxJSAPICreateOrderRes sign(String prepay_id, String appid, Sign sign) {
        WxJSAPICreateOrderRes res = new WxJSAPICreateOrderRes();
        res.setAppId(appid);
        res.setTimeStamp(String.valueOf(System.currentTimeMillis() / 1000));
        res.setNonceStr(UUID.randomUUID().toString().replaceAll("-", ""));
        res.setPackageValue("prepay_id=" + prepay_id);
        res.setSignType("RSA");
        res.setPaySign(sign.sign((res.getAppId() + "\n"
                + res.getTimeStamp() + "\n"
                + res.getNonceStr() + "\n"
                + res.getPackageValue() + "\n").getBytes(StandardCharsets.UTF_8)));
        return res;
    }
}
