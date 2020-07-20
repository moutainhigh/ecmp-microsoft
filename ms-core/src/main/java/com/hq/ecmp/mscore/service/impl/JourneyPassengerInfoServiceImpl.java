package com.hq.ecmp.mscore.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.JourneyPassengerInfo;
import com.hq.ecmp.mscore.dto.JourneyPassengerInfoDto;
import com.hq.ecmp.mscore.mapper.JourneyPassengerInfoMapper;
import com.hq.ecmp.mscore.service.IJourneyPassengerInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class JourneyPassengerInfoServiceImpl implements IJourneyPassengerInfoService
{
    @Autowired
    private JourneyPassengerInfoMapper journeyPassengerInfoMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param journeyPassengerId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public JourneyPassengerInfo selectJourneyPassengerInfoById(Long journeyPassengerId)
    {
        return journeyPassengerInfoMapper.selectJourneyPassengerInfoById(journeyPassengerId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param journeyPassengerInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<JourneyPassengerInfo> selectJourneyPassengerInfoList(JourneyPassengerInfo journeyPassengerInfo)
    {
        return journeyPassengerInfoMapper.selectJourneyPassengerInfoList(journeyPassengerInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param journeyPassengerInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertJourneyPassengerInfo(JourneyPassengerInfo journeyPassengerInfo)
    {
        journeyPassengerInfo.setCreateTime(DateUtils.getNowDate());
        return journeyPassengerInfoMapper.insertJourneyPassengerInfo(journeyPassengerInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param journeyPassengerInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateJourneyPassengerInfo(JourneyPassengerInfo journeyPassengerInfo)
    {
        journeyPassengerInfo.setUpdateTime(DateUtils.getNowDate());
        return journeyPassengerInfoMapper.updateJourneyPassengerInfo(journeyPassengerInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param journeyPassengerIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteJourneyPassengerInfoByIds(Long[] journeyPassengerIds)
    {
        return journeyPassengerInfoMapper.deleteJourneyPassengerInfoByIds(journeyPassengerIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param journeyPassengerId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteJourneyPassengerInfoById(Long journeyPassengerId)
    {
        return journeyPassengerInfoMapper.deleteJourneyPassengerInfoById(journeyPassengerId);
    }

    @Override
    public String getPeerPeople(Long journeyId) {
        return journeyPassengerInfoMapper.getPeerPeople(journeyId);
    }

	@Override
	public Integer queryPeerCount(Long journeyId) {
		// TODO Auto-generated method stub
		return journeyPassengerInfoMapper.queryPeerCount(journeyId);
	}

	@Override
	public List<String> queryPeerUserNameList(Long journeyId) {
		return journeyPassengerInfoMapper.queryPeerUserNameList(journeyId);
	}

    /**
     * 根据乘车人名称模糊查询
     *
     * @param name
     * @return com.hq.common.core.api.ApiResponse<java.util.List<com.hq.ecmp.mscore.domain.JourneyPassengerInfo>>
     * @author Chenkp
     * @date 2020-07-17 15:32
     */
    @Override
    public List<JourneyPassengerInfoDto> selectJourneyPassengerInfoByName(String name) {
        List<JourneyPassengerInfoDto> journeyPassengerInfoDtos = journeyPassengerInfoMapper.selectJourneyPassengerInfoByName(name);
        return journeyPassengerInfoDtos.stream().distinct().collect(Collectors.toList());
    }
}
