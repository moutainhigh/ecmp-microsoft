package com.hq.ecmp.mscore.service;

import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.mscore.dto.statistics.StatisticsParam;

public interface StatisticsCostService {
    public ApiResponse cost(StatisticsParam statisticsParam);
}
