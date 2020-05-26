package com.hq.ecmp.mscore.service.impl;

import com.hq.ecmp.constant.OrderServiceType;
import com.hq.ecmp.mscore.domain.CostConfigInfo;
import com.hq.ecmp.mscore.domain.OrderSettlingInfoVo;
import com.hq.ecmp.mscore.dto.cost.CostConfigListResult;
import com.hq.ecmp.mscore.service.CostCalculation;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 成本计算器
 */
public class CostCalculator implements CostCalculation {

    private BigDecimal getBigDecimal(BigDecimal fee){
        if(fee==null){
            return BigDecimal.ZERO;
        }else {
            return fee;
        }
    }
    /**
     * 成本计算
     * @param costConfigInfoList
     * @return
     */
    @Override
    public OrderSettlingInfoVo calculator(List<CostConfigInfo> costConfigInfoList, OrderSettlingInfoVo orderSettlingInfoVo) {
        OrderSettlingInfoVo orderSettlingInfo = new OrderSettlingInfoVo();
        //服务类型
        String serviceType = orderSettlingInfoVo.getServiceType();
        //实际行程时间= 总时长 - 等待时长
        BigDecimal  waitingTime = new BigDecimal(orderSettlingInfoVo.getTotalTime()).subtract(orderSettlingInfoVo.getWaitingTime());
        //所有的费用
        BigDecimal amount ;
        //订单里程的总价格
        BigDecimal totalPrice = BigDecimal.ZERO;
        //起步价
        BigDecimal startingPrice = BigDecimal.ZERO;
        //套餐价
        BigDecimal  packagePrice  = BigDecimal.ZERO;
        //超里程价格
        BigDecimal  overMileagePrice  = BigDecimal.ZERO;
        //超时长价格
        BigDecimal  overtimeLongPrice  = BigDecimal.ZERO;
        //等待费
        BigDecimal  waitingFee  =BigDecimal.ZERO;
        //路桥费
        BigDecimal  roadBridgeFee  = getBigDecimal(orderSettlingInfoVo.getRoadBridgeFee());
        //其他费用
        BigDecimal otherFee = getBigDecimal(orderSettlingInfoVo.getOtherFee());
        //高速费
        BigDecimal  highSpeedFee  = getBigDecimal(orderSettlingInfoVo.getHighSpeedFee());
        //停车费
        BigDecimal  parkingRateFee  = getBigDecimal(orderSettlingInfoVo.getParkingRateFee());
        //住宿费
        BigDecimal  hotelExpenseFee  =getBigDecimal(orderSettlingInfoVo.getHotelExpenseFee());
        //餐饮费
        BigDecimal  restaurantFee  = getBigDecimal(orderSettlingInfoVo.getRestaurantFee());
        if(serviceType.equals(OrderServiceType.ORDER_SERVICE_TYPE_CHARTERED.getBcState())){
            //包车的情况下
            for (CostConfigInfo costConfigInfo : costConfigInfoList) {
                //套餐价
                packagePrice = costConfigInfo.getCombosPrice();
                //超里程数小于&&等于1  则不计算他的超里程费用
                if(orderSettlingInfoVo.getTotalMileage().compareTo(costConfigInfo.getCombosMileage())==1){
                    //超里程价格=（总里程-包含里程）* 超里程单价
                    overMileagePrice = overMileagePrice.add((orderSettlingInfoVo.getTotalMileage().subtract(costConfigInfo.getCombosMileage())).multiply(costConfigInfo.getBeyondPriceEveryKm()));
                }else {
                    overMileagePrice=overMileagePrice.add(BigDecimal.ZERO);
                }
                //超时长价格小于&&等于1  则不计算他的超时长费用
                if(orderSettlingInfoVo.getTotalTime()-costConfigInfo.getCombosTimes().intValue()<1){
                    overtimeLongPrice= overtimeLongPrice.add(BigDecimal.ZERO);
                }else{
                    //超时长价格=(总时长-包含时长)* 超时长单价
                    overtimeLongPrice= overtimeLongPrice.add(new BigDecimal(orderSettlingInfoVo.getTotalTime()-costConfigInfo.getCombosTimes().intValue()).multiply(costConfigInfo.getBeyondPriceEveryMinute()));
                }
                //总价=（等待时长*等待单价）+(起步价)+(超里程价格)+(超时长价格)
                totalPrice = totalPrice.add(packagePrice.add(waitingFee).add(overMileagePrice).add(overtimeLongPrice));
            }
        }else if(serviceType.equals(OrderServiceType.ORDER_SERVICE_TYPE_APPOINTMENT.getBcState())){
            //预约
            for (CostConfigInfo costConfigInfo : costConfigInfoList) {
                //起步价
                startingPrice=costConfigInfo.getStartPrice();
                // 等待费=（等待时长*等待单价）
                waitingFee=orderSettlingInfoVo.getWaitingTime().multiply(costConfigInfo.getWaitPriceEreryMinute());
                //超里程数小于&&等于1  则不计算他的超里程费用
                if(orderSettlingInfoVo.getTotalMileage().compareTo(costConfigInfo.getCombosMileage())==1){
                    //超里程价格=（总里程-包含里程）* 超里程单价
                    overMileagePrice=overMileagePrice.add((orderSettlingInfoVo.getTotalMileage().subtract(costConfigInfo.getCombosMileage())).multiply(costConfigInfo.getBeyondPriceEveryKm()));
                }else {
                    overMileagePrice=overMileagePrice.add(BigDecimal.ZERO);
                }
                //超时长价格小于&&等于1  则不计算他的超时长费用
                if(orderSettlingInfoVo.getTotalTime()-costConfigInfo.getCombosTimes().intValue()<1){
                    overtimeLongPrice=overtimeLongPrice.add(BigDecimal.ZERO);
                }else{
                    //超时长价格=(总时长-包含时长)* 超时长单价
                    overtimeLongPrice=overtimeLongPrice.add(new BigDecimal(orderSettlingInfoVo.getTotalTime()-costConfigInfo.getCombosTimes().intValue()).multiply(costConfigInfo.getBeyondPriceEveryMinute()));
                }
                totalPrice = totalPrice.add(startingPrice.add(waitingFee).add(overMileagePrice).add(overtimeLongPrice));
            }
        }else if(serviceType.equals(OrderServiceType.ORDER_SERVICE_TYPE_PICK_UP.getBcState())){
            //接机
            for (CostConfigInfo costConfigInfo : costConfigInfoList) {
                //起步价
                startingPrice=costConfigInfo.getStartPrice();
                // 等待费=（等待时长*等待单价）
                waitingFee=orderSettlingInfoVo.getWaitingTime().multiply(costConfigInfo.getWaitPriceEreryMinute());
                //超里程数小于&&等于1  则不计算他的超里程费用
                if(orderSettlingInfoVo.getTotalMileage().compareTo(costConfigInfo.getCombosMileage())==1){
                    //超里程价格=（总里程-包含里程）* 超里程单价
                    overMileagePrice=overMileagePrice.add((orderSettlingInfoVo.getTotalMileage().subtract(costConfigInfo.getCombosMileage())).multiply(costConfigInfo.getBeyondPriceEveryKm()));
                }else {
                    overMileagePrice=overMileagePrice.add(BigDecimal.ZERO);
                }
                //超时长价格小于&&等于1  则不计算他的超时长费用
                if(orderSettlingInfoVo.getTotalTime()-costConfigInfo.getCombosTimes().intValue()<1){
                    overtimeLongPrice=overtimeLongPrice.add(BigDecimal.ZERO);
                }else{
                    //超时长价格=(总时长-包含时长)* 超时长单价
                    overtimeLongPrice=overtimeLongPrice.add(new BigDecimal(orderSettlingInfoVo.getTotalTime()-costConfigInfo.getCombosTimes().intValue()).multiply(costConfigInfo.getBeyondPriceEveryMinute()));
                }
                totalPrice = totalPrice.add(startingPrice.add(waitingFee).add(overMileagePrice).add(overtimeLongPrice));
            }
        }else if(serviceType.equals(OrderServiceType.ORDER_SERVICE_TYPE_SEND.getBcState())){
            //送机
            for (CostConfigInfo costConfigInfo : costConfigInfoList) {
                //起步价
                startingPrice=costConfigInfo.getStartPrice();
                // 等待费=（等待时长*等待单价）
                waitingFee=orderSettlingInfoVo.getWaitingTime().multiply(costConfigInfo.getWaitPriceEreryMinute());
                //超里程数小于&&等于1  则不计算他的超里程费用
                if(orderSettlingInfoVo.getTotalMileage().compareTo(costConfigInfo.getCombosMileage())==1){
                    //超里程价格=（总里程-包含里程）* 超里程单价
                    overMileagePrice=overMileagePrice.add((orderSettlingInfoVo.getTotalMileage().subtract(costConfigInfo.getCombosMileage())).multiply(costConfigInfo.getBeyondPriceEveryKm()));
                }
                //超时长价格小于&&等于1  则不计算他的超时长费用
                if(orderSettlingInfoVo.getTotalTime()-costConfigInfo.getCombosTimes().intValue()>=1){
                    //超时长价格=(总时长-包含时长)* 超时长单价
                    overtimeLongPrice=overtimeLongPrice.add(new BigDecimal(orderSettlingInfoVo.getTotalTime()-costConfigInfo.getCombosTimes().intValue()).multiply(costConfigInfo.getBeyondPriceEveryMinute()));
                }
                totalPrice = totalPrice.add(startingPrice.add(waitingFee).add(overMileagePrice).add(overtimeLongPrice));
            }
        }else if(serviceType.equals(OrderServiceType.ORDER_SERVICE_TYPE_NOW.getBcState())){
            //即时用车
            for (CostConfigInfo costConfigInfo : costConfigInfoList) {
                //起步价
                startingPrice=costConfigInfo.getStartPrice();
                // 等待费=（等待时长*等待单价）
                waitingFee=orderSettlingInfoVo.getWaitingTime().multiply(costConfigInfo.getWaitPriceEreryMinute());
                //超里程数小于&&等于1  则不计算他的超里程费用
                if(orderSettlingInfoVo.getTotalMileage().compareTo(costConfigInfo.getCombosMileage())==1){
                    //超里程价格=（总里程-包含里程）* 超里程单价
                    overMileagePrice=overMileagePrice.add(orderSettlingInfoVo.getTotalMileage().subtract(costConfigInfo.getCombosMileage())).multiply(costConfigInfo.getBeyondPriceEveryKm());
                }
                //超时长价格小于&&等于1  则不计算他的超时长费用
                if(orderSettlingInfoVo.getTotalTime()-costConfigInfo.getCombosTimes().intValue()>=1){
                    //超时长价格=(总时长-包含时长)* 超时长单价
                    overtimeLongPrice=overtimeLongPrice.add(new BigDecimal(orderSettlingInfoVo.getTotalTime()-costConfigInfo.getCombosTimes().intValue()).multiply(costConfigInfo.getBeyondPriceEveryMinute()));
                }
                totalPrice = totalPrice.add(startingPrice.add(waitingFee).add(overMileagePrice).add(overtimeLongPrice));
            }
        }

        //订单各项费用科目和对应费用
        amount=totalPrice
                .add(roadBridgeFee)
                .add(highSpeedFee)
                .add(parkingRateFee
                .add(hotelExpenseFee)
                .add(restaurantFee))
                .add(otherFee);

        //返回所需要的详情
        orderSettlingInfoVo.setStartingPrice(startingPrice);
        orderSettlingInfoVo.setOverMileagePrice(overMileagePrice);
        orderSettlingInfoVo.setOvertimeLongPrice(overtimeLongPrice);
        orderSettlingInfoVo.setWaitingFee(waitingFee);
        orderSettlingInfoVo.setAmount(amount);
        return orderSettlingInfo;
    }
}
