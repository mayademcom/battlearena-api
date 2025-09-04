package com.mayadem.battlearena.api.dto;

import com.mayadem.battlearena.api.dto.enums.StreakType;

public record OverallStatsDto(
    int totalBattles,
    int victories,
    int defeats,
    int draws,
    double winRate,
    int bestScore,
    double averageScore,
    int totalRankPointsGained,
    int currentStreak,
    StreakType streakType,
    int longestWinStreak
) {}