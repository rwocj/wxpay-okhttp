package top.rwocj.wx.pay.vehicle.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
import top.rwocj.wx.pay.vehicle.enums.VehicleEventType;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 高速场景车牌状态变更通知接口
 *
 * @author lqb
 * @since 2024/7/22 14:01
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class WxHighwayPlateNumberStatusChangeNotify extends AbstractRequest {

    /**
     * 车牌变更事件类型
     * 当前通知车牌的状态变化类型：
     * NORMAL-变为可用状态
     * BLOCKED-变为不可用状态
     * 注意此状态是车牌状态，而非用户状态
     *
     * @see VehicleEventType
     */
    @JacksonXmlProperty(localName = "vehicle_event_type")
    @JacksonXmlCData
    private String vehicleEventType;

    /**
     * 车牌变更事件信息
     * 当前车牌状态，所对应的事件类型为BLOCKED时返回：
     * OVERDUE: 车牌对应用户欠费。商户引导用户还款，请跳转到车主服务
     * REMOVE：用户移除车牌导致车牌不可用。请跳转到授权/开通接口。
     * PAUSE: 用户关闭或已暂停车主服务导致车牌不可用。请跳转到授权/开通接口
     */
    @JacksonXmlProperty(localName = "vehicle_event_des")
    @JacksonXmlCData
    private String vehicleEventDes;

    /**
     * 车牌事件发生时间
     * 车牌状态变更的发生时间, 商户可根据该时间是否最新，来判断是否需要更新当前车牌状态。
     */
    @JacksonXmlProperty(localName = "vehicle_event_createtime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMddHHmmss", timezone = "Asia/Shanghai")
    @JacksonXmlCData
    private Date vehicleEventCreateTime;

    /**
     * 车牌信息
     */
    @JacksonXmlProperty(localName = "plate_number_info")
    @JacksonXmlCData
    private String plateNumberInfo;

    /**
     * openid
     * 用户在商户appid或子商户appid下的唯一标识。
     */
    @JacksonXmlProperty(localName = "openid")
    @JacksonXmlCData
    private String openId;

    /**
     * 是否是正常状态
     */
    @JsonIgnore
    public boolean isNormalEvent() {
        return VehicleEventType.NORMAL.name().equals(vehicleEventType);
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
