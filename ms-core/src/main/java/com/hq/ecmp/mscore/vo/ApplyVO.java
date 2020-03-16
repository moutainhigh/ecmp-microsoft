package com.hq.ecmp.mscore.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: chao.zhang
 * @Date: 2020/3/14 13:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplyVO {

    //申请id
    private Long applyId;

    //行程id
    private Long journeyId;
}
