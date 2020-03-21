package com.hq.ecmp.mscore.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.DateUtils;
import com.hq.common.utils.OkHttpUtil;
import com.hq.ecmp.constant.*;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.dto.ContactorDto;
import com.hq.ecmp.mscore.dto.IsContinueReDto;
import com.hq.ecmp.mscore.dto.OrderViaInfoDto;
import com.hq.ecmp.mscore.service.*;
import com.hq.ecmp.util.MacTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @ClassName DriverOrderServiceImpl
 * @Description TODO
 * @Author yj
 * @Date 2020/3/10 14:36
 * @Version 1.0
 */
@Service
@Slf4j
public class DriverOrderServiceImpl implements IDriverOrderService {

    @Resource
    IOrderInfoService iOrderInfoService;

    @Resource
    IOrderStateTraceInfoService iOrderStateTraceInfoService;

    @Resource
    IOrderWaitTraceInfoService iOrderWaitTraceInfoService;

    @Resource
    IOrderSettlingInfoService iOrderSettlingInfoService;

    @Resource
    IJourneyPassengerInfoService iJourneyPassengerInfoService;

    @Resource
    ICarInfoService iCarInfoService;

    @Resource
    IDriverInfoService iDriverInfoService;

    @Resource
    IOrderViaInfoService iOrderViaInfoService;

    @Resource
    ICarGroupInfoService iCarGroupInfoService;

    @Resource
    IOrderAddressInfoService iOrderAddressInfoService;


    @Value("${thirdService.enterpriseId}") //企业编号
    private String enterpriseId;

    @Value("${thirdService.licenseContent}") //企业证书信息
    private String licenseContent;

    @Value("${thirdService.apiUrl}")//三方平台的接口前地址
    private String apiUrl;


    @Override
    @Transactional(rollbackFor = Exception.class)
        public void handleDriverOrderStatus(String type, String currentPoint, String orderNo,Long userId) throws Exception {
        Double longitude = null;
        Double latitude = null;
        if(currentPoint!=null && !currentPoint.equals("")){
            String[] point = currentPoint.split("\\,| \\，");
            longitude = Double.parseDouble(point[0]);
            longitude =  Double.parseDouble(point[1]);
        }

        long orderId = Long.parseLong(orderNo);
        //更新前订单信息
        OrderInfo orderInfoOld = iOrderInfoService.selectOrderInfoById(orderId);
        //订单
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderId(orderId);
        orderInfo.setUpdateBy(String.valueOf(userId));
        //订单轨迹
        OrderStateTraceInfo orderStateTraceInfo = new OrderStateTraceInfo();
        orderStateTraceInfo.setOrderId(orderId);
        orderStateTraceInfo.setDriverLongitude(longitude);
        orderStateTraceInfo.setDriverLatitude(latitude);
        orderStateTraceInfo.setCreateBy(String.valueOf(userId));
        if(DriverBehavior.PICKUP_PASSENGER.getType().equals(type)){
            //订单状态
            orderInfo.setState(OrderState.REASSIGNMENT.getState());
            iOrderInfoService.updateOrderInfo(orderInfo);
            //订单轨迹状态
            orderStateTraceInfo.setState(OrderStateTrace.ALREADY_SET_OUT.getState());
            iOrderStateTraceInfoService.insertOrderStateTraceInfo(orderStateTraceInfo);

        }else if(DriverBehavior.ARRIVE.getType().equals(type)){
            //订单状态
            orderInfo.setState(OrderState.READYSERVICE.getState());
            iOrderInfoService.updateOrderInfo(orderInfo);
            //订单轨迹状态
            orderStateTraceInfo.setState(OrderStateTrace.PRESERVICE.getState());
            iOrderStateTraceInfoService.insertOrderStateTraceInfo(orderStateTraceInfo);
        }else if((DriverBehavior.START_SERVICE.getType().equals(type))){

            //TODO 此处需要根据经纬度去云端的接口获取长地址和短地址存入订单表
            List<String> macList = MacTools.getMacList();
            String macAdd = macList.get(0);
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("enterpriseId", enterpriseId);
            paramMap.put("licenseContent", licenseContent);
            paramMap.put("mac", macAdd);
            paramMap.put(" longitude", longitude+"");
            paramMap.put("latitude",latitude+"");
            String result = OkHttpUtil.postJson(apiUrl + "/service/locateByLongitudeAndLatitude", paramMap);
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (ApiResponse.SUCCESS_CODE!=jsonObject.getInteger("code")) {
                log.error("调用云端经纬度获取长短地址接口失败");
            }
            JSONObject data = jsonObject.getJSONObject("data");
            String longAddr = data.getString("addressFullName");
            String shortAddr = data.getString("maddress");
            //订单地址表
            Long setOutOrderAddressId = null;
            OrderAddressInfo orderAddressInfoOld = new OrderAddressInfo();
            orderAddressInfoOld.setOrderId(orderId);
            orderAddressInfoOld.setType(OrderConstant.ORDER_ADDRESS_ACTUAL_SETOUT);
            List<OrderAddressInfo> orderAddressInfos = iOrderAddressInfoService.selectOrderAddressInfoList(orderAddressInfoOld);
            OrderAddressInfo orderAddressInfoCh = orderAddressInfos.get(0);
            setOutOrderAddressId = orderAddressInfoCh.getOrderAddressId();

            OrderAddressInfo orderAddressInfo = new OrderAddressInfo();
            orderAddressInfo.setType(OrderConstant.ORDER_ADDRESS_ACTUAL_SETOUT);
            orderAddressInfo.setOrderId(orderId);
            orderAddressInfo.setJourneyId(orderInfoOld.getJourneyId());
            orderAddressInfo.setNodeId(orderInfoOld.getNodeId());
            orderAddressInfo.setPowerId(orderInfoOld.getPowerId());
            orderAddressInfo.setDriverId(orderInfoOld.getDriverId());
            orderAddressInfo.setCarId(orderInfoOld.getCarId());
            orderAddressInfo.setUserId(orderInfoOld.getUserId()+"");
            orderAddressInfo.setCityPostalCode(null);
            orderAddressInfo.setActionTime(DateUtils.getNowDate());
            orderAddressInfo.setLongitude(longitude);
            orderAddressInfo.setLatitude(latitude);
            orderAddressInfo.setAddress(shortAddr);
            orderAddressInfo.setAddressLong(longAddr);
            orderAddressInfo.setCreateBy(userId+"");
            if(setOutOrderAddressId != null){
                orderAddressInfo.setOrderAddressId(setOutOrderAddressId);
                iOrderAddressInfoService.updateOrderAddressInfo(orderAddressInfo);
            }else{
                iOrderAddressInfoService.insertOrderAddressInfo(orderAddressInfo);
            }
            //订单状态
            orderInfo.setState(OrderState.INSERVICE.getState());
            iOrderInfoService.updateOrderInfo(orderInfo);
            //订单轨迹状态
            orderStateTraceInfo.setState(OrderStateTrace.SERVICE.getState());
            iOrderStateTraceInfoService.insertOrderStateTraceInfo(orderStateTraceInfo);
        }else if((DriverBehavior.SERVICE_COMPLETION.getType().equals(type))){
            //TODO 此处需要根据经纬度去云端的接口获取长地址和短地址存入订单表
            List<String> macList = MacTools.getMacList();
            String macAdd = macList.get(0);
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("enterpriseId", enterpriseId);
            paramMap.put("licenseContent", licenseContent);
            paramMap.put("mac", macAdd);
            paramMap.put(" longitude", longitude+"");
            paramMap.put("latitude",latitude+"");
            String result = OkHttpUtil.postJson(apiUrl + "/service/locateByLongitudeAndLatitude", paramMap);
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (ApiResponse.SUCCESS_CODE!=jsonObject.getInteger("code")) {
                log.error("调用云端经纬度获取长短地址接口失败");
            }
            JSONObject data = jsonObject.getJSONObject("data");
            String longAddr = data.getString("addressFullName");
            String shortAddr = data.getString("maddress");
            //订单地址表
            Long arriveOutOrderAddressId = null;
            OrderAddressInfo orderAddressInfoOld = new OrderAddressInfo();
            orderAddressInfoOld.setOrderId(orderId);
            orderAddressInfoOld.setType(OrderConstant.ORDER_ADDRESS_ACTUAL_ARRIVE);
            List<OrderAddressInfo> orderAddressInfos = iOrderAddressInfoService.selectOrderAddressInfoList(orderAddressInfoOld);
            OrderAddressInfo orderAddressInfoCh = orderAddressInfos.get(0);
            arriveOutOrderAddressId = orderAddressInfoCh.getOrderAddressId();
            OrderAddressInfo orderAddressInfo = new OrderAddressInfo();
            orderAddressInfo.setType(OrderConstant.ORDER_ADDRESS_ACTUAL_ARRIVE);
            orderAddressInfo.setOrderId(orderId);
            orderAddressInfo.setJourneyId(orderInfoOld.getJourneyId());
            orderAddressInfo.setNodeId(orderInfoOld.getNodeId());
            orderAddressInfo.setPowerId(orderInfoOld.getPowerId());
            orderAddressInfo.setDriverId(orderInfoOld.getDriverId());
            orderAddressInfo.setCarId(orderInfoOld.getCarId());
            orderAddressInfo.setUserId(orderInfoOld.getUserId()+"");
            orderAddressInfo.setCityPostalCode(null);
            orderAddressInfo.setActionTime(DateUtils.getNowDate());
            orderAddressInfo.setLongitude(longitude);
            orderAddressInfo.setLatitude(latitude);
            orderAddressInfo.setAddress(shortAddr);
            orderAddressInfo.setAddressLong(longAddr);
            orderAddressInfo.setCreateBy(userId+"");
            if(arriveOutOrderAddressId != null){
                orderAddressInfo.setOrderAddressId(arriveOutOrderAddressId);
                iOrderAddressInfoService.updateOrderAddressInfo(orderAddressInfo);
            }else{
                iOrderAddressInfoService.insertOrderAddressInfo(orderAddressInfo);
            }
            orderInfo.setState(OrderState.STOPSERVICE.getState());
            iOrderInfoService.updateOrderInfo(orderInfo);
            //订单轨迹状态
            orderStateTraceInfo.setState(OrderStateTrace.SERVICEOVER.getState());
            iOrderStateTraceInfoService.insertOrderStateTraceInfo(orderStateTraceInfo);
        }else{
            throw new Exception("操作类型有误");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public IsContinueReDto isContinue(String mileage, String travelTime, String orderNo,String userId) {
        long orderId = Long.parseLong(orderNo);
        //添加里程数和总时长
        OrderSettlingInfo orderSettlingInfo = new OrderSettlingInfo();
        orderSettlingInfo.setOrderId(orderId);
        orderSettlingInfo.setTotalMileage(new BigDecimal(mileage).stripTrailingZeros());
        orderSettlingInfo.setTotalTime(Integer.parseInt(travelTime));
        orderSettlingInfo.setCreateBy(userId);
        iOrderSettlingInfoService.insertOrderSettlingInfo(orderSettlingInfo);
        //判断是还车还是继续用车
        OrderInfo orderInfoOri = iOrderInfoService.selectOrderInfoById(orderId);
        //车辆
        Long carId = orderInfoOri.getCarId();
        //驾驶员
        Long driverId = orderInfoOri.getDriverId();
        //查询此驾驶员的订单
        OrderDriverListInfo nextTaskWithDriver = iOrderInfoService.getNextTaskWithDriver(driverId);
        OrderDriverListInfo nextTaskWithCar = iOrderInfoService.getNextTaskWithCar(carId);
        IsContinueReDto isContinueReDto = new IsContinueReDto();
        //还车
        if(nextTaskWithDriver == null || nextTaskWithDriver.getCarId()!=carId || nextTaskWithCar.getDriverId()!=driverId){
            isContinueReDto.setIsCallBack(1);
            if(nextTaskWithCar != null && nextTaskWithCar.getDriverId()!=driverId){
                isContinueReDto.setIsCarUse(1);
                isContinueReDto.setUseDateTime(nextTaskWithCar.getUseCarTime());
            }else {
                isContinueReDto.setIsCarUse(2);
            }
        }
        //继续用车
        if(nextTaskWithDriver!=null && nextTaskWithCar!=null && nextTaskWithDriver.getCarId()==carId
        && nextTaskWithCar.getDriverId()==driverId){
            isContinueReDto.setNextTaskDetailWithDriver(nextTaskWithDriver);
        }
        return isContinueReDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long waitingOrder(String orderNo, String isFinish, String currentPoint,String userId,String waitingId) throws Exception {
        Long orderId = Long.parseLong(orderNo);
        String[] point = currentPoint.split("\\,| \\，");
        Double longitude = Double.parseDouble(point[0]);
        Double latitude = Double.parseDouble(point[1]);
        if(CommonConstant.START.equals(isFinish)){
            OrderInfo orderInfo = iOrderInfoService.selectOrderInfoById(orderId);
            OrderWaitTraceInfo orderWaitTraceInfo = new OrderWaitTraceInfo();
            orderWaitTraceInfo.setOrderId(orderId);
            orderWaitTraceInfo.setJourneyId(orderInfo.getJourneyId());
            orderWaitTraceInfo.setDirverId(orderInfo.getDriverId());
            orderWaitTraceInfo.setCarLicense(orderInfo.getCarLicense());
            orderWaitTraceInfo.setStartTime(DateUtils.getNowDate());
            orderWaitTraceInfo.setLongitude(longitude);
            orderWaitTraceInfo.setLatitude(latitude);
            orderWaitTraceInfo.setCreateBy(userId);
            iOrderWaitTraceInfoService.insertOrderWaitTraceInfo(orderWaitTraceInfo);
            return orderWaitTraceInfo.getTraceId();
        }else if(CommonConstant.FINISH.equals(isFinish)){
            OrderWaitTraceInfo orderWaitTraceInfoQuery = new OrderWaitTraceInfo();
            orderWaitTraceInfoQuery.setOrderId(orderId);
            orderWaitTraceInfoQuery.setTraceId(Long.parseLong(waitingId));
            iOrderWaitTraceInfoService.updateOrderWaitTraceInfo(orderWaitTraceInfoQuery);
            OrderWaitTraceInfo orderWaitTraceInfo = iOrderWaitTraceInfoService.selectOrderWaitTraceInfoById(Long.parseLong(waitingId));
            Date startTime = orderWaitTraceInfo.getStartTime();
            Long duration = (DateUtils.getNowDate().getTime()-startTime.getTime())/1000;
            OrderWaitTraceInfo orderWaitTraceInfoParam = new OrderWaitTraceInfo();
            orderWaitTraceInfoParam.setEndTime(DateUtils.getNowDate());
            orderWaitTraceInfoParam.setDuration(duration);
            orderWaitTraceInfoParam.setUpdateBy(userId);
            iOrderWaitTraceInfoService.updateOrderWaitTraceInfo(orderWaitTraceInfoParam);
            return null;
        }else{
            throw new Exception("操作异常");
        }
    }

    @Override
    public List<JourneyPassengerInfo> getInfoWithPassenger(String orderId) {
        OrderInfo orderInfo = iOrderInfoService.selectOrderInfoById(Long.parseLong(orderId));
        Long journeyId = orderInfo.getJourneyId();
        JourneyPassengerInfo journeyPassengerInfo = new JourneyPassengerInfo();
        journeyPassengerInfo.setJourneyId(journeyId);
        journeyPassengerInfo.setItIsPeer("01");
        List<JourneyPassengerInfo> journeyPassengerInfos = iJourneyPassengerInfoService.selectJourneyPassengerInfoList(journeyPassengerInfo);
        return journeyPassengerInfos;
    }

    @Override
    public List<ContactorDto> getInfoWithCarGroup(String orderId) {
        List<ContactorDto> result = new ArrayList();
        OrderInfo orderInfo = iOrderInfoService.selectOrderInfoById(Long.parseLong(orderId));
        Long carId = orderInfo.getCarId();
        CarInfo carInfo = iCarInfoService.selectCarInfoById(carId);
        Long carGroupId = carInfo.getCarGroupId();
        CarGroupInfo carGroupInfo = iCarGroupInfoService.selectCarGroupInfoById(carGroupId);
        String telephone = carGroupInfo.getTelephone();
        //車隊座機
        ContactorDto contactorDtoCarGr = new ContactorDto();
        contactorDtoCarGr.setRoleName(CommonConstant.CARGROUP_PHONE_ROLE);
        contactorDtoCarGr.setPhone(telephone);
        contactorDtoCarGr.setName(CommonConstant.CARGROUP_PHONE_ROLE);
        result.add(contactorDtoCarGr);
        //调度员
        OrderStateTraceInfo orderStateTraceInfo = new OrderStateTraceInfo();
        orderStateTraceInfo.setOrderId(Long.parseLong(orderId));
        orderStateTraceInfo.setState(OrderStateTrace.SENDCAR.getState());
        List<OrderStateTraceInfo> orderStateTraceInfos = iOrderStateTraceInfoService.selectOrderStateTraceInfoList(orderStateTraceInfo);
        OrderStateTraceInfo orderStateTraceInfoSc = orderStateTraceInfos.get(0);

        String createBy = orderStateTraceInfoSc.getCreateBy();
        DriverInfo driverInfo = new DriverInfo();
        driverInfo.setUserId(Long.parseLong(createBy));
        List<DriverInfo> driverInfos = iDriverInfoService.selectDriverInfoList(driverInfo);
        DriverInfo driverInfoChild = driverInfos.get(0);
        String mobile = driverInfoChild.getMobile();
        String driverName = driverInfoChild.getDriverName();
        ContactorDto contactorDto = new ContactorDto();
        contactorDto.setRoleName(CommonConstant.DISPATCHER_ROLE);
        contactorDto.setPhone(mobile);
        contactorDto.setName(driverName);
        result.add(contactorDto);
        return result;
    }

    @Override
    public List<OrderViaInfoDto> getOrderViaInfos(String orderId) {
        OrderViaInfo orderViaInfo = new OrderViaInfo();
        orderViaInfo.setOrderId(Long.parseLong(orderId));
        List<OrderViaInfoDto> orderViaInfoDtos = new ArrayList<>();
        List<OrderViaInfo> orderViaInfos = iOrderViaInfoService.selectOrderViaInfoList(orderViaInfo);
        for (int i = 0; i < orderViaInfos.size(); i++) {
            OrderViaInfo orderViaInfoCh = orderViaInfos.get(i);
            OrderViaInfoDto orderViaInfoDto = new OrderViaInfoDto();
            orderViaInfoDto.setViaId(orderViaInfoCh.getViaId());
            orderViaInfoDto.setFullAddress(orderViaInfoCh.getFullAddress());
            orderViaInfoDto.setLatitude(orderViaInfoCh.getLatitude());
            orderViaInfoDto.setLongitude(orderViaInfoCh.getLongitude());
            orderViaInfoDto.setShortAddress(orderViaInfoCh.getShortAddress());
            orderViaInfoDto.setSortNumber(orderViaInfoCh.getSortNumber());
            orderViaInfoDto.setOrderId(Long.parseLong(orderId));
            orderViaInfoDtos.add(orderViaInfoDto);
        }
        return orderViaInfoDtos;
    }
}