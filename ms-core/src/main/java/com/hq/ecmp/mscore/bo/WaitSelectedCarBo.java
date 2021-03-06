package com.hq.ecmp.mscore.bo;

import com.hq.ecmp.constant.CarPowerEnum;
import com.hq.ecmp.constant.enumerate.*;
import com.hq.ecmp.mscore.domain.CarInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;

/**
 * 等待被选择的车辆对象
 *
 * @Author: zj.hu
 * @Date: 2020-03-17 21:57
 */
@Data
public class WaitSelectedCarBo extends CarInfo implements Comparable<WaitSelectedCarBo> {

    /**
     * 车辆分类：
     *
     * A-前后无任务
     * B-车辆仅有前任务
     * C-车辆仅有后任务
     * D-车辆前后都有任务
     */
    private String type;
    /**
     * 优先值 最低 1
     */
    private int priority;

    /**
     * 车辆锁定状态
     * 0000   未锁定
     * 1111   已锁定
     */
    private String lockState;

    /**
     * 任务是否冲突，用于自动派单
     * 需要查询 车辆 前后任务  结束 和 开始 时间进行判断,
     * 100 前任务冲突
     * 001 后任务冲突
     * 101 前后任务都冲突
     * 000 无任务冲突
     *
     */
    private TaskConflictEnum  taskConflict;

    /**
     * 任务流情况
     * 查询车辆是否有前置和 后置任务，仅限当日
     * 000  前后都没有任务
     * 100  前面有任务
     * 001  后面有任务
     * 101  前后都有任务
     *
     */
    private String  taskStream;


    /**
     * 前续 冲突的任务 订单编号
     */
    private Long beforeClashTaskOrderId;
    /**
     * 前续个任务结束时间 这里是不冲突的任务
     */
    private Timestamp beforeTaskEndTime;

    /**
     * 前续个任务订单编号，这里是不冲突的任务
     */
    private Long beforeTaskOrderId;

    /**
     * 后续 冲突的任务 订单编号
     */
    private Long afterClashTaskOrderId;
    /**
     * 后续个任务开始时间 这里是不冲突的任务
     */
    private Timestamp afterTaskBeginTime;

    /**
     * 后续任务订单编号,这里是不冲突的任务
     */
    private Long  afterTaskOrderId;

    private String carModelName;
    /**
     * 车辆级别
     */
    private String level;

    /**
     * 车型图片
     */
    private String carTypeImage;

    /**
     * 车辆颜色
     */
    private String color;

    /**
     * 车牌号
     */
    private String plateLicence;

    /**
     * 车队名称
     */
    private String carGroupName;

    /**
     * 所属部门名称
     */
    private String deptName;



    /**
     *
     * 综合状态： 根据各种状态综合判断
     *
     */
    private String status="";

    /**
     * 车型级别是否匹配
     * 0000 匹配
     * 1111 不匹配  跨级调度
     *
     */
    private String levelIsMatch;



    /**
     * 等于零 则o1=o2
     * 大于零 则o1>o2
     * 小于零 则o1<o2
     */
    @Override
    public int compareTo(WaitSelectedCarBo o) {

        return this.priority - o.priority;
    }

    public void   embellish(){
        if(CarStateEnum.EFFECTIVE.getCode().equals(this.getState())){
            this.status=CarStateEnum.EFFECTIVE.getDesc();
        }else{
            this.status="不可用";
        }
//        if(CarStateEnum.NONEFFECTIVE.getCode().equals(this.getState())){
//            this.status=CarStateEnum.NONEFFECTIVE.getDesc();
//        }
//        if(CarStateEnum.MAINTENANCE.getCode().equals(this.getState())){
//            this.status=CarStateEnum.MAINTENANCE.getDesc();
//        }
//        if(CarStateEnum.EXPIRE.getCode().equals(this.getState())){
//            this.status=CarStateEnum.EXPIRE.getDesc();
//        }
//        if(CarStateEnum.WAS_BORROWED.getCode().equals(this.getState())){
//            this.status=CarStateEnum.WAS_BORROWED.getDesc();
//        }


        if(CarLockStateEnum.UNLOCK.getCode().equals(this.lockState)){
//            this.status=CarLockStateEnum.UNLOCK.getDesc()+","+this.status;
        }else {
            this.status="不可用";
        }



        if(TaskConflictEnum.CONFLICT_FREE.equals(this.getTaskConflict())){
//            this.status=TaskConflictEnum.CONFLICT_FREE.getDesc()+","+this.status;
        }else{
            this.status="不可用";
        }
//        if(TaskConflictEnum.BEFORE_TASK_CLASH.equals(this.getTaskConflict())){
//            this.status=TaskConflictEnum.BEFORE_TASK_CLASH.getDesc()+","+this.status;
//        }
//        if(TaskConflictEnum.AFTER_TASK_CLASH.equals(this.getTaskConflict())){
//            this.status=TaskConflictEnum.AFTER_TASK_CLASH.getDesc()+","+this.status;
//        }
//        if(TaskConflictEnum.BEFORE_AND_AFTER_TASK_CLASH.equals(this.getTaskConflict())){
//            this.status=TaskConflictEnum.BEFORE_AND_AFTER_TASK_CLASH.getDesc()+","+this.status;
//        }

        //是否跨级调度
        if(CarLevelMatchEnum.UN_MATCH.getCode().equals(this.getLevelIsMatch()) && "可派".equals( this.status)){
            this.status=CarLevelMatchEnum.UN_MATCH.getDesc();
        }




        //能源类型转换-枚举问题
        if(StringUtils.isNotEmpty(this.getPowerType())){
            if(CarPowerEnum.gasoline.getKey().equals(this.getPowerType())){
                this.setPowerType(CarPowerEnum.gasoline.getDesc());
            }
            if(CarPowerEnum.Diesel.getKey().equals(this.getPowerType())){
                this.setPowerType(CarPowerEnum.Diesel.getDesc());
            }
            if(CarPowerEnum.ELECTRIC.getKey().equals(this.getPowerType())){
                this.setPowerType(CarPowerEnum.ELECTRIC.getDesc());
            }
            if(CarPowerEnum.Hybrid.getKey().equals(this.getPowerType())){
                this.setPowerType(CarPowerEnum.Hybrid.getDesc());
            }
        }

        //车辆性质转换
        if(StringUtils.isNotEmpty(this.getSource())){
            if(CarSourceEnum.OWN.getCode().equals(this.getSource())){
                this.setSource(CarSourceEnum.OWN.getDesc());
            }
            if(CarSourceEnum.RENT.getCode().equals(this.getSource())){
                this.setSource(CarSourceEnum.RENT.getDesc());
            }
            if(CarSourceEnum.BORROWED.getCode().equals(this.getSource())){
                this.setSource(CarSourceEnum.BORROWED.getDesc());
            }
        }




    }
}
