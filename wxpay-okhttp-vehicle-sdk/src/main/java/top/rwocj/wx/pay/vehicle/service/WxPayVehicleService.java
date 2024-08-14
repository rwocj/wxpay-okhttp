package top.rwocj.wx.pay.vehicle.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.rwocj.wx.pay.common.WxPayException;
import top.rwocj.wx.pay.vehicle.dto.*;
import top.rwocj.wx.pay.vehicle.enums.BillType;
import top.rwocj.wx.pay.vehicle.property.WxPayVehicleProperties;
import top.rwocj.wx.pay.vehicle.utils.CommonUtil;
import top.rwocj.wx.pay.vehicle.utils.SignUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.zip.GZIPInputStream;

@RequiredArgsConstructor
public class WxPayVehicleService {

    private final static Logger log = LoggerFactory.getLogger(WxPayVehicleService.class);

    /**
     * base url
     */
    public final static String BASE_URL = "https://api.mch.weixin.qq.com";

    /**
     * 无需证书的请求客户端
     */
    protected final OkHttpClient noCertificateRequiredOkHttpClient;
    /**
     * 需要证书的请求客户端
     */
    protected final OkHttpClient certificateRequiredOkHttpClient;

    @Getter
    protected final XmlMapper xmlMapper;

    @Getter
    protected final WxPayVehicleProperties wxPayVehicleProperties;

    /**
     * 微信垫资小程序跳转需要的信息
     *
     * @param openId 用户标识
     */
    public MiniProgramAdvanceFundingExtraData getMiniProgramAdvanceFundingExtraData(String openId) {
        MiniProgramAdvanceFundingExtraData extraData = new MiniProgramAdvanceFundingExtraData(openId);
        setCommonRequestParams(extraData);
        return extraData;
    }

    /**
     * 开通车主服务的信息，用于小程序跳转至车主小程序
     * 如在没有返回path,说明不需要再调用‘用户授权/开通接口’
     * <a href="https://pay.weixin.qq.com/wiki/doc/api/vehicle_v2.php?chapter=20_101&index=10">参考文档</a>
     *
     * @param openId       用户openid
     * @param materialInfo 物料信息
     * @param plateNum     车牌号
     * @return 用户授权信息
     */
    public UserAuthorizationInfo userAuthorization(String openId, String plateNum, String materialInfo) throws WxPayException {
        UserStateRequest request = UserStateRequest.highwayRequestWithOpenId(openId);
        request.setPlateNumber(plateNum);
        try {
            UserStateResponse userStateResponse = userState(request);
            if (userStateResponse.isBusinessSuccess()) {
                UserAuthorizationInfo userAuthorizationInfo = new UserAuthorizationInfo();
                userAuthorizationInfo.setNormal(userStateResponse.isNormal());
                userAuthorizationInfo.setContainPlateNum(userStateResponse.getPlateNumberInfos().stream().anyMatch(item -> Objects.equals(plateNum, item.getPlateNumber())));
                String path = userStateResponse.getPath();
                userAuthorizationInfo.setPath(path);
                if (path != null && (!userStateResponse.isNormal() || !userAuthorizationInfo.isContainPlateNum())) {
                    UserAuthorizationExtraData extraData = UserAuthorizationExtraData.highwayExtraData(openId, plateNum);
                    setCommonRequestParams(extraData);
                    extraData.setMaterialInfo(materialInfo);
                    userAuthorizationInfo.setExtraData(extraData);
                }
                return userAuthorizationInfo;
            }
            throw new WxPayException("查询车主用户状态失败");
        } catch (WxPayException e) {
            throw new WxPayException("查询车主用户状态失败", e);
        }

    }

    /**
     * 检测用户是否正常，且包含指定车牌号
     *
     * @param openId   用户openId
     * @param plateNum 车牌号
     * @return true: 正常 false: 不正常
     */
    public boolean isUserNormalAndContainsPlateNum(String openId, String plateNum) {
        UserStateRequest userStateRequest = openId == null ? UserStateRequest.highwayRequestWithPlateNumber(plateNum) : UserStateRequest.highwayRequestWithOpenId(openId);
        try {
            UserStateResponse userStateResponse = userState(userStateRequest);
            if (userStateResponse.isBusinessSuccess()) {
                List<PlateNumberInfo> plateNumberInfos = userStateResponse.getPlateNumberInfos();
                boolean containsPlateNum = plateNumberInfos.stream().anyMatch(plateNumberInfo -> plateNumberInfo.getPlateNumber().equals(plateNum));
                return userStateResponse.isNormal() && containsPlateNum;
            }
        } catch (WxPayException e) {
            log.error("查询微信签约用户状态失败", e);
        }
        return false;
    }

    /**
     * <a href="https://pay.weixin.qq.com/wiki/doc/api/vehicle_v2.php?chapter=20_93&index=9">用户状态查询</a>
     * 在停车场、高速、加油等场景下，商户需获取用户车主服务状态/需要关联车主服务。本接口，会查询用户是否开通、授权、有欠费或黑名单用户情况，并将对应的用户状态进行返回。
     *
     * @param request 用户状态查询请求参数
     * @return 用户状态查询结果，调用者无需再次验签，也无需判断http是否成功
     * @throws WxPayException 下单失败,包括请求失败、请求参数错误导致的失败，验签失败等
     * @apiNote 1. 调用用户状态查询api获取当前用户车主状态，如果当前用户车主状态异常（如有欠费(OVERDUE)，未授权(UNAUTHORIZED)，校验授权关系失败(VEHICLE_AUTH_ERROR)），接口同步返回跳转路径(path字段)
     * ，商户侧需根据步骤2引导用户进入车主服务进行相关操作；如果当前用户车主状态正常，不会返回path字段，不需要再引导用户进入车主服务
     * 2. --小程序,APP跳转，通过跳转路径(path)调用 ‘用户授权/开通接口api’ 进入车主小程序的对应页面，用户进行授权/开通的操作
     * --H5跳转，通过跳转路径(path)调用 ‘用户授权/开通接口api’ 进入车主H5对应的页面，用户进行授权/开通操作
     * 3. 返回商户小程序,APP或H5页面后再次调用用户状态查询api确认用户最新车主状态及车牌信息
     */
    public UserStateResponse userState(UserStateRequest request) throws WxPayException {
        return post(BASE_URL + "/vehicle/pay/querystate", request, UserStateResponse.class);
    }

    /**
     * <a href="https://pay.weixin.qq.com/wiki/doc/api/vehicle_v2.php?chapter=20_982&index=2">申请扣款</a>
     *
     * @param request 扣款请求
     * @throws WxPayException 下单失败,包括请求失败、请求参数错误导致的失败，验签失败等
     */
    public PayApplyResponse payApply(PayApplyRequest request) throws WxPayException {
        if (request.getNotifyUrl() == null) {
            request.setNotifyUrl(wxPayVehicleProperties.getPayApplyNotifyUrl());
        }
        return post(BASE_URL + "/vehicle/pay/payapply", request, PayApplyResponse.class);
    }

    /**
     * <a href="https://pay.weixin.qq.com/wiki/doc/api/vehicle_v2.php?chapter=20_96&index=7">查询订单</a>
     *
     * @param request 查询订单请求
     * @return 查询订单结果
     * @throws WxPayException 下单失败,包括请求失败、请求参数错误导致的失败，验签失败等
     */
    public QueryOrderResponse queryOrder(QueryOrderRequest request) throws WxPayException {
        return post(BASE_URL + "/transit/queryorder", request, QueryOrderResponse.class);
    }

    /**
     * <a href="https://pay.weixin.qq.com/wiki/doc/api/vehicle_v2.php?chapter=20_4&index=5">申请退款</a>
     *
     * @param request 退款请求
     * @throws WxPayException 下单失败,包括请求失败、请求参数错误导致的失败，验签失败等
     */
    public PayRefundResponse payRefund(PayRefundRequest request) throws WxPayException {
        if (request.getNotifyUrl() == null) {
            request.setNotifyUrl(wxPayVehicleProperties.getPayRefundNotifyUrl());
        }
        return request(generateRequest(BASE_URL + "/secapi/pay/refund", request), PayRefundResponse.class, true);
    }

    /**
     * 查询退款
     */
    public QueryRefundResponse queryRefund(QueryRefundRequest request) throws WxPayException {
        return post(BASE_URL + "/pay/refundquery", request, QueryRefundResponse.class);
    }

    /**
     * 下载账单，未对文件流做处理，如返回的是压缩包，调用者需自己解压
     *
     * @param request 下载账单请求
     * @return 返回原始的账单字节流，无需再解压
     * @throws WxPayException 下单失败,包括请求失败、请求参数错误导致的失败，验签失败等
     */
    public InputStream downloadBill(DownloadBillRequest request) throws WxPayException {
        if (request.getBillType() == null) {
            request.setBillType(BillType.ALL.name());
        }
        return download(generateRequest(BASE_URL + "/pay/downloadbill", request));
    }

    /**
     * 验证签名
     *
     * @param t   签名相关信息
     * @param <T> 签名基类
     * @return true: 验签通过; false: 验签失败
     */
    public <T extends HttpCommonField> boolean isSignPass(T t) {
        return isSignPass(t, !(t instanceof AbstractRequest));
    }

    /**
     * 验证签名
     *
     * @param t   签名相关信息
     * @param <T> 签名基类
     * @return true: 验签通过; false: 验签失败
     */
    public <T extends HttpCommonField> boolean isSignPass(T t, boolean ignoreSignType) {
        log.debug("待签名验证实体:{}", t);
        return Objects.equals(SignUtil.sign(t, wxPayVehicleProperties.getApiKey(), ignoreSignType), t.getSign());
    }

    /**
     * 通用的POST方法，用于请求其他微信支付接口
     *
     * @param url         完善的请求地址
     * @param requestBody 请求数据，最终会序列化为json
     * @return 请求结果
     * @throws WxPayException 请求失败
     */
    public <T extends AbstractResponse, R extends AbstractRequest> T post(String url, R requestBody, Class<T> tClass) throws WxPayException {
        return request(generateRequest(url, requestBody), tClass, false);
    }

    /**
     * POST请求，直接获取响应的流
     *
     * @param request 请求参数
     */
    private InputStream download(Request request) throws WxPayException {
        try (Response execute = noCertificateRequiredOkHttpClient.newCall(request).execute()) {
            log.debug("下载微信账单响应码：{}", execute.code());
            ResponseBody body = execute.body();
            if (execute.isSuccessful()) {
                if (body != null) {
                    byte[] bodyBytes = body.bytes();
                    boolean gzipCompressed = CommonUtil.isGzipCompressed(bodyBytes);
                    if (gzipCompressed) {
                        try (GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(bodyBytes));
                             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                            //gzip解压
                            byte[] buffer = new byte[1024];
                            int len;
                            while ((len = gis.read(buffer)) != -1) {
                                outputStream.write(buffer, 0, len);
                            }
                            return new ByteArrayInputStream(outputStream.toByteArray());
                        }
                    }
                    throw new WxPayException("下载微信账单失败，" + new String(bodyBytes, StandardCharsets.UTF_8));
                }
            } else {
                throw new WxPayException(body != null ? "下载微信账单响应：" + body.string() : "下载微信账单请求响应码:" + execute.code());
            }
        } catch (IOException e) {
            throw new WxPayException("下载微信账单失败", e);
        }
        throw new WxPayException("下载微信账单失败");
    }

    /**
     * @param requiredCertificate 是否需要证书
     */
    protected <T extends AbstractResponse> T request(Request request, Class<T> tClass, boolean requiredCertificate) throws WxPayException {
        String url = request.url().toString();
        OkHttpClient okHttpClient = requiredCertificate ? certificateRequiredOkHttpClient : noCertificateRequiredOkHttpClient;
        try (Response execute = okHttpClient.newCall(request).execute()) {
            log.debug("微信支付url:{}, 响应码：{}", url, execute.code());
            ResponseBody body = execute.body();
            if (execute.isSuccessful()) {
                if (body != null) {
                    String string = body.string();
                    log.debug("微信支付响应,url:{},响应体：\n{}", url, string);
                    T t = xmlMapper.readValue(string, tClass);
                    if (t.isHttpSuccess()) {
                        String sign = t.getSign();
                        if (sign == null || sign.isEmpty()) {
                            throw new WxPayException("微信支付响应签名为空");
                        }
                        if (!isSignPass(t)) {
                            throw new WxPayException("微信支付响应签名验证失败");
                        }
                        return t;
                    }
                    throw new WxPayException("微信支付调用失败：" + t.getReturnCode() + ":" + t.getReturnMsg());
                }
            } else {
                throw new WxPayException(body != null ? "请求响应：" + body.string() : "请求响应码:" + execute.code());
            }
        } catch (IOException e) {
            throw new WxPayException("微信支付请求失败", e);
        }
        throw new WxPayException("微信支付请求失败");
    }

    protected Request generateRequest(String url, AbstractRequest request) {
        setCommonRequestParams(request);
        String content = null;
        try {
            content = xmlMapper.writeValueAsString(request);
            log.debug("微信支付请url：{}, body:{}", url, content);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
        return new Request.Builder()
                .url(url)
                .post(RequestBody.create(MediaType.parse("application/xml"), content))
                .header("Accept", "application/xml")
                .build();
    }

    protected <T extends AbstractRequest> void setCommonRequestParams(T request) {
        if (request.getAppId() == null) {
            request.setAppId(wxPayVehicleProperties.getAppId());
        }
        if (request.getMchId() == null) {
            request.setMchId(wxPayVehicleProperties.getMchId());
        }
        request.setSign(SignUtil.signRequest(request, wxPayVehicleProperties.getApiKey()));
    }

}
