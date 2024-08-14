package top.rwocj.wx.pay.vehicle.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于标记某个字段不参与签名计算
 *
 * @author lqb
 * @since 2024/7/18 11:14
 **/
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreSign {
}
