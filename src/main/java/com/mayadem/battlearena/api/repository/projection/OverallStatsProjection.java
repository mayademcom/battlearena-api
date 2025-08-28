package com.mayadem.battlearena.api.repository.projection;

public record OverallStatsProjection(
    long totalBattles,
    long victories,
    long defeats,
    long draws,
    Double winRate, 
    Integer bestScore, 
    Double averageScore,
    Long totalRankPointsGained
) {}