package com.hq.ecmp.mscore.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @ClassName
 * @Description TODO
 * @Author yj
 * @Date 2020/4/7 21:25
 * @Version 1.0
 */
@ApiModel("按月排班列表出参")
@Data
public class WorkInfoMonthVo {



    @ApiModelProperty(value = "日期（格式：yyyy-MM-dd）")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date calendarDate;
    @ApiModelProperty(value = "当前排班状态（0000:上班,1111:没上班）")
    private String workState;
    @ApiModelProperty(value = "休假状态（X000:病假,X002:年假,X003:公休,X999:正常排班工作）")
    private String leaveStatus;




}
