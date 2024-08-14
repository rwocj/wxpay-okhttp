package top.rwocj.wx.pay.vehicle.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.UUID;

/**
 * @author lqb
 * @since 2024/7/18 11:23
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JacksonXmlRootElement(localName = "xml")
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class AbstractRequest extends HttpCommonField {

    public static final String VERSION_2 = "2.0";
    public static final String VERSION_3 = "3.0";

    public AbstractRequest() {
        setNonceStr(UUID.randomUUID().toString().replace("-", "").toUpperCase());
    }
}
