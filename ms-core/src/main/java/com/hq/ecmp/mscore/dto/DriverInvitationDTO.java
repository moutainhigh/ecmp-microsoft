package com.hq.ecmp.mscore.dto;


import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.util.Date;

/**
 * @author  shixin
 * @date 2020-03-17
 */
@Data
public class DriverInvitationDTO {
    @ApiModelProperty(value = "邀请ID")
    private Long invitationId;
    @ApiParam(name = "name", value = "邀请名", required = true )
    private String name;
    @ApiParam(name = "enterpriseId", value = "所属公司", required = true )
    private Long enterpriseId;
    @ApiParam(name = "carGroupId", value = "所属车队", required = true )
    private Long carGroupId;
    @ApiParam(name = "roseId", value = "角色", required = true )
    private Long roseId;
    @ApiParam(name = "regimeIds", value = "可用车辆", required = true )
    private String regimeIds;
    @ApiParam(name = "url", value = "url链接", required = true )
    private String url;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiParam(name = "state", value = "状态", required = true )
    private String state;
    @ApiParam(name = "type", value = "类型", required = true )
    private String type;
    @ApiParam(name = "apiUrl", value = "域名")
    private String apiUrl;
    @ApiParam(name = "driverNature", value = "驾驶员性质")
    String driverNature;
    @ApiParam(name = "hireBeginTime", value = "外聘驾驶员开始日期")
    Date hireBeginTime;
    @ApiParam(name = "hireEndTime", value = "外聘驾驶员结束日期")
    Date hireEndTime;
    @ApiParam(name = "borrowBeginTime", value = "借调开始日期")
    Date borrowBeginTime;
    @ApiParam(name = "borrowEndTime", value = "借调结束日期")
    Date borrowEndTime;
    @ApiParam(name = "companyId", value = "公司id")
    Long companyId;
}
