package com.hq.ecmp.constant.enumerate;

import com.google.common.collect.Maps;
import com.hq.ecmp.constant.CarModeEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 车辆来演（车辆性质） 枚举类
 * @Author: zj.hu
 * @Date: 2020-04-07 14:52
 */
public enum CarSourceEnum {

    OWN("S001","车况良好"),
    RENT("S002","禁用中"),
    BORROWED("S003","维护中");

    @Setter
    @Getter
    private String code;

    @Setter
    @Getter
    private String desc;

    CarSourceEnum(String code,String desc){
        this.code=code;
        this.desc=desc;
    }


}