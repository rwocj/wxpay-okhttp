package top.rwocj.wx.pay.vehicle.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author lqb
 * @since 2024/7/18 13:54
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JacksonXmlRootElement(localName = "xml")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AbstractResponse extends HttpCommonField {

    /**
     * 返回状态码
     * SUCCESS/FAIL
     * 此字段是通信标识，非交易标识
     */
    @JacksonXmlProperty(localName = "return_code")
    private String returnCode;

    /**
     * 返回信息，如非空，为错误原因
     * 签名失败
     * 参数格式校验错误
     */
    @JacksonXmlProperty(localName = "return_msg")
    private String returnMsg;

    /**
     * 业务结果
     * SUCCESS/FAIL
     */
    @JacksonXmlProperty(localName = "result_code")
    private String resultCode;

    /**
     * 业务错误代码
     */
    @JacksonXmlProperty(localName = "err_code")
    private String errCode;

    /**
     * 业务错误描述
     */
    @JacksonXmlProperty(localName = "err_code_des")
    private String errCodeDes;

    @JsonIgnore
    public final boolean isHttpSuccess() {
        return "SUCCESS".equals(returnCode);
    }

    @JsonIgnore
    public final boolean isBusinessSuccess() {
        return "SUCCESS".equals(resultCode);
    }

    @JsonIgnore
    public final boolean isSuccess() {
        return isHttpSuccess() && isBusinessSuccess();
    }
}
