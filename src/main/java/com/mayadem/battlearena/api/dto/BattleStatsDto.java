package com.mayadem.battlearena.api.dto;

public record BattleStatsDto(
        long totalBattles,
        long victories,
        long defeats,
        long draws,
        double winRate,
        int bestScore,
        double averageScore,
        int totalRankPointsGained) {
}