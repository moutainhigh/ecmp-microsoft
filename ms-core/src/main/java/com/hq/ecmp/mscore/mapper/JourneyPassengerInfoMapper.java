package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.JourneyPassengerInfo;
import com.hq.ecmp.mscore.dto.JourneyPassengerInfoDto;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
@Repository
public interface JourneyPassengerInfoMapper
{
    /**
     * 查询【请填写功能名称】
     *
     * @param journeyPassengerId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public JourneyPassengerInfo selectJourneyPassengerInfoById(Long journeyPassengerId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param journeyPassengerInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<JourneyPassengerInfo> selectJourneyPassengerInfoList(JourneyPassengerInfo journeyPassengerInfo);

    /**
     * 新增行程乘客信息
     *
     * @param journeyPassengerInfo 行程乘客信息
     * @return 结果
     */
    public int insertJourneyPassengerInfo(JourneyPassengerInfo journeyPassengerInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param journeyPassengerInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateJourneyPassengerInfo(JourneyPassengerInfo journeyPassengerInfo);

    /**
     * 删除【请填写功能名称】
     *
     * @param journeyPassengerId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteJourneyPassengerInfoById(Long journeyPassengerId);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param journeyPassengerIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteJourneyPassengerInfoByIds(Long[] journeyPassengerIds);

    String getPeerPeople(Long journeyId);

    public Integer queryPeerCount(Long journeyId);
    
    public List<String> queryPeerUserNameList(Long journeyId);
    public JourneyPassengerInfo queryJourneyPassengerInfoByJourneyId(Long journeyId);


    /**
     * 根据名称模糊查询
     *
     * @author 郝大龙
     * @date 2020-08-11 11:45
     * @param name
     * @return java.util.List<com.hq.ecmp.mscore.dto.JourneyPassengerInfoDto>
     **/
    @Select("SELECT name,mobile FROM journey_passenger_info WHERE name LIKE CONCAT('%',#{name},'%')  ")
    List<JourneyPassengerInfoDto> selectLikeName(String name);

}
