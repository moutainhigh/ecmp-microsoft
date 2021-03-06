package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.EcmpUserFeedbackImage;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
@Repository
public interface EcmpUserFeedbackImageMapper
{
    /**
     * 查询【请填写功能名称】
     *
     * @param imageId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public EcmpUserFeedbackImage selectEcmpUserFeedbackImageById(Long imageId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param ecmpUserFeedbackImage 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<EcmpUserFeedbackImage> selectEcmpUserFeedbackImageList(EcmpUserFeedbackImage ecmpUserFeedbackImage);

    /**
     * 新增【请填写功能名称】
     *
     * @param ecmpUserFeedbackImage 【请填写功能名称】
     * @return 结果
     */
    public int insertEcmpUserFeedbackImage(EcmpUserFeedbackImage ecmpUserFeedbackImage);

    /**
     * 修改【请填写功能名称】
     *
     * @param ecmpUserFeedbackImage 【请填写功能名称】
     * @return 结果
     */
    public int updateEcmpUserFeedbackImage(EcmpUserFeedbackImage ecmpUserFeedbackImage);

    /**
     * 删除【请填写功能名称】
     *
     * @param imageId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteEcmpUserFeedbackImageById(Long imageId);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param imageIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteEcmpUserFeedbackImageByIds(Long[] imageIds);

    /**
     * 查询图片
     * @param feedbackId
     * @return
     */
    List<String> selectEcmpUserFeedbackByImage(@Param("feedbackId") Long feedbackId);
}
