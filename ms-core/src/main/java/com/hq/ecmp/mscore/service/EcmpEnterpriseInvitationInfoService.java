package com.hq.ecmp.mscore.service;
import com.hq.ecmp.mscore.domain.EcmpEnterpriseInvitationInfo;
import com.hq.ecmp.mscore.dto.InvitationDto;
import com.hq.ecmp.mscore.dto.InvitationInfoDTO;
import com.hq.ecmp.mscore.vo.InvitationDriverVO;
import com.hq.ecmp.mscore.vo.InvitationUserVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * (EcmpEnterpriseInvitationInfo)表服务接口
 *
 * @author makejava
 * @since 2020-03-16 18:33:00
 */
@Repository
public interface EcmpEnterpriseInvitationInfoService {

    /**
     * 通过ID查询单条数据
     *
     * @param invitationId 主键
     * @return 实例对象
     */
    EcmpEnterpriseInvitationInfo queryById(Long invitationId);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<EcmpEnterpriseInvitationInfo> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param ecmpEnterpriseInvitationInfo 实例对象
     * @return 实例对象
     */
    int insert(EcmpEnterpriseInvitationInfo ecmpEnterpriseInvitationInfo);

    /**
     * 修改数据
     *
     * @param ecmpEnterpriseInvitationInfo 实例对象
     * @return 实例对象
     */
    EcmpEnterpriseInvitationInfo update(EcmpEnterpriseInvitationInfo ecmpEnterpriseInvitationInfo);

    /**
     * 通过主键删除数据
     *
     * @param invitationId 主键
     * @return 是否成功
     */
    boolean deleteById(Long invitationId);


    /**
     * 邀请：停用/启用
     */
    public int updateInvitationState(InvitationDto invitationDto);
    /**
     * 邀请列表-员工
     */
    public List<InvitationUserVO> queryInvitationUser(InvitationInfoDTO invitationInfoDTO);
    /**
     * 邀请员工详情
     */
    public InvitationUserVO  queryInvitationUserDetial(String invitationId);

    /**
     * 邀请列表-驾驶员
     */
    List<InvitationDriverVO> queryInvitationDriver(InvitationInfoDTO invitationInfoDTO);
    /**
     * 邀请驾驶员详情
     */
    public InvitationDriverVO  queryInvitationDriverDetial(String invitationId);
}