package com.hq.ecmp.mscore.service.impl;

import java.util.List;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.JourneyNodeInfo;
import com.hq.ecmp.mscore.mapper.JourneyNodeInfoMapper;
import com.hq.ecmp.mscore.service.IJourneyNodeInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class JourneyNodeInfoServiceImpl implements IJourneyNodeInfoService
{
    @Autowired
    private JourneyNodeInfoMapper journeyNodeInfoMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param nodeId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public JourneyNodeInfo selectJourneyNodeInfoById(Long nodeId)
    {
        return journeyNodeInfoMapper.selectJourneyNodeInfoById(nodeId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param journeyNodeInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<JourneyNodeInfo> selectJourneyNodeInfoList(JourneyNodeInfo journeyNodeInfo)
    {
        return journeyNodeInfoMapper.selectJourneyNodeInfoList(journeyNodeInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param journeyNodeInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertJourneyNodeInfo(JourneyNodeInfo journeyNodeInfo)
    {
        journeyNodeInfo.setCreateTime(DateUtils.getNowDate());
        return journeyNodeInfoMapper.insertJourneyNodeInfo(journeyNodeInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param journeyNodeInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateJourneyNodeInfo(JourneyNodeInfo journeyNodeInfo)
    {
        journeyNodeInfo.setUpdateTime(DateUtils.getNowDate());
        return journeyNodeInfoMapper.updateJourneyNodeInfo(journeyNodeInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param nodeIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteJourneyNodeInfoByIds(Long[] nodeIds)
    {
        return journeyNodeInfoMapper.deleteJourneyNodeInfoByIds(nodeIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param nodeId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteJourneyNodeInfoById(Long nodeId)
    {
        return journeyNodeInfoMapper.deleteJourneyNodeInfoById(nodeId);
    }
}
