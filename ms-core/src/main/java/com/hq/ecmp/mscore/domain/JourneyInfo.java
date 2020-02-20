package com.hq.ecmp.mscore.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 journey_info
 *
 * @author hqer
 * @date 2020-01-02
 */
public class JourneyInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long journeyId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long userId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long regimenId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String serviceType;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String useCarMode;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String useCarTime;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String itIsReturn;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String estimatePrice;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long projectId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String flightNumber;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String useTime;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String waitTimeLong;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String charterCarType;

    public void setJourneyId(Long journeyId)
    {
        this.journeyId = journeyId;
    }

    public Long getJourneyId()
    {
        return journeyId;
    }
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }
    public void setRegimenId(Long regimenId)
    {
        this.regimenId = regimenId;
    }

    public Long getRegimenId()
    {
        return regimenId;
    }
    public void setServiceType(String serviceType)
    {
        this.serviceType = serviceType;
    }

    public String getServiceType()
    {
        return serviceType;
    }
    public void setUseCarMode(String useCarMode)
    {
        this.useCarMode = useCarMode;
    }

    public String getUseCarMode()
    {
        return useCarMode;
    }
    public void setUseCarTime(String useCarTime)
    {
        this.useCarTime = useCarTime;
    }

    public String getUseCarTime()
    {
        return useCarTime;
    }
    public void setItIsReturn(String itIsReturn)
    {
        this.itIsReturn = itIsReturn;
    }

    public String getItIsReturn()
    {
        return itIsReturn;
    }
    public void setEstimatePrice(String estimatePrice)
    {
        this.estimatePrice = estimatePrice;
    }

    public String getEstimatePrice()
    {
        return estimatePrice;
    }
    public void setProjectId(Long projectId)
    {
        this.projectId = projectId;
    }

    public Long getProjectId()
    {
        return projectId;
    }
    public void setFlightNumber(String flightNumber)
    {
        this.flightNumber = flightNumber;
    }

    public String getFlightNumber()
    {
        return flightNumber;
    }
    public void setUseTime(String useTime)
    {
        this.useTime = useTime;
    }

    public String getUseTime()
    {
        return useTime;
    }
    public void setWaitTimeLong(String waitTimeLong)
    {
        this.waitTimeLong = waitTimeLong;
    }

    public String getWaitTimeLong()
    {
        return waitTimeLong;
    }
    public void setCharterCarType(String charterCarType)
    {
        this.charterCarType = charterCarType;
    }

    public String getCharterCarType()
    {
        return charterCarType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("journeyId", getJourneyId())
            .append("userId", getUserId())
            .append("regimenId", getRegimenId())
            .append("serviceType", getServiceType())
            .append("useCarMode", getUseCarMode())
            .append("useCarTime", getUseCarTime())
            .append("itIsReturn", getItIsReturn())
            .append("estimatePrice", getEstimatePrice())
            .append("projectId", getProjectId())
            .append("flightNumber", getFlightNumber())
            .append("useTime", getUseTime())
            .append("waitTimeLong", getWaitTimeLong())
            .append("charterCarType", getCharterCarType())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
