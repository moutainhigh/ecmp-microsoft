package com.hq.ecmp.ms.api.controller.settle;


import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.mscore.domain.InvoiceAddress;
import com.hq.ecmp.mscore.domain.OrderSettlingInfo;
import com.hq.ecmp.mscore.service.IInvoiceAddressService;
import com.hq.ecmp.mscore.service.IOrderSettlingInfoService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 自由车结算
 * @author shixin
 * @date 2020-3-11
 *
 */
 @RestController
 @RequestMapping("/settle")
 @Slf4j
public class settleInfoController {

    @Autowired
    private IOrderSettlingInfoService iOrderSettlingInfoService;

    /**
     * 自有车结算， 端上返回数据
     * @param rderSettlingInfo 实际里程、时长入库
     * @return
     */
    @ApiOperation(value = "settleAddCommit",notes = "新增结算信息",httpMethod = "POST")
    @PostMapping("/settleAddCommit")
     public ApiResponse settleAddCommit(@RequestBody OrderSettlingInfo rderSettlingInfo){
         try {
             iOrderSettlingInfoService.insertOrderSettlingInfo(rderSettlingInfo);
         } catch (Exception e) {
             log.error("业务处理异常", e);
             return ApiResponse.error("新增失败");
         }
         return ApiResponse.success("新增成功");

     }

}
