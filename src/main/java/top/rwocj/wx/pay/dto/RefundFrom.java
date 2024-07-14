package top.rwocj.wx.pay.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 退款出资账户及金额
 *
 * @author lqb
 * @since 2024/7/14 11:23
 **/
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
public class RefundFrom {

    /**
     * 出资账户类型
     * 下面枚举值多选一。
     * 枚举值：
     * AVAILABLE : 可用余额
     * UNAVAILABLE : 不可用余额
     * 示例值：AVAILABLE
     */
    private String account;

    /**
     * 出资金额
     * 对应账户出资金额
     * 示例值：444
     */
    private Integer amount;
}
