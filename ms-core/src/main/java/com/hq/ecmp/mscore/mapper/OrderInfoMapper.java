package com.hq.ecmp.mscore.mapper;


import com.hq.ecmp.mscore.bo.OrderTaskClashBo;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.dto.MessageDto;
import com.hq.ecmp.mscore.dto.OrderDetailBackDto;
import com.hq.ecmp.mscore.dto.OrderInfoFSDto;
import com.hq.ecmp.mscore.dto.OrderListBackDto;
import com.hq.ecmp.mscore.dto.lease.LeaseSettlementDto;
import com.hq.ecmp.mscore.dto.*;
import com.hq.ecmp.mscore.vo.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
@Repository
public interface OrderInfoMapper {
    /**
     * 查询【请填写功能名称】
     *
     * @param orderId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public OrderInfo selectOrderInfoById(Long orderId);

    OrderInfo selectOrderInfoByOrderNumber(@Param("orderNumber") String orderNumber);

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
     * 修改【请填写功能名称】
     *
     * @param orderInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateOrderInfoNull(OrderInfo orderInfo);

    /**
     * 删除【请填写功能名称】
     *
     * @param orderId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteOrderInfoById(Long orderId);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param orderIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteOrderInfoByIds(Long[] orderIds);

    /**
     * 获取乘客端我的行程订单列表
     *
     * @param userId
     * @param isConfirmState
     * @return
     */
    public List<OrderListInfo> getOrderList(@Param("deptId") Long deptId,@Param("isConfirmState") int isConfirmState);


    /**
     * 订单列表外部车队只能看到自己外部车队的订单
     *
     * @param userId
     * @param isConfirmState
     * @return
     */
    public List<OrderListInfo> getOrderOutList(@Param("deptId") Long deptId,@Param("isConfirmState") int isConfirmState);


    public List<DispatchOrderInfo> queryOrderRelateInfo(OrderInfo orderInfo);

    /**
     * 查询已完成调度的订单
     *
     * @return
     */
    public List<DispatchOrderInfo> queryCompleteDispatchOrder();

    /**
     * 通过司机id获取司机的任务列表
     *
     * @param driverId
     * @return
     */
    public List<OrderDriverListInfo> getDriverOrderList(@Param("driverId") long driverId, @Param("flag") int flag);
    Integer getDriverOrderListCount(@Param("driverId") Long driverId,@Param("states")  String states);
    public DispatchOrderInfo getWaitDispatchOrderDetailInfo(Long orderId);

    public DispatchOrderInfo queryCompleteDispatchOrderDetail(Long orderId);

    /**
     * 获取调度员派车通知
     */
    MessageDto getOrderMessage(@Param("userId") Long userId, @Param("states") String states, @Param("driveId") Long driveId);


    /**
     * 通过司机id获取两小时内司机的下一个任务数据
     *
     * @param driverId
     * @return
     */
    OrderDriverListInfo getNextTaskWithDriver(@Param("driverId") Long driverId);

    /**
     * 通过汽车id获取两小时内司机的下一个任务数据
     *
     * @param carId
     * @return
     */
    OrderDriverListInfo getNextTaskWithCar(@Param("carId") Long carId);

    MessageDto getCancelOrderMessage(@Param("driverId") Long driverId, @Param("state") String state);

    //查询司机未完成的任务数量
    int getDriverOrderCount(@Param("driverId") Long driverId, @Param("states") String states);

    List<OrderDriverListInfo> driverOrderUndoneList(@Param("driverId") long driverId, @Param("day") int day);

    DriverOrderInfoVO selectOrderDetail(Long orderId);

    OrderStateVO getOrderState(@Param("orderId")Long orderId);

    /**
     * pc端获取订单列表
     *
     * @param orderListBackDto
     * @return
     */
    List<OrderListBackDto> getOrderListBackDto(OrderListBackDto orderListBackDto);

    /**
     * 根据订单编号获取订单详情数据
     *
     * @param orderNo
     * @return
     */
    OrderDetailBackDto getOrderListDetail(@Param("orderId") String orderNo);


    /**
     * pc端分页获取申请调派订单
     * @param query
     * @return
     */
    public List<ApplyDispatchVo> queryApplyDispatchList(ApplyDispatchQuery query);

    public Integer queryApplyDispatchListCount(ApplyDispatchQuery query);

    public List<ApplyDispatchVo> queryReassignmentDispatchList(ApplyDispatchQuery query);

    public Integer queryReassignmentDispatchListCount(ApplyDispatchQuery query);

    OrderInfo selectDriverOrder(@Param("driverId")Long driverId,@Param("state") String state);

    /**
     * 查询指定行程下的所有订单状态
     * @param journeyId
     * @return
     */
    public List<String> queryAllOrderStatusByJourneyId(Long journeyId);

    /**
     * 查询指定权限下的有效订单状态
     * @param powerId
     * @return
     */
    public String queryVaildOrderStatusByPowerId(Long powerId);

    /**
     * 查询权限下的有效订单
     * @param powerId
     * @return
     */
    public Long queryVaildOrderIdByPowerId(Long powerId);

    public List<String> queryAllOrderStatusByPowerId(Long powerId);

    public List<Long> queryOrderIdListByPowerId(Long powerId);

    List<String> queryUseCarMode(Long powerId);

    /**
     * 通過用車權限id查詢是否有有效的訂單
     * @param powerId
     * @return
     */
    List<OrderInfo> getValidOrderByPowerId(Long powerId);



    /**
     * 获取 指定车辆 与出发时间冲突的任务
     * @return
     */
    public List<OrderInfo> getSetOutClashTask(OrderTaskClashBo carTaskClashBo);

    /**
     * 获取 指定车辆 与 到达时间冲突的任务
     * @return
     */
    public List<OrderInfo> getArrivalClashTask(OrderTaskClashBo carTaskClashBo);


    /**
     * 获取 指定车辆 与出发时间 不冲突 的订单任务
     * @return
     */
    public List<OrderInfo> getSetOutBeforeTaskForCarOrDriver(OrderTaskClashBo carTaskClashBo);

    /**
     * 获取 指定车辆 与到达时间 不冲突 的订单任务
     * @return
     */
    public List<OrderInfo> getArrivalAfterTaskForCarOrDriver(OrderTaskClashBo carTaskClashBo);

    /**
     * 获取过期的订单
     */
    public List<OrderInfo> getExpiredOrder();

    /**
     * 通过用车权限查询最近的订单状态
     * @param powerId
     */
    public String queryLatestOrderByPowerId(Long powerId);

    /**
     *轨迹状态 订单状态
     * @param orderId
     * @return
     */
    OrderInfo selectOrderStateById(@Param("orderId")Long orderId);

    Long getCountForDispatched(ApplyDispatch query);

    Long getCountForReassigned(ApplyDispatch query);

    /*
     * @author ghb
     * @description  查询某订单所在当天的所有订单
     */
    List<OrderInfo> selectOrderInfoByIdAllDay(@Param("userId")Long userId);
    //查询已完单订单
    List<OrderInfo> selectOrderEnd();

    //根据行程id查询场景id
    Long getSceneByOrder(Long journeyId);

    //根据订单id查询城市
    String getProvinceByOrder(Long orderId);

    //查询已完单订单耗时
    double getOrderDurationById(Long orderId);

    //查询所有自有车订单
    String getOrderStateTraceById(Long orderId);

    //根据订单查询派单耗时
    int getdispatchDurationById(Long orderId);

    List<RunningOrderVo> getRunningOrder(@Param("userId")Long userId, @Param("states")String states);

    /***
     *
     * @param orderId
     * @return
     */
    OrderInfoMessage getCarMessage(Long orderId);

    /**
     * 查询市内用车权限已经使用过的订单id
     * @param powerId
     */
    List<Long> getAlreadyUsingOrderIdByPowerId(Long powerId);

    /**
     * 获取申请调度调度员列表
     * @param query
     * @return
     */
    List<DispatchVo> queryDispatchList(ApplyDispatchQuery query);

    /**
     * 获取直接调度列表
     * @return
     */
    List<DispatchVo> queryDispatchOrder(ApplyDispatchQuery query);

    /**
     * 获取申请调度系统管理员
     * @param query
     * @return
     */
    List<DispatchVo> queryAdminDispatchList(ApplyDispatchQuery query);

    /**
     * 获取申请调度系统管理员
     * @param query
     * @return
     */
    List<DispatchVo> queryAdminDispatchList2(ApplyDispatch query);

    /**
     * 改派系统管理员数据
     * @param query
     * @return
     */
    List<DispatchVo> queryAdminDispatchReassignmentList(ApplyDispatchQuery query);

    /**
     * 改派调度员数据
     * @param query
     * @return
     */
    List<DispatchVo> queryDispatchReassignmentList(ApplyDispatchQuery query);

    /**
     * 补单关联的数据
     * @param orderListBack
     * @return
     */
    OrderListBackDto getReplentshmentOrder(OrderListBackDto orderListBack);

    /**
     * 订单数据
     * @param orderNo
     * @return
     */
    String getOrderById(@Param("orderNo") String orderNo);

    /**
     * 补单详情
     * @param orderNo
     * @return
     */
    OrderDetailBackDto getOrderListDetailById(@Param("orderNo") String orderNo);

    List<OrderInfo> selectUsingCarByCarId(Long carId);

    /**
     * 用车申请单列表
     * @param userApplySingleVo
     * @return
     */
    List<UserApplySingleVo> getUseApplySearchList(UserApplySingleVo userApplySingleVo);


    /**
     * 获取当前业务员的待派车，已派车，已过期数量
     * @param userApplySingleVo
     * @return
     */
    List<UserApplySingleVo> getUseApplyCounts(UserApplySingleVo userApplySingleVo);

    /**
     * 获取申请调度调度员列表,佛山包车业务
     * @param query
     * @return
     */
    List<DispatchVo> queryDispatchListCharterCar(ApplyDispatch  query);

    /**
     * 获取首页动态
     * @param query
     * @return
     */
    List<DispatchVo> queryHomePageDispatchListCharterCarCounts(ApplyDispatchQuery query);

    /**
     * 获取首页申请调度调度员列表,佛山包车业务
     * @param query
     * @return
     */
    List<DispatchVo> queryHomePageDispatchListCharterCar(ApplyDispatchQuery query);

    /**
     * 获取用车总时长 和 开始用车时间
     * @param
     * @return
     */
    OrderInfoDate getUserTimeAndActionTime(long orderNo);
    /***
     * 当前订单改派订单
     * add by liuzb
     * @param orderInfo
     * @return
     */
    int changeOrder(OrderInfo orderInfo);


    DriverSmsInfo getOrderInfo(@Param("orderId") Long orderId);

    /**
     * 通过 城市code和部门id来查询对应的订单信息
     * @param query
     * @return
     */
    List<DispatchVo> getOrderInfoByCityAndDept(ApplyDispatch query);
    /**
     * 获取各状态调度单的数量
     * @param query
     * @return
     */
    List<DisOrderStateCount> getOrderStateCount(ApplyDispatch query);

    /***
     *
     * @param orderId
     * @return
     */
    Map downloadOrderData(@Param("orderId") Long orderId);

    /**
     *
     * @return
     */
    Map<String,Object> orderServiceCategory();

    /***
     * s
     * @return
     */
    List<String> getUseTheCar(@Param("userId") Long userId,@Param("companyId") Long companyId);

    /***
     *
     * @param data
     * @return
     */
    List<OrderInfoFSDto> getOrderInfoList(OrderInfoFSDto data);



    /**
     * 查询订单司机的所属车队和车队性质，用于首页统计
     * @return
     */
    List<Map<String,String>> selectOrderCarGroup(@Param("companyId")Long companyId);

    /**
     * 查询用车开始时间段之内的订单
     * @param companyId 当前登录人所属公司
     * @param beginDate 开始时间
     * @param endDate 结束时间
     * @return
     */
    List<Map> selectNormalOrderReserveTime(@Param("companyId")Long companyId,@Param("beginDate")String beginDate,@Param("endDate")String endDate);

    Long selectOrderIdByJourneyId(Long journeyId);


    /***
     * 结算单（当前车队结算单的所有已完成订单）
     * @param data
     * @return
     */
    List<String> getStatementsList(LeaseSettlementDto data);


    /**
     * 获取调度工作台对应状态的列表数据
     * @param state
     * @return
     */
    List<DisWorkBenchOrder> getDispatchOrderListWorkBench(@Param("state") String state);

    /**
     * 获取调度工作台订单的统计数量信息
     * @param state
     * @return
     */
    List<DisOrderStateCount> getDispatchOrderListWorkBenchCount(@Param("state") String state);

    PayeeInfoDto getPayeeInfo(ReckoningDto param);

    List<MoneyListDto> getMoneyList(ReckoningDto param);

    /***
     *
     * @param data
     * @return
     */
    List<OrderListBackDto> getCount(OrderListBackDto data);

    List<StatisticsForAdminDetailVo> userDeptUseCarDetail(@Param("beginDate") String beginDate,
                                                          @Param("endDate") String endDate,
                                                          @Param("carGroupName")String carGroupName,
                                                          @Param("deptId")Long deptId);

    PayeeInfoDto getCarGroupInfo(@Param("carGroupId")String carGroupId);


    String  selectOrderApplyInfoByOrderNumber(OrderInfo orderInfo);

    String  getOrderStateByOrderInfo(OrderInfo orderInfo);
}

