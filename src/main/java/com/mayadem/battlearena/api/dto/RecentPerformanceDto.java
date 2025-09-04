package com.mayadem.battlearena.api.dto;

import java.util.Map; 

import com.mayadem.battlearena.api.dto.enums.PerformanceTrend;

public record RecentPerformanceDto(
    int battlesLast30Days,
    int victoriesLast30Days,
    double winRateLast30Days,
    int rankPointsChangeLast30Days,
    PerformanceTrend performanceTrend,
    Map<String, Integer> dailyStats
) {}