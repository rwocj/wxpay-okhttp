package top.rwocj.wx.pay.dto;

/**
 * 返回给微信的信息
 */
public class Result {

    static final Result OK = new Result("SUCCESS", null);
    static final Result FAIL = new Result("fail", null);

    private final String code;

    private final String message;

    public Result(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static Result ok() {
        return OK;
    }

    public static Result fail() {
        return FAIL;
    }
}
