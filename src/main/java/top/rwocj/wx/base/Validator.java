package top.rwocj.wx.base;

/**
 * 验证微信发过来的响应体
 * https://wechatpay-api.gitbook.io/wechatpay-api-v3/qian-ming-zhi-nan-1/qian-ming-yan-zheng
 */
public interface Validator {

    /**
     * @param wxHeaders     验证需要的相关response wxHeaders
     * @param responseStr 响应体
     * @return true验证成功，表明是微信支付发过来的请求，false为验证失败
     */
    boolean validate(WxHeaders wxHeaders, String responseStr);
}
