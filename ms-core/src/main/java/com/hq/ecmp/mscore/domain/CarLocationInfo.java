package com.hq.ecmp.mscore.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 car_location_info
 *
 * @author hqer
 * @date 2020-01-02
 */
public class CarLocationInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private String carLocationId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long carId;

    public void setCarLocationId(String carLocationId)
    {
        this.carLocationId = carLocationId;
    }

    public String getCarLocationId()
    {
        return carLocationId;
    }
    public void setCarId(Long carId)
    {
        this.carId = carId;
    }

    public Long getCarId()
    {
        return carId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("carLocationId", getCarLocationId())
            .append("carId", getCarId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
