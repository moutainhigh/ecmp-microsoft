package com.hq.ecmp.ms.api.controller.journey;

import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.constant.ApproveStateEnum;
import com.hq.ecmp.ms.api.dto.base.RegimeDto;
import com.hq.ecmp.ms.api.dto.base.UserDto;
import com.hq.ecmp.ms.api.dto.journey.JourneyApplyDto;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.dto.*;
import com.hq.ecmp.mscore.dto.config.ApproveTemplateIDTO;
import com.hq.ecmp.mscore.service.*;
import com.hq.ecmp.mscore.vo.*;
import com.hq.ecmp.util.DateFormatUtils;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: caobj
 * @Date: 2019-3-19 13:18
 */
@RestController
@RequestMapping("/flow")
public class FlowContoller {

    @Autowired
    private IApplyInfoService applyInfoService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private IApproveTemplateInfoService templateInfoService;
    @Autowired
    private IApproveTemplateNodeInfoService nodeInfoService;



    /**
     *添加审批流
     * @param
     * @return
     */
    @Deprecated()
    @ApiOperation(value = "addFlowTemplate",notes = "添加审批流 ",httpMethod ="POST")
    @PostMapping("/addFlowTemplate")
    public ApiResponse addFlowTemplate(@RequestBody AddFolwDTO addFolwDTO){
        //提交行程申请
        try{
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            nodeInfoService.addFlowTemplate(addFolwDTO,loginUser.getUser().getUserId());
        }catch (Exception e){
            return ApiResponse.success();
        }
        return ApiResponse.success();
    }

    /**
     *审批流列表
     * @param
     * @return
     */
    @Deprecated()
    @ApiOperation(value = "flowTemplateList",notes = "审批流列表 ",httpMethod ="POST")
    @PostMapping("/flowTemplateList")
    public ApiResponse<List<ApprovaTemplateVO>> flowTemplateList(){
        //提交行程申请
        List<ApprovaTemplateVO> list=templateInfoService.getTemplateList();
        return ApiResponse.success(list);
    }

    /**
     *审批流详情
     * @param
     * @return
     */
    @Deprecated()
    @ApiOperation(value = "flowTemplateDetail",notes = "审批流详情 ",httpMethod ="GET")
    @PostMapping("/flowTemplateDetail")
    public ApiResponse<ApprovaTemplateVO> flowTemplateDetail(@RequestBody ApproveTemplateIDTO templateIDTO){
        //提交行程申请
        ApprovaTemplateVO vo=templateInfoService.flowTemplateDetail(templateIDTO.getApproveTemplateId());
        return ApiResponse.success(vo);
    }


    /**
     * 编辑审批流
     * @param
     * @return
     */
    @ApiOperation(value = "editFlowTemplate",notes = "编辑审批流",httpMethod ="POST")
    @PostMapping("/editFlowTemplate")
    public ApiResponse editFlowTemplate(@RequestBody AddFolwDTO addFolwDTO){
        //提交公务行程申请
        try {
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            nodeInfoService.editFlowTemplate(addFolwDTO,loginUser.getUser().getUserId());
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("编辑审批流失败");
        }
        return ApiResponse.success("编辑审批流成功"+addFolwDTO.getApproveTemplateId());
    }

    /**
     * 删除审批流
     * @return
     */
    @ApiOperation(value = "deleteFlow",notes = "删除审批流 ",httpMethod ="POST")
    @PostMapping("/deleteFlow")
    public ApiResponse deleteFlow(@RequestBody ApproveTemplateIDTO templateIDTO){
        try {
            templateInfoService.deleteFlow(templateIDTO.getApproveTemplateId());
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("删除模板失败");
        }
        return ApiResponse.success("删除模板成功:"+templateIDTO.getApproveTemplateId());
    }



}