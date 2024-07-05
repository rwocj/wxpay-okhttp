package top.rwocj.wx.util;

import top.rwocj.wx.core.Sign;
import top.rwocj.wx.dto.JSAPICreateOrderRes;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class SignUtil {

    private SignUtil() {

    }

    public static JSAPICreateOrderRes sign(String prepay_id, String appid, Sign sign) {
        JSAPICreateOrderRes res = new JSAPICreateOrderRes();
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
