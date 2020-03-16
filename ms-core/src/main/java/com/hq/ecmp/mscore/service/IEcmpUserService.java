package com.hq.ecmp.mscore.service;

import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.mscore.domain.EcmpOrg;
import com.hq.ecmp.mscore.domain.EcmpUser;
import com.hq.ecmp.mscore.dto.EcmpUserDto;
import com.hq.ecmp.mscore.vo.EcmpUserVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * 用户信息Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface IEcmpUserService {


    /**
     * 根据用户id查询用户信息
     *
     * @param userId 用户信息ID
     * @return 用户信息
     */
    public EcmpUser selectEcmpUserById(Long userId);

    /**
     * 查询用户信息列表
     *
     * @param ecmpUser 用户信息
     * @return 用户信息集合
     */
    public List<EcmpUser> selectEcmpUserList(EcmpUser ecmpUser);

    /**
     * 新增用户信息
     *
     * @param ecmpUser 用户信息
     * @return 结果
     */
    public int insertEcmpUser(EcmpUser ecmpUser);

    /**
     * 修改用户信息
     *
     * @param ecmpUser 用户信息
     * @return 结果
     */
    public int updateEcmpUser(EcmpUserVo ecmpUser);

    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户信息ID
     * @return 结果
     */
    public int deleteEcmpUserByIds(Long[] userIds);

    /**
     * 删除用户信息信息
     *
     * @param userId 用户信息ID
     * @return 结果
     */
    public int deleteEcmpUserById(Long userId);
    
    
    /**
     * 判断用户是否是调度员
     * @param userId
     * @return
     */
    public boolean isDispatcher(Long userId);

    /*
     * 获取上级组织id中的员工姓名和电话
     *  @param  ecmpUserVo
     * @return List<EcmpUserDto>
     * */
    public List<EcmpUserDto> getEcmpUserNameAndPhone(EcmpUserVo ecmpUserVo);

    /*
     * 新增员工信息
     *  @param  ecmpUserVo
     * @return int
     * */
    public int addEcmpUser(EcmpUserVo ecmpUser);

    /*
    *查询手机号与邮箱是否已经存在
    * */
    public int selectPhoneAndEmailExist(EcmpUserVo ecmpUser);

    /**
     * 禁用/启用  员工
     *
     * @param deptId 部门ID
     * @return 结果
     */
    public String updateUseStatus(String status,Long deptId);

    /**
     * 逻辑删除员工信息
     *
     * @param userId 员工编号
     * @return 结果
     */
    public int updateDelFlagById(Long userId);

    /**
     * 查询员工列表
     *
     * @param deptId 部门ID
     * @return
     */
    public List<EcmpUserDto> getEcmpUserList(Long deptId);

    /*只修改手机号
    @param  ecmpOrg
     * @return
    * */
    public String updatePhoneNum(String newPhoneNum,String reWritePhone);

    /*员工详情
   @param  userId员工编号
    * @return
   * */
    public EcmpUserDto selectEcmpUserDetail(Long userId);

}
