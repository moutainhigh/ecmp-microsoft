package com.hq.ecmp.constant.enumerate;

import lombok.Getter;
import lombok.Setter;

/**
 * 任务冲突状况 枚举
 * @Author: zj.hu
 * @Date: 2020-03-30 11:40
 */
public enum TaskConflictEnum {
    /**
     * 前序任务冲突
     */
    BEFORE_TASK_CLASH("100","不可用"),
    /**
     * 后序任务冲突
     */
    AFTER_TASK_CLASH("001","不可用"),
    /**
     * 前序后序任务皆冲突
     */
    BEFORE_AND_AFTER_TASK_CLASH("101","不可用"),

    /**
     * 无任务冲突
     */
    CONFLICT_FREE("000","可用");

    @Setter
    @Getter
    private String code;

    @Setter
    @Getter
    private String desc;

    TaskConflictEnum(String code,String desc){
        this.code=code;
        this.desc=desc;
    }

}
