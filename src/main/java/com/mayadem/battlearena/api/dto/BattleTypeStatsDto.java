package com.mayadem.battlearena.api.dto;

import com.mayadem.battlearena.api.entity.enums.BattleType; 

public record BattleTypeStatsDto(
    BattleType battleType,
    int battles,
    int victories,
    int defeats,
    int draws,
    double winRate,
    int bestScore,
    double averageScore,
    int rankPointsGained
) {}