package com.hq.ecmp.ms.api.controller.base;

import com.github.pagehelper.PageInfo;
import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.common.utils.StringUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.constant.CommonConstant;
import com.hq.ecmp.ms.api.dto.base.ProjectDto;
import com.hq.ecmp.ms.api.dto.base.UserDto;
import com.hq.ecmp.mscore.domain.EcmpUser;
import com.hq.ecmp.mscore.domain.ProjectInfo;
import com.hq.ecmp.mscore.domain.ProjectUserRelationInfo;
import com.hq.ecmp.mscore.dto.*;
import com.hq.ecmp.mscore.service.IEcmpUserService;
import com.hq.ecmp.mscore.service.IProjectInfoService;
import com.hq.ecmp.mscore.service.IProjectUserRelationInfoService;
import com.hq.ecmp.mscore.vo.OrgTreeVo;
import com.hq.ecmp.mscore.vo.PageResult;
import com.hq.ecmp.mscore.vo.ProjectInfoVO;
import com.hq.ecmp.mscore.vo.ProjectUserVO;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: zj.hu
 * @Date: 2019-12-31 12:00
 */
@RestController
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private IProjectInfoService iProjectInfoService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private IEcmpUserService ecmpUserService;
    @Autowired
    private IProjectUserRelationInfoService iProjectUserRelationInfoService;

    /**
     * 项目下拉选
     * @return
     */
    @ApiOperation(value = "selectProjectsList",notes = "项目下拉选 ",httpMethod ="POST")
    @PostMapping("/selectProjectsList")
    public ApiResponse<List<ProjectInfo>> selectProjectsList(){
        List<ProjectInfo> projectInfoList = iProjectInfoService.selectProjectInfoList(new ProjectInfo(CommonConstant.ONE));
        if (CollectionUtils.isNotEmpty(projectInfoList)){
            return ApiResponse.success(projectInfoList);
        }else {
            return ApiResponse.error("未查询到项目信息列表");
        }
    }
    /**
     * 查询用户所在的所有项目列表
     * @return
     */
    @ApiOperation(value = "getListByUser",notes = "查询用户所有的项目列表",httpMethod ="POST")
    @PostMapping("/getListByUser")
    public ApiResponse<List<ProjectInfo>> getListByUser(@RequestBody ProjectDto projectDto){
        //根据用户Id查询项目对象
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        List<ProjectInfo> projectInfos = iProjectInfoService.getListByUserId(loginUser.getUser().getUserId(),projectDto.getProjectName());
        if(CollectionUtils.isNotEmpty(projectInfos)){
            return ApiResponse.success(projectInfos);
        }else {
            return ApiResponse.error("未查询到项目编号");
        }
    }

    /**
     * 校验项目编号
     * @return
     */
    @ApiOperation(value = "checkProjectCode",notes = "校验项目编号",httpMethod ="POST")
    @PostMapping("/checkProjectCode")
    public ApiResponse checkProjectCode(@RequestBody ProjectInfoDTO projectInfoDto){
        if (StringUtils.isEmpty(projectInfoDto.getProjectCode())){
            return ApiResponse.success();
        }
        List<ProjectInfo> projectInfos = iProjectInfoService.selectProjectInfoList(new ProjectInfo(projectInfoDto.getProjectCode()));
        if (CollectionUtils.isNotEmpty(projectInfos)){
            return ApiResponse.error("该编号已存在,不可重复录入!");
        }
        return ApiResponse.success();
    }

    /**
     * 获取项目主管列表
     * @return
     */
    @ApiOperation(value = "getProjectDirector",notes = "获取项目主管列表",httpMethod ="POST")
    @PostMapping("/getProjectDirector")
    public ApiResponse getProjectDirector(@RequestBody ProjectInfoDTO projectInfoDto){
        return ApiResponse.success();
    }

    /**
     * 获取项目列表
     * @return
     */
    @ApiOperation(value = "getProjectList",notes = "获取项目列表",httpMethod ="POST")
    @PostMapping("/getProjectList")
    public ApiResponse<PageResult<ProjectInfoVO>> getProjectList(@RequestBody PageRequest page){
        PageResult<ProjectInfoVO> pageInfo= iProjectInfoService.getProjectList(page.getPageNum(),page.getPageSize(),page.getSearch(),page.getFatherProjectId());
        return ApiResponse.success(pageInfo);
    }

    /**
     * 新增项目
     * @return
     */
    @ApiOperation(value = "createProject",notes = "新增项目",httpMethod ="POST")
    @PostMapping("/createProject")
    public ApiResponse createProject(@RequestBody ProjectInfoDTO projectInfoDto){
        ProjectInfo projectInfo = new ProjectInfo();
        BeanUtils.copyProperties(projectInfoDto,projectInfo);
        projectInfo.setIsEffective(1);
        int i = iProjectInfoService.insertProjectInfo(projectInfo);
        if (i>0){
            if (projectInfoDto.getIsAllUserUse()==1){//全部员工
                this.saveUserProject(projectInfo.getProjectId());
            }
        }
        return ApiResponse.error("新建项目成功");
    }

    @ApiOperation(value = "getProjectInfo",notes = "详情",httpMethod ="POST")
    @PostMapping("/getProjectInfo")
    @Transactional
    public ApiResponse<ProjectInfoVO> getProjectInfo(@RequestBody ProjectInfoDTO projectInfoDto){
        ProjectInfoVO VO=iProjectInfoService.getProjectInfo(projectInfoDto.getProjectId());
        return ApiResponse.success(VO);
    }

    private void saveUserProject(Long projectId){
        List<EcmpUser> ecmpUsers = ecmpUserService.selectEcmpUserList(new EcmpUser(CommonConstant.SWITCH_ON,CommonConstant.SWITCH_ON));
        List<ProjectUserRelationInfo> list=new ArrayList<>();
        for(EcmpUser user:ecmpUsers){
            list.add(new ProjectUserRelationInfo(projectId,user.getUserId()));
        }
        if (CollectionUtils.isNotEmpty(list)){
            List<ProjectUserRelationInfo> projectUserRelationInfo = iProjectUserRelationInfoService.selectProjectUserRelationInfoList(new ProjectUserRelationInfo(projectId));
            list.addAll(projectUserRelationInfo);
            List<ProjectUserRelationInfo> collect = list.stream().distinct().collect(Collectors.toList());
            iProjectUserRelationInfoService.insertProjectList(collect);
        }
    }
    @ApiOperation(value = "editProject",notes = "编辑",httpMethod ="POST")
    @PostMapping("/editProject")
    @Transactional
    public ApiResponse editProject(@RequestBody ProjectInfoDTO projectInfoDto){
        ProjectInfo projectInfo = new ProjectInfo();
        if (projectInfoDto.getProjectId()==null||projectInfoDto.getProjectId()==0){
            return ApiResponse.error("项目id不可为空");
        }
        ProjectInfo oldInfo = iProjectInfoService.selectProjectInfoById(projectInfoDto.getProjectId());
        int isAlluser=oldInfo.getIsAllUserUse();
        BeanUtils.copyProperties(projectInfoDto,projectInfo);
        int i = iProjectInfoService.updateProjectInfo(projectInfo);
        if (i>0){
            if (isAlluser!=projectInfoDto.getIsAllUserUse()){
                if (projectInfoDto.getIsAllUserUse()==1){//全部员工
                    saveUserProject(projectInfoDto.getProjectId());
                }else{
                    iProjectUserRelationInfoService.deleteProjectUserRelationInfoById(projectInfoDto.getProjectId());
                }
            }
        }
        return ApiResponse.success("编辑项目成功");
    }


    @ApiOperation(value = "getProjectUserList",notes = "获取成员列表",httpMethod ="POST")
    @PostMapping("/getProjectUserList")
    public ApiResponse<PageResult<ProjectUserVO>> getProjectUserList(@RequestBody PageRequest pageRequest){
        PageResult<ProjectUserVO> VO=iProjectInfoService.getProjectUserList(pageRequest.getFatherProjectId(),pageRequest.getPageNum(),pageRequest.getPageSize(),pageRequest.getSearch());
        return ApiResponse.success(VO);
    }

    /**
     * 成员树
     * @return*/
    @ApiOperation(value = "显示部门及员工树",notes = "显示部门及员工树",httpMethod ="POST")
    @PostMapping("/selectProjectUserTree")
    public ApiResponse<OrgTreeVo> selectProjectUserTree(String projectId){
        OrgTreeVo deptList = iProjectInfoService.selectProjectUserTree(projectId);
        return ApiResponse.success(deptList);
    }

    @ApiOperation(value = "getProjectUserInfo",notes = "根据项目id获取已绑定所有成员列表",httpMethod ="GET")
    @RequestMapping("/getProjectUserInfo")
    public ApiResponse<List<Long>> getProjectUserInfo(String projectId){
        List<Long> users=iProjectInfoService.getProjectUserInfo(Long.parseLong(projectId));
        return ApiResponse.success(users);
    }

    @ApiOperation(value = "removeProjectUser",notes = "移除成员",httpMethod ="POST")
    @PostMapping("/removeProjectUser")
    @Transactional
    public ApiResponse removeProjectUser(@RequestBody ProjectUserDTO projectUserDTO){
        //TODO 暂时不考虑当前用户是否有未完成的项目报销
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        int count=iProjectInfoService.removeProjectUser(projectUserDTO,loginUser.getUser().getUserId());
        return ApiResponse.success();
    }

    @ApiOperation(value = "addProjectUser",notes = "添加项目成员",httpMethod ="POST")
    @PostMapping("/addProjectUser")
    @Transactional
    public ApiResponse addProjectUser(@RequestBody AddProjectUserDTO projectUserDTO){
        //TODO 暂时不考虑当前用户是否有未完成的项目报销
        List<ProjectUserRelationInfo> list=new ArrayList<>();
        List<ProjectUserRelationInfo> projectUserRelationInfos = iProjectUserRelationInfoService.selectProjectUserRelationInfoList(new ProjectUserRelationInfo(projectUserDTO.getProjectId()));
        if (projectUserDTO.getUserIds()!=null&&projectUserDTO.getUserIds().length>0){
            for (Long userId:projectUserDTO.getUserIds()){
                list.add(new ProjectUserRelationInfo(projectUserDTO.getProjectId(),userId));
            }
        }
        list.removeAll(projectUserRelationInfos);
        int count=iProjectUserRelationInfoService.insertProjectList(list);
        return ApiResponse.success();
    }

    @ApiOperation(value = "deleteProject",notes = "删除项目",httpMethod ="POST")
    @PostMapping("/deleteProject")
    @Transactional
    public ApiResponse deleteProject(@RequestBody ProjectUserDTO projectUserDTO){
        //TODO 暂时不考虑当前用户是否有未完成的项目报销
        int count=iProjectInfoService.deleteProject(projectUserDTO);
        return ApiResponse.success();
    }

}
