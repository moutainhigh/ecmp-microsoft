package com.hq.ecmp.ms.api.controller.base;

import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.ms.api.dto.base.UserDto;
import com.hq.ecmp.mscore.domain.EcmpOrg;
import com.hq.ecmp.mscore.service.IEcmpOrgService;
import com.hq.ecmp.mscore.service.IEcmpUserService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author: zj.hu
 * @Date: 2019-12-31 12:00
 */
@RestController
@RequestMapping("/org")
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


}
