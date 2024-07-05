package top.rwocj.wx.core;

public class WxPayException extends Exception {

    private static final long serialVersionUID = 1L;

    public WxPayException() {
        super();
    }

    public WxPayException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public WxPayException(String message) {
        super(message);
    }

    public WxPayException(String messages, Exception e) {
        super(messages, e);
    }

}
