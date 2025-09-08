package com.mayadem.battlearena.api.dto;

import java.util.List;

public record WarriorDetailedStatsDto(
    String username,
    String displayName,
    int currentRankPoints,
    Integer currentRank,
    OverallStatsDto overallStats,
    List<BattleTypeStatsDto> statsByType,
    RecentPerformanceDto recentPerformance,
    GlobalComparisonDto globalComparison
) {}