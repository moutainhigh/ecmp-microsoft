package com.hq.ecmp.mscore.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 car_group_info
 *
 * @author hqer
 * @date 2020-01-02
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class CarGroupInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long carGroupId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long parentCarGroupId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String city;  //TODO Long改动

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String cityName;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long leader;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String state;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String telephone;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String carGroupName;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String carGroupCode;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String shortAddress;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String fullAddress;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String province;   // 所属省份代码：110000  北京

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Float longitude;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Float latitude;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long companyId;   // 所属公司id


    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String allowOuterDispatch;   // 是否允许外部调度

    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String itIsInner;  // 是否是内部车队  C000   内部车队      C111   外部车队

    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String accountName;  //TODO 佛山二期新增字段 账户名

    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String bankName;  //TODO 佛山二期新增字段 开户行

    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String bankAccount;  //TODO 佛山二期新增字段 银行账号





    public CarGroupInfo(String city, Long companyId) {
        this.city = city;
        this.companyId = companyId;
    }
    public CarGroupInfo( Long companyId) {
        this.companyId = companyId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("carGroupId", getCarGroupId())
            .append("city", getCity())
            .append("cityName", getCityName())
            .append("leader", getLeader())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
