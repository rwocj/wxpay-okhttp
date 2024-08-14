package top.rwocj.wx.pay.vehicle.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import top.rwocj.wx.pay.common.JacksonUtil;

import java.util.Collections;
import java.util.List;

/**
 * 用户状态响应
 *
 * @author lqb
 * @since 2024/7/18 14:06
 **/
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UserStateResponse extends AbstractResponse {

    /**
     * NORMAL：正常用户，已开通车主服务，且已授权访问
     * PAUSED：已暂停车主服务
     * OVERDUE: 用户已开通车主服务，但欠费状态。提示用户还款，请跳转到车主服务
     * UNAUTHORIZED：用户未授权使用当前业务，或未开通车主服务。请跳转到授权接口
     */
    @JacksonXmlProperty(localName = "user_state")
    @JacksonXmlCData
    private String userState;

    /**
     * 用户标识,用户在商户appid下的唯一标识，当用户入驻车主平台时进行返回
     * 可能为空
     */
    @JacksonXmlProperty(localName = "openid")
    @JacksonXmlCData
    private String openId;

//    /**
//     * 用户子标识，sub_appid下，用户的唯一标识
//     * 可能为空
//     */
//    @JacksonXmlProperty(localName = "sub_openid")
//    private String subOpenId;

    /**
     * 发起扣费方式
     * PROACTIVE：表示用户主动发起的免密支付
     * AUTOPAY：表示用户无感的支付
     * 传入openid作为查询条件时，只会返回PROACTIVE
     * 可能为空
     */
    @JacksonXmlProperty(localName = "deduct_mode")
    @JacksonXmlCData
    private String deductMode;

    /**
     * 跳转路径
     * 跳转车主小程序的页面路径，如果该参数返回不为空，商户侧需调用‘用户授权/开通接口’引导用户进入车主小程序进行授权/开通的操作,‘用户授权/开通接口’请查看下面的详细说明;
     * H5跳转同理，需跳转的场景有：
     * -用户状态正常，但无车牌
     * -用户欠费
     * -用户未授权
     * -用户未开通/暂停车主服务
     * H5跳转同理
     * 可能为空
     */
    @JacksonXmlProperty(localName = "path")
    @JacksonXmlCData
    private String path;

    /**
     * 车牌信息
     * 车牌号列表。仅包括省份+车牌，不包括特殊字符。
     * 注：在PARKING SPACE场景下无返回。
     * 可能为空
     */
    @JacksonXmlProperty(localName = "plate_number_info")
    @JacksonXmlCData
    private String plateNumberInfo;

    @JsonIgnore
    public boolean isNormal() {
        return "NORMAL".equals(userState);
    }

    @JsonIgnore
    public List<PlateNumberInfo> getPlateNumberInfos() {
        try {
            if (this.plateNumberInfo == null) {
                return Collections.emptyList();
            }
            ObjectMapper mapper = JacksonUtil.getObjectMapper();
            JsonNode jsonNode = mapper.readTree(plateNumberInfo);
            return mapper.readValue(mapper.writeValueAsString(jsonNode.get("plate_number_info")), mapper.getTypeFactory().constructCollectionType(List.class, PlateNumberInfo.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
