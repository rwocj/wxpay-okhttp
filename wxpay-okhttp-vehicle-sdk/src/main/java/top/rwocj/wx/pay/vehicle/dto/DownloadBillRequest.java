package top.rwocj.wx.pay.vehicle.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import top.rwocj.wx.pay.vehicle.enums.BillType;

import java.util.Date;

/**
 * 下载交易账单请求实体
 *
 * @author lqb
 * @since 2024/7/22 13:26
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DownloadBillRequest extends AbstractRequest {

    /**
     * 下载对账单的日期，格式：20140603
     */
    @JacksonXmlProperty(localName = "bill_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd", timezone = "Asia/Shanghai")
    @JacksonXmlCData
    private Date billDate;

    /**
     * 账单类型
     * ALL，返回当日所有订单信息，默认值
     * SUCCESS，返回当日成功支付的订单
     * REFUND，返回当日退款订单
     * RECHARGE_REFUND，返回当日充值退款订单
     */
    @JacksonXmlProperty(localName = "bill_type")
    @JacksonXmlCData
    private String billType;

    /**
     * 压缩账单
     * 非必传参数，固定值：GZIP，返回格式为.gzip的压缩包账单。不传则默认为数据流形式。
     */
    @JacksonXmlProperty(localName = "tar_type")
    private final String tarType = "GZIP";

    /**
     * @param billDate 下载对账单的日期，格式：20140603
     */
    public DownloadBillRequest(Date billDate) {
        this.billDate = billDate;
    }

    /**
     * @param billDate 下载对账单的日期，格式：20140603
     * @param billType 账单类型
     */
    public DownloadBillRequest(Date billDate, BillType billType) {
        this.billDate = billDate;
        this.billType = billType.name();
    }
}
