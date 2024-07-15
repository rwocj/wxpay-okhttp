package top.rwocj.wx.pay.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 支付者信息
 *
 * @author lqb
 * @since 2024/7/8 14:55
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payer {

    /**
     * 用户标识
     */
    @JsonProperty(value = "openid", required = true)
    private String openid;

}
