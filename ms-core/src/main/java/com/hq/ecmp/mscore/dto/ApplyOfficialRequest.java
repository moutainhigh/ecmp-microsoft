package com.hq.ecmp.mscore.dto;

import com.hq.ecmp.mscore.vo.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author xueyong
 * @date 2020/1/6
 * ecmp-proxy.
 */
@Data
@ApiModel(description = "申请单添加对象")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplyOfficialRequest {

    /**
     * 申请类型 eg 公务、差旅
     */
    @ApiModelProperty(name = "applyType", value = "申请类型", required = true, position = 1, example = "公务")
    private String applyType;
    /**
     * 服务类型 eg 预约、接机、送机、包车
     */
    @ApiModelProperty(name = "serviceType", value = "服务类型", required = true, position = 2, example = "预约")
    private String serviceType;
    /**
     * 用车类型 eg 自有/网约车
     */
    @ApiModelProperty(name = "useType", value = "用车类型", required = true, position = 3, example = "自有/网约车")
    private String useType;

    /**
     * 用车时间
     */
    @ApiModelProperty(name = "applyDate", value = "用车时间", required = true, position = 4)
    private Date applyDate;
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
     * 途经地
     */
    @ApiModelProperty(name = "passedAddress", value = "途经地址,集合", required = false, position = 7)
    private List<AddressVO> passedAddress;

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
     * 同行人（多个）
     */
    @ApiModelProperty(name = "partner", value = "同行人", required = false, position = 10)
    private List<UserVO> partner;

    /**
     * 是否往返
     */
    @ApiModelProperty(name = "isGoBack", value = "是否往返", required = false, position = 11)
    private String isGoBack;

    /**
     * 预估价格
     */
    @ApiModelProperty(name = "estimatePrice", value = "预估价格", required = false, position = 12)
    private String estimatePrice;

    /**
     * 成本中心,部门ID
     */
    @ApiModelProperty(name = "costCenter", value = "成本中心", required = false, position = 13)
    private String costCenter;
    /**
     * 项目编号
     */
    @ApiModelProperty(name = "projectNumber", value = "项目编号", required = false, position = 14)
    private String projectNumber;

    /**
     * 申请原因
     */
    @ApiModelProperty(name = "reason", value = "申请原因", required = true, position = 15)
    private String reason;

    /**
     * 包车类型 eg 日租8小时、半日租4小时
     */
    @ApiModelProperty(name = "charterType", value = "包车类型", required = false, position = 16)
    private String charterType;

    /**
     * 航班编号
     */
    @ApiModelProperty(name = "flightNumber", value = "航班号", required = false, position = 17)
    private String flightNumber;

    /**
     * 航班到达后等待多长时间用车 单位 分钟 M010 10分钟 M020 20分钟 M030 30分钟 H100 一小时 H130 一个半小时
     */
    @ApiModelProperty(name = "waitDurition", value = "等待时长", required = false, position = 18)
    private String waitDurition;

    /**
     * 往返开启后 等待 多长时间 返程用车
     */
    @ApiModelProperty(name = "returnWaitTime", value = "等待时长", required = false, position = 19)
    private String returnWaitTime;

    /**
     * 审批人
     */
    @ApiModelProperty(name = "approvers", value = "审批人", required = false, position = 20)
    private List<ApprovalVO> approvers;

    /**
     * 用车制度id
     */
    @ApiModelProperty(name = "regimeId", value = "用车制度id", required = true, position = 21)
    private Integer regimenId;

    /**
     * 航班计划起飞时间
     */
    @ApiModelProperty(name = "flightPlanTakeOffTime", value = "航班计划起飞时间", required = false, position = 21)
    private Date flightPlanTakeOffTime;

    /**
     * 航班计划到达时间
     */
    @ApiModelProperty(name = "flightPlanArriveTime", value = "航班计划到达时间", required = false, position = 22)
    private Date flightPlanArriveTime;

    /**
     * 航班计划到达时间
     */
    @ApiModelProperty(name = "carLevelAndPriceVOs", value = "车型及预估价集合", required = false, position = 23)
    List<CarLevelAndPriceVO> carLevelAndPriceVOs;

    /**
     * 航班计划到达时间
     */
    @ApiModelProperty(name = "peerNumber", value = "同行人数", required = false, position = 24)
    private Integer peerNumber;  //TODO 新增

    private Long companyId;

    //后台管理直接调度所用  D000 为直接调度
    private String distinguish;
    /**
     * 用车城市可用车型
     */
    @ApiModelProperty(name = "useCarTypes", value = "用车城市可用车型", required = false, position = 25)
    private List<UseCarTypeVO> canUseCarTypes;

    /**
     *  红旗公务=H001   佛山公务 = F001
     */
    @ApiModelProperty(name = "smsDifference", value = "短信发送区分")
    private String  smsDifference;

    @ApiModelProperty(name = "applyDays", value = "用车天数")
    private String applyDays;
}
