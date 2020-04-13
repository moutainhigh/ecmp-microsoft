package com.hq.ecmp.mscore.service;

import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.mscore.dto.dispatch.*;
import com.hq.ecmp.mscore.vo.DispatchResultVo;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * @Author: zj.hu
 * @Date: 2020-03-17 22:53
 */
public interface IDispatchService {

    /**
     * 调度-获取可选择的车辆
     * @param dispatchSelectCarDto dispatchSelectCarDto
     * @return ApiResponse<DispatchResultVo>
     */
    ApiResponse<DispatchResultVo> getWaitSelectedCars(DispatchSelectCarDto dispatchSelectCarDto);

    /**
     * 调度-锁定选择的车辆
     * @param dispatchLockCarDto  dispatchLockCarDto
     * @return ApiResponse
     */
    ApiResponse lockSelectedCar(DispatchLockCarDto dispatchLockCarDto);

    /**
     * 调度-解除锁定选择的车辆
     * @param dispatchLockCarDto  dispatchLockCarDto
     * @return ApiResponse
     */
    ApiResponse unlockSelectedCar(DispatchLockCarDto dispatchLockCarDto);


    /**
     * 调度-获取可选择的司机
     * @param dispatchSelectDriverDto  dispatchSelectDriverDto
     * @return ApiResponse<DispatchResultVo>
     */

    ApiResponse<DispatchResultVo> getWaitSelectedDrivers(DispatchSelectDriverDto dispatchSelectDriverDto);

    /**
     *  调度-锁定选择的司机
     * @param dispatchLockDriverDto dispatchLockDriverDto
     * @return ApiResponse
     */
    ApiResponse lockSelectedDriver(DispatchLockDriverDto dispatchLockDriverDto);

    /**
     *  调度-解除锁定选择的司机
     * @param dispatchLockDriverDto dispatchLockDriverDto
     * @return ApiResponse
     */
    ApiResponse unlockSelectedDriver(DispatchLockDriverDto dispatchLockDriverDto);

    /**
     * 自动调度
     * @param dispatchCountCarAndDriverDto dispatchCountCarAndDriverDto
     * @return ApiResponse<DispatchResultVo>
     */
    ApiResponse<DispatchResultVo> autoDispatch(@RequestBody DispatchCountCarAndDriverDto dispatchCountCarAndDriverDto);
}
