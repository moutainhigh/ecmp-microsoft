package com.hq.ecmp.mscore.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.constant.CommonConstant;
import com.hq.ecmp.constant.OrderPayConstant;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.mapper.*;
import com.hq.ecmp.mscore.service.IOrderPayInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import springfox.documentation.spring.web.json.Json;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author ghb
 * @description 支付表
 * @date 2020/5/7
 */
@Service
@Slf4j
public class OrderPayInfoServiceImpl implements IOrderPayInfoService {

    @Resource
    private OrderPayInfoMapper orderPayInfoMapper;
    @Resource
    private OrderSettlingInfoMapper orderSettlingInfoMapper;
    @Resource
    private RegimeInfoMapper regimeInfoMapper;
    @Resource
    private OrderInfoMapper orderInfoMapper;
//    @Resource
//    private OrderAccountInfoMapper orderAccountInfoMapper;

    @Override
    public int insertOrderPayInfo(OrderPayInfo orderPayInfo) {
        return orderPayInfoMapper.insertOrderPayInfo(orderPayInfo);
    }

    @Override
    public OrderPayInfo getOrderPayInfo(Long orderId) {
        return orderPayInfoMapper.getOrderPayInfo(orderId);
    }

    /***
     * 服务结束后插入结算单/财务单/支付单
     * @param orderNo
     * @param amount 结算单/取消单总金额
     * @param distance 公里
     * @param duration 时长
     * @param json 结算单/取消单详情
     * @param userId
     * @param overMoney 个人超额支付金额
     * @return
     */
    @Override
    public OrderPayInfo insertOrderPayAndSetting(Long orderNo,BigDecimal amount,String distance,String duration,String json,Long userId,BigDecimal overMoney) {
        if (amount==null||BigDecimal.ZERO.compareTo(amount)>0){
            return null;
        }
        OrderSettlingInfo orderSettlingInfo = new OrderSettlingInfo();
        orderSettlingInfo.setOrderId(orderNo);
        orderSettlingInfo.setTotalMileage(new BigDecimal(distance).stripTrailingZeros());
        orderSettlingInfo.setTotalTime(new BigDecimal(duration).stripTrailingZeros());
        orderSettlingInfo.setAmount(amount);
        orderSettlingInfo.setAmountDetail(json);
        orderSettlingInfo.setCreateBy(CommonConstant.START);
        orderSettlingInfo.setCreateTime(DateUtils.getNowDate());
        orderSettlingInfoMapper.insertOrderSettlingInfo(orderSettlingInfo);
//        //插入订单财务信息表
//        OrderAccountInfo orderAccountInfo = new OrderAccountInfo();
//        orderAccountInfo.setBillId(orderSettlingInfo.getBillId());
//        orderAccountInfo.setOrderId(orderNo.toString());
//        orderAccountInfo.setAmount(amount.stripTrailingZeros());
//        orderAccountInfo.setCreateTime(new Date());
//        orderAccountInfo.setState(CommonConstant.NOT_INVOICED);
//        orderAccountInfoMapper.insertOrderAccountInfo(orderAccountInfo);
        //插入订单支付表
        if (overMoney.compareTo(BigDecimal.ZERO)==1){
            OrderPayInfo orderPayInfo = new OrderPayInfo();
            String substring = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
            orderPayInfo.setPayId(substring);
            orderPayInfo.setBillId(orderSettlingInfo.getBillId());
            orderPayInfo.setOrderId(orderNo);
            orderPayInfo.setState(OrderPayConstant.UNPAID);
            orderPayInfo.setPayMode(OrderPayConstant.PAY_AFTER_STATEMENT);
            orderPayInfo.setAmount(overMoney);
            orderPayInfo.setCreateTime(DateUtils.getNowDate());
            orderPayInfo.setCreateBy(userId);
            this.insertOrderPayInfo(orderPayInfo);
            return orderPayInfo;
        }
        return null;
    }

    /**
     * 校验是否超额
     * @param amount 取消费/正常结单费
     * @param regimeId
     * @param applyUserId
     * @return
     */
    @Override
    public BigDecimal checkOrderFeeOver(BigDecimal amount,Long regimeId,Long applyUserId){
        //网约车是否限额
        //查询出用车制度表的限额额度，和限额类型
        Map<String,Object> map= Maps.newHashMap();
//        OrderSettlingInfo orderSettlingInfo2 = orderSettlingInfoMapper.selectOrderSettlingInfoByOrderId(orderId);
//        if (orderSettlingInfo2==null){
//            return null;
//        }
        RegimeVo regimeInfo = regimeInfoMapper.queryRegimeDetail(regimeId);
        BigDecimal limitMoney = regimeInfo.getLimitMoney()==null?BigDecimal.ZERO:regimeInfo.getLimitMoney();
        String limitType = regimeInfo.getLimitType();
        //按天
        BigDecimal  excessMoney=BigDecimal.ZERO;
        if ("T001".equals(limitType)) {
            //根据订单号和当前申请人，得出当前申请人在当天一共申请的单量
            List<OrderInfo> orderInfos = orderInfoMapper.selectOrderInfoByIdAllDay(applyUserId);
            //从订单结算表当中，查询出当前申请人在当天一共申请的单量的金额总和
            BigDecimal sum = BigDecimal.ZERO;
            List<Long> orderIds = orderInfos.stream().map(OrderInfo::getOrderId).collect(Collectors.toList());
            List<OrderSettlingInfo> list = orderSettlingInfoMapper.selectSettingInfoByOrderIds(orderIds);
            sum = list.stream().map(OrderSettlingInfo::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            if (amount!=null&&amount.compareTo(BigDecimal.ZERO)>0){
                sum=sum.add(amount);
            }
            // 当前申请人在当天一共申请的单量的金额总和-限额=超额
            if (sum.compareTo(limitMoney) > 0) {
                BigDecimal subtract = sum.subtract(limitMoney);
                excessMoney=subtract.stripTrailingZeros();
                //总额sum
            }
            // 按次数
        } else if ("T002".equals(limitType)) {
            //判断
            if (amount.compareTo(limitMoney) > 0) {
                BigDecimal subtract = amount.subtract(limitMoney);
                excessMoney=subtract.stripTrailingZeros();
            }
            //不限
        }else{
            return excessMoney;
        }

        return excessMoney;
    }
    @Override
    public int updateOrderPayInfo(OrderPayInfo orderPayInfo) {
        return orderPayInfoMapper.updateOrderPayInfo(orderPayInfo);
    }

    @Override
    public OrderPayInfo getOrderPayInfoByPayId(String payId) {
        return orderPayInfoMapper.getOrderPayInfoByPayId(payId);
    }

}
