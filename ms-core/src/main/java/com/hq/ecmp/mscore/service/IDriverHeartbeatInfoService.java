package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.DriverHeartbeatInfo;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface IDriverHeartbeatInfoService
{
    /**
     * 查询【请填写功能名称】
     *
     * @param heartId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public DriverHeartbeatInfo selectDriverHeartbeatInfoById(Long heartId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param driverHeartbeatInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<DriverHeartbeatInfo> selectDriverHeartbeatInfoList(DriverHeartbeatInfo driverHeartbeatInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param driverHeartbeatInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertDriverHeartbeatInfo(DriverHeartbeatInfo driverHeartbeatInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param driverHeartbeatInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateDriverHeartbeatInfo(DriverHeartbeatInfo driverHeartbeatInfo);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param heartIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    public int deleteDriverHeartbeatInfoByIds(Long[] heartIds);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param heartId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteDriverHeartbeatInfoById(Long heartId);

    DriverHeartbeatInfo findNowLocation(Long driverId,Long orderId);

}
