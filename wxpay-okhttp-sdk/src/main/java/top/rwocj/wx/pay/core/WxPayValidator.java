package top.rwocj.wx.pay.core;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;

/**
 * 微信通知/响应验证器，用以验证消息来源于微信支付官方
 */
@Slf4j
public class WxPayValidator {

    private final SignVerifier signVerifier;

    public WxPayValidator(SignVerifier signVerifier) {
        this.signVerifier = signVerifier;
    }

    static IllegalArgumentException parameterError(String message, Object... args) {
        message = String.format(message, args);
        return new IllegalArgumentException("parameter error: " + message);
    }

    static IllegalArgumentException verifyFail(String message, Object... args) {
        message = String.format(message, args);
        return new IllegalArgumentException("signature verify fail: " + message);
    }

    public final boolean validate(WxHeaders wxHeaders, String responseStr) {
        try {
            validateParameters(wxHeaders);

            String message = buildMessage(wxHeaders, responseStr);
            String serial = wxHeaders.getWechatpaySerial();
            String signature = wxHeaders.getWechatpaySignature();

            if (!signVerifier.verify(serial, message.getBytes(StandardCharsets.UTF_8), signature)) {
                throw verifyFail("serial=[%s] message=[%s] sign=[%s], request-id=[%s]",
                        serial, message, signature,
                        wxHeaders.getRequestID());
            }
        } catch (IllegalArgumentException e) {
            log.warn(e.getMessage());
            return false;
        }
        return true;
    }

    protected final void validateParameters(WxHeaders wxHeaders) {
        String requestId;
        if (wxHeaders.getRequestID() == null) {
            throw parameterError("empty Request-ID");
        } else {
            requestId = wxHeaders.getRequestID();
        }

        if (wxHeaders.getWechatpaySerial() == null) {
            throw parameterError("empty Wechatpay-Serial, request-id=[%s]", requestId);
        } else if (wxHeaders.getWechatpaySignature() == null) {
            throw parameterError("empty Wechatpay-Signature, request-id=[%s]", requestId);
        } else if (wxHeaders.getWechatpayTimestamp() == null) {
            throw parameterError("empty Wechatpay-Timestamp, request-id=[%s]", requestId);
        } else if (wxHeaders.getWechatpayNonce() == null) {
            throw parameterError("empty Wechatpay-Nonce, request-id=[%s]", requestId);
        } else {
            String timestamp = wxHeaders.getWechatpayTimestamp();
            try {
                Instant instant = Instant.ofEpochSecond(Long.parseLong(timestamp));
                // 拒绝5分钟之外的应答
                if (Duration.between(instant, Instant.now()).abs().toMinutes() >= 5) {
                    throw parameterError("timestamp=[%s] expires, request-id=[%s]",
                            timestamp, requestId);
                }
            } catch (DateTimeException | NumberFormatException e) {
                throw parameterError("invalid timestamp=[%s], request-id=[%s]",
                        timestamp, requestId);
            }
        }
    }

    protected final String buildMessage(WxHeaders wxHeaders, String responseStr) {
        String timestamp = wxHeaders.getWechatpayTimestamp();
        String nonce = wxHeaders.getWechatpayNonce();

        return timestamp + "\n"
                + nonce + "\n"
                + responseStr + "\n";
    }


}
