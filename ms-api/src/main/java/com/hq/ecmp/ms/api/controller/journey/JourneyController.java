package com.hq.ecmp.ms.api.controller.journey;

import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.ms.api.dto.base.UserDto;
import com.hq.ecmp.ms.api.dto.journey.JourneyApplyDto;
import com.hq.ecmp.ms.api.dto.journey.JourneyNodeDto;
import com.hq.ecmp.mscore.domain.ApplyInfo;
import com.hq.ecmp.mscore.domain.JourneyInfo;
import com.hq.ecmp.mscore.service.IApplyInfoService;
import com.hq.ecmp.mscore.service.IJourneyInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: zj.hu
 * @Date: 2019-12-31 13:17
 */
@RestController
public class JourneyController {

    @Autowired
    private IJourneyInfoService journeyInfoService;
    @Autowired
    private IApplyInfoService applyInfoService;
    /**
     * 创建行程
     * @param  journeyApplyDto  行程申请信息
     * @return
     */
    @ApiOperation(value = "createJourneyApply",notes = "创建行程",httpMethod ="POST")
    @PostMapping("/createJourneyApply")
    public ApiResponse createJourneyApply(JourneyApplyDto journeyApplyDto, UserDto userDto){

        return null;
    }


    /**
     * 撤消行程
     * @param  journeyApplyDto  行程申请信息
     * @return
     */
    @ApiOperation(value = "cancelJourney",notes = "撤消行程",httpMethod ="POST")
    @PostMapping("/cancelJourney")
    public ApiResponse cancelJourneyApply(@RequestBody JourneyApplyDto journeyApplyDto){
        //撤销行程申请
        ApplyInfo applyInfo = ApplyInfo.builder().applyId(journeyApplyDto.getApplyId()).state("S004").build();
        int i = applyInfoService.updateApplyInfo(applyInfo);
        if(i == 1){
            return ApiResponse.success("撤销成功");
        }else {
            return ApiResponse.error("撤销申请失败，请重试");
        }
    }

    /**
     * 查询用户当前进行中的行程信息
     * @param  userDto  用户信息
     * @return
     */
    @ApiOperation(value = "getUserRunningJourneyNumbers",notes = "查询用户当前进行中的行程信息 ",httpMethod ="POST")
    @PostMapping("/getUserRunningJourneyNumbers")
    public ApiResponse getUserRunningJourneyNumbers(UserDto userDto){

        return null;
    }
    /**
     * 查询用户所有行程信息
     * @param  userDto  用户信息
     * @return
     */
    @ApiOperation(value = "getUserAllJourneyNumbers",notes = "查询用户当前进行中的行程信息 ",httpMethod ="POST")
    @PostMapping("/getUserAllJourneyNumbers")
    public ApiResponse getUserAllJourneyNumbers(UserDto userDto){
        //查询用户所有行程信息
        JourneyInfo journeyInfo = new JourneyInfo();
        journeyInfo.setUserId(userDto.getUserId());
        List<JourneyInfo> journeyInfoList = journeyInfoService.selectJourneyInfoList(journeyInfo);
        return ApiResponse.success(journeyInfoList);
    }



    /**
     * 查询用户当前进行中的行程列表
     * @param  userDto  用户信息
     * @return
     */
    @ApiOperation(value = "getUserJourneys",notes = "查询用户当前进行中的行程信息 ",httpMethod ="POST")
    @PostMapping("/getUserJourneys")
    public ApiResponse<List<JourneyInfo>> getUserJourneys(UserDto userDto){

        return null;
    }

    /**
     * 查询用户当前进行中的行程详细信息
     * @param  journeyApplyDto  用户信息
     * @return
     */
    @ApiOperation(value = "getUserJourneysDetail",notes = "查询用户当前进行中的行程详细信息 ",httpMethod ="POST")
    @PostMapping("/getUserJourneysDetail")
    public ApiResponse<JourneyInfo> getUserJourneysDetail(JourneyApplyDto journeyApplyDto){
        //根据行程id查询行程信息
        JourneyInfo journeyInfo = journeyInfoService.selectJourneyInfoById(journeyApplyDto.getJouneyId());
        return ApiResponse.success(journeyInfo);
    }

    /**
     * 获取行程的 分享的链接
     * @param  journeyApplyDto  用户信息
     * @return
     */
    @ApiOperation(value = "getSharedJourneyUrl",notes = "查询用户当前进行中的行程详细信息 ",httpMethod ="POST")
    @PostMapping("/getSharedJourneyUrl")
    public ApiResponse<JourneyInfo> getSharedJourneyUrl(JourneyApplyDto journeyApplyDto){

        return null;
    }

    /**
     * 获取行程的用车权限
     * @param  journeyApplyDto  行程申请信息
     * @return
     */
    @ApiOperation(value = "getJourneyUserCarPower",notes = "获取行程的用车权限 ",httpMethod ="POST")
    @PostMapping("/getJourneyUserCarPower")
    public ApiResponse<JourneyInfo> getJourneyUserCarPower(JourneyApplyDto journeyApplyDto){

        return null;
    }

    /**
     * 获取行程对应的订单信息
     * @param  journeyApplyDto  行程申请信息
     * @return
     */
    @ApiOperation(value = "getJourneyOrders",notes = "获取行程的用车权限 ",httpMethod ="POST")
    @PostMapping("/getJourneyOrders")
    public ApiResponse<JourneyInfo> getJourneyOrders(JourneyApplyDto journeyApplyDto){

        return null;
    }

    /**
     * 获取行程-行程节点的 的司车调派信息
     * @param  journeyNodeDto  行程-行程节点-信息
     * @return
     */
    @ApiOperation(value = "getJourneyDispatchedInfo",notes = "获取行程-行程节点的 的司车调派信息 ",httpMethod ="POST")
    @PostMapping("/getJourneyDispatchedInfo")
    public ApiResponse<JourneyInfo> getJourneyDispatchedInfo(JourneyNodeDto journeyNodeDto){

        return null;
    }

}
