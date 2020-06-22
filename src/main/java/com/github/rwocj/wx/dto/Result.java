package com.github.rwocj.wx.dto;

import lombok.AllArgsConstructor;
import lombok.Value;

/**
 * 返回给微信的信息
 */
@Value
@AllArgsConstructor
public class Result {

    static final Result OK = new Result("SUCCESS", null);
    static final Result FAIL = new Result("fail", null);

    String code;

    String message;

    public static Result ok() {
        return OK;
    }

    public static Result fail() {
        return FAIL;
    }
}
