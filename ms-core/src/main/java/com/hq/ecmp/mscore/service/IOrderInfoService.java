package com.hq.ecmp.mscore.service;

import com.github.pagehelper.PageInfo;
import com.hq.ecmp.mscore.domain.OrderInfo;
import com.hq.ecmp.mscore.domain.OrderListInfo;
import com.hq.ecmp.mscore.dto.MessageDto;
import com.hq.ecmp.mscore.vo.OrderVO;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface IOrderInfoService {
    /**
     * 查询【请填写功能名称】
     *
     * @param orderId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public OrderInfo selectOrderInfoById(Long orderId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param orderInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<OrderInfo> selectOrderInfoList(OrderInfo orderInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param orderInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertOrderInfo(OrderInfo orderInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param orderInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateOrderInfo(OrderInfo orderInfo);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param orderIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    public int deleteOrderInfoByIds(Long[] orderIds);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param orderId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteOrderInfoById(Long orderId);

    /**
     * 我的行程订单列表
     *
     * @param userId
     * @return
     */
    public List<OrderListInfo> getOrderList(Long userId, int pageNum, int pageSize);


    /**
     * 获取订单详情
     * @param orderId
     * @return
     */
    OrderVO orderBeServiceDetail(Long orderId);

    /**
     * 获取服务提示语
     * @param orderId
     * @return
     */
    String orderHint(Long orderId);

    MessageDto getOrderMessage(Long userId);
}

