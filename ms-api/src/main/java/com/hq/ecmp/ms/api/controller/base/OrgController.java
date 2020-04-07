package com.hq.ecmp.ms.api.controller.base;

import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.ms.api.dto.base.UserDto;
import com.hq.ecmp.mscore.domain.EcmpOrg;
import com.hq.ecmp.mscore.domain.RegimeVo;
import com.hq.ecmp.mscore.dto.EcmpOrgDto;
import com.hq.ecmp.mscore.dto.PageRequest;
import com.hq.ecmp.mscore.service.IEcmpOrgService;
import com.hq.ecmp.mscore.service.IEcmpUserService;
import com.hq.ecmp.mscore.vo.EcmpOrgVo;
import com.hq.ecmp.mscore.vo.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author: zj.hu
 * @Date: 2019-12-31 12:00
 */
@RestController
@RequestMapping("/org")
@Api(description="分/子公司 操作处理")
public class OrgController {

    @Autowired
    private IEcmpUserService ecmpUserService;
    @Autowired
    private IEcmpOrgService orgService;
    @Autowired
    private TokenService tokenService;
    /**
     * 查询用户 所在（子）公司的 部门列表
     * @param
     * @return
     */
    @ApiOperation(value = "getUserOwnCompanyDept",notes = "查询用户 所在（子）公司的 部门列表 ",httpMethod ="POST")
    @PostMapping("/getUserOwnCompanyDept")
    public ApiResponse<List<EcmpOrg>> getUserOwnCompanyDept(@RequestBody UserDto userDto){
        //获取登录用户
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        //(根据用户及部门名称模糊)查询用户 所在（子）公司的 部门列表
        List<EcmpOrg> ecmpOrgs = orgService.selectUserOwnCompanyDept(loginUser.getUser().getUserId(),userDto.getDeptName());
       if(CollectionUtils.isNotEmpty(ecmpOrgs)){
           return ApiResponse.success(ecmpOrgs);
       }else {
           return ApiResponse.error("未查询到指定部门");
       }
    }

    /**
     * 删除部门信息
     *
     * @param deptId 部门ID
     * @return 结果
     */
    /*@ApiOperation(value = "deleteDept",notes = "删除部门信息",httpMethod ="POST")
    @PostMapping("/deleteDept")
    public ApiResponse deleteDept(Long deptId){
        int i = orgService.deleteEcmpOrgById(deptId);
        if (i > 0){
            return ApiResponse.success("删除部门信息成功");
        }else {
            return ApiResponse.error("删除部门信息失败");
        }
    }*/

    /**
     * 根据公司id查询部门对象列表
     * @param
     * @return*/
    /*@ApiOperation(value = "getDeptList",notes = "查询部门列表",httpMethod ="POST")
    @PostMapping("/getDeptList")
    public ApiResponse<List<EcmpOrg>> selectEcmpOrgsByCompanyId(Long companyId){
        List<EcmpOrg> orgServiceeList = orgService.selectEcmpOrgsByCompanyId(companyId);
        return ApiResponse.success(orgServiceeList);
    }*/


    /**
     * 显示公司组织结构
     * @param  ecmpOrgVo
     * @return*/
    @ApiOperation(value = "显示公司组织结构",notes = "显示公司组织结构",httpMethod ="POST")
    @PostMapping("/selectCombinationOfCompany")
    public ApiResponse<List<EcmpOrgDto>> selectCombinationOfCompany(@RequestBody EcmpOrgVo ecmpOrgVo){
        Long deptId=ecmpOrgVo.getDeptId();
        String deptType=ecmpOrgVo.getDeptType();
        List<EcmpOrgDto> deptList = orgService.selectCombinationOfCompany(deptId,deptType);
        return ApiResponse.success(deptList);
    }

    /**
     * 显示公司列表
     * @param  ecmpOrgVo
     * @return*/
   /* @ApiOperation(value = "显示公司列表",notes = "显示公司列表",httpMethod ="POST")
    @PostMapping("/selectCompanyList")
    public ApiResponse<List<EcmpOrgDto>> selectCompanyList(@RequestBody EcmpOrgVo ecmpOrgVo){
        Long deptId=ecmpOrgVo.getDeptId();
        String deptType=ecmpOrgVo.getDeptType();
        if(deptId==null){
            return ApiResponse.error("组织id不能为空！");
        }
        List<EcmpOrgDto> companyList = orgService.selectCompanyList(deptId,deptType);
        return ApiResponse.success(companyList);
    }
*/

    /**
     * 显示公司列表
     * @param
     * @return*/
    @ApiOperation(value = "显示公司列表",notes = "显示公司列表",httpMethod ="POST")
    @PostMapping("/selectCompanyList")
    public ApiResponse<PageResult<EcmpOrgDto>> selectCompanyList(@RequestBody PageRequest pageRequest){
        Long deptId=pageRequest.getDeptId();
        String deptType=pageRequest.getDeptType();
        if(deptId==null){
            return ApiResponse.error("组织id不能为空！");
        }
       // List<EcmpOrgDto> companyList = orgService.selectCompanyList(deptId,deptType,pageRequest.getPageSize(),pageRequest.getPageNum());
        PageResult<EcmpOrgDto> pageResult = orgService.selectCompanyList(deptId,deptType,pageRequest.getPageSize(),pageRequest.getPageNum());
        //总条数
       // Integer count = orgService.queryCompanyListCount(ecmpOrgVo);
       // PageResult<EcmpOrgDto> pageResult = new PageResult<EcmpOrgDto>(Long.valueOf(count), companyList);
        return ApiResponse.success(pageResult);
    }

    /**
     * 查询当前机构信息
     * @param  ecmpOrgVo
     * @return*/
    @ApiOperation(value = "查询当前机构信息",notes = "查询当前机构信息",httpMethod ="POST")
    @PostMapping("/selectCurrentDeptInformation")
    public ApiResponse<EcmpOrgDto> selectCurrentDeptInformation(@RequestBody EcmpOrgVo ecmpOrgVo){
        Long deptId=ecmpOrgVo.getDeptId();
        if(deptId==null){
            return ApiResponse.error("组织id不能为空！");
        }
        EcmpOrgDto ecmpOrgDto = orgService.selectCurrentDeptInformation(deptId);
        return ApiResponse.success(ecmpOrgDto);
    }

    /**
     * 查询子公司详情
     * @return*/
    @ApiOperation(value = "getSubDetail",notes = "查询子公司详情",httpMethod ="POST")
    @PostMapping("/getSubDetail")
    public ApiResponse<EcmpOrgDto> getSubDetail(@RequestBody EcmpOrgVo ecmpOrgVo){
        Long deptId=ecmpOrgVo.getDeptId();
        if(deptId==null){
            return ApiResponse.error("组织id不能为空！");
        }
        EcmpOrgDto ecmpOrg = orgService.getSubDetail(deptId);
        return ApiResponse.success(ecmpOrg);
    }

    /**
     * 查询分/子公司、部门编号是否已存在
     * @param  ecmpOrg
     * @return
     */
    @ApiOperation(value = "selectDeptCodeExist",notes = "查询分/子公司、部门编号是否已存在",httpMethod ="POST")
    @PostMapping("/selectDeptCodeExist")
    public ApiResponse selectDeptCodeExist(@RequestBody EcmpOrgVo ecmpOrg){
        String deptCode=ecmpOrg.getDeptCode();
        if(deptCode!=null&&!("").equals(deptCode)){
            int j = orgService.selectDeptCodeExist(deptCode);
            if(j>0){
                return ApiResponse.error("该编号已存在，不可重复录入！");
            }
        }
            return ApiResponse.success();
    }
    /**
     * 添加单个分/子公司
     * @param  ecmpOrg
     * @return
     */
    @ApiOperation(value = "insertCompany",notes = "添加分/子公司",httpMethod ="POST")
    @PostMapping("/insertCompany")
    public ApiResponse insertCompany(@RequestBody EcmpOrgVo ecmpOrg){
        String deptCode=ecmpOrg.getDeptCode();
        /*if(deptCode!=null&&!("").equals(deptCode.trim())){
            int j = orgService.selectDeptCodeExist(deptCode);
            if(j>0){
                return ApiResponse.error("该编号已存在，不可重复录入！");
            }
        }
        String deptName=ecmpOrg.getDeptName();
        if(deptName==null&&("").equals(deptName.trim())){
                return ApiResponse.error("名称不可为空！");
        }
        String leader=ecmpOrg.getLeader();
        if(leader==null&&("").equals(leader.trim())){
                return ApiResponse.error("主管姓名不可为空！");
        }
        String phone=ecmpOrg.getPhone();
        if(phone==null&&("").equals(phone.trim())){
            return ApiResponse.error("主管手机号不可为空！");
        }else{
            int j = ecmpUserService.selectPhoneNumberExist(phone);
            if(j>0){
                return ApiResponse.error("该手机号已存在，不可重复录入！");
            }
        }
        String email=ecmpOrg.getEmail();
        if(email==null&&("").equals(email.trim())){
            return ApiResponse.error("主管邮箱不可为空！");
        }else{
            int j = ecmpUserService.selectEmailExist(email);
            if(j>0){
                return ApiResponse.error("该邮箱已存在，不可重复录入！");
            }
        }*/
        int i = orgService.addDept(ecmpOrg);
        if (i == 1){
            return ApiResponse.success("添加分/子公司成功");
        }else {
            return ApiResponse.error("添加分/子公司失败");
        }
    }

    /**
     * 修改单个分/子公司信息
     * @param  ecmpOrg
     * @return
     */
    @ApiOperation(value = "updateCompany",notes = "修改分/子公司信息",httpMethod ="POST")
    @PostMapping("/updateCompany")
    public ApiResponse updateCompany(@RequestBody EcmpOrgVo ecmpOrg){
        /*String deptCode=ecmpOrg.getDeptCode();
        if(deptCode!=null&&!("").equals(deptCode)){
            int j = orgService.selectDeptCodeExist(deptCode);
            if(j>0){
                return ApiResponse.error("该编号已存在，不可重复录入！");
            }
        }*/
        int i = orgService.updateEcmpOrg(ecmpOrg);
        if (i > 0){
            return ApiResponse.success("修改分/子公司成功");
        }else {
            return ApiResponse.error("修改分/子公司失败");
        }
    }
    /**
     * 逻辑删除分/子公司信息
     * @param  ecmpOrgVo
     * @return
     */
    @ApiOperation(value = "updateDelFlagById",notes = "删除分/子公司信息",httpMethod ="POST")
    @PostMapping("/updateDelFlagById")
    public ApiResponse updateDelFlagById(@RequestBody EcmpOrgVo ecmpOrgVo){
        Long deptId=ecmpOrgVo.getDeptId();
        String deptType=ecmpOrgVo.getDeptType();
        if(deptId==null){
            return ApiResponse.error("组织id不能为空！");
        }
        if(deptType==null){
            return ApiResponse.error("组织类别不能为空！");
        }
        String msg = orgService.updateDelFlagById(deptType,deptId);
            return ApiResponse.error(msg);
    }

    /**
     * 禁用/启用  分/子公司
     * @param  ecmpOrgVo
     * @return
     */
    @ApiOperation(value = "updateUseStatus",notes = "禁用/启用  分/子公司",httpMethod ="POST")
    @PostMapping("/updateUseStatus")
    public ApiResponse updateUseStatus(@RequestBody EcmpOrgVo ecmpOrgVo){
        Long deptId=ecmpOrgVo.getDeptId();
        String status=ecmpOrgVo.getStatus();
        if(deptId==null){
            return ApiResponse.error("组织id不能为空！");
        }
        if(status==null){
            return ApiResponse.error("部门状态不能为空！");
        }
        String s = orgService.updateUseStatus(status,deptId);
        /*if (i > 0){
            return ApiResponse.success("启用成功");
        }else {
            return ApiResponse.error("启用失败");
        }*/
        return ApiResponse.success(s);
    }

    /**
     * 按照分子公司名称或编号模糊查询匹配的列表
     * @param  ecmpOrgVo
     * @return*/
    @ApiOperation(value = "按照分子公司名称或编号模糊",notes = "按照分子公司名称或编号模糊",httpMethod ="POST")
    @PostMapping("/selectCompanyByDeptNameOrCode")
    public ApiResponse<List<EcmpOrgDto>> selectCompanyByDeptNameOrCode(@RequestBody EcmpOrgVo ecmpOrgVo){
        String deptNameOrCode=ecmpOrgVo.getDeptNameOrCode();
        if("".equals(deptNameOrCode.trim())){
            return ApiResponse.error("请输入有效的公司名称或编号！");
        }
        List<EcmpOrgDto> companyList = orgService.selectCompanyByDeptNameOrCode(deptNameOrCode);
        if(companyList.size()>0){
            return ApiResponse.success(companyList);
        }else{
            return ApiResponse.error("无匹配数据！");
        }
    }

    /**
     * 查询分/子公司下的部门名称和deptId
     * @return*/
    @ApiOperation(value = "selectDeptByCompany",notes = "查询分/子公司下的部门名称和deptId",httpMethod ="POST")
    @PostMapping("/selectDeptByCompany")
    public ApiResponse<List<EcmpOrgDto>> selectDeptByCompany(@RequestBody EcmpOrgVo ecmpOrgVo){
        Long deptId=ecmpOrgVo.getDeptId();
        if(deptId==null){
            return ApiResponse.error("组织id不能为空！");
        }
        List<EcmpOrgDto> ecmpOrgList = orgService.selectDeptByCompany(deptId);
        return ApiResponse.success(ecmpOrgList);
    }

}
