package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 后台提交申请单
 */
@Data
@ApiModel(description = "申请单添加对象")
public class ApplySingleVO {

    /**
     * 用车时间
     */
    @ApiModelProperty(name = "applyDate", value = "用车时间", required = true, position = 4)
    private Date applyDate;

    /**
     * 用车天数
     */
    @ApiModelProperty(name = "applyDays", value = "用车天数", required = true, position = 4)
    private String applyDays;


    /**
     * 上车地址
     */
    @ApiModelProperty(name = "startAddr", value = "上车地址", required = true, position = 5)
    private AddressVO startAddr;

    /**
     * 下车地址
     */
    @ApiModelProperty(name = "endAddr", value = "下车地址", required = true, position = 6)
    private AddressVO endAddr;

    /**
     * 申请人
     */
    @ApiModelProperty(name = "applyUser", value = "申请人", required = false, position = 8)
    private UserVO applyUser;
    /**
     * 乘车人
     */
    @ApiModelProperty(name = "passenger", value = "乘车人", required = true, position = 9)
    private UserVO passenger;

    /**
     * 申请原因
     */
    @ApiModelProperty(name = "reason", value = "申请原因", required = true, position = 15)
    private String reason;

    /**
     * 用车注意事项
     */
    @ApiModelProperty(name = "notes", value = "用车注意事项", required = true, position = 15)
    private String notes;

    /**
     * 同行人数
     */
    @ApiModelProperty(name = "peerNumber", value = "同行人数", required = false, position = 23)
    private Integer peerNumber;

    /**
     * 用车城市可用车型
     */
    @ApiModelProperty(name = "useCarTypes", value = "用车城市可用车型", required = false, position = 25)
    private List<UseCarTypeVO> canUseCarTypes;

    /**
     * 用车制度
     */
    @ApiModelProperty(name = "regimenId", value = "制度id")
    private Long regimenId;

    /**
     * 申请人id
     */
    @ApiModelProperty(name = "userId", value = "申请人id")
    private Long userId;
    /**
     * 申请人公司
     */
    @ApiModelProperty(name = "regimenId", value = "申请人公司")
    private Long companyId;

    /**
     * 车型级别 编号（主键）
     */
    @ApiModelProperty(name = "carTypeId", value = "车型级别")
    private Long carTypeId;
}
