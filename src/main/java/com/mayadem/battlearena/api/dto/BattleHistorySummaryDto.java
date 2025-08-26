package com.mayadem.battlearena.api.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

import com.mayadem.battlearena.api.entity.BattleParticipant;
import com.mayadem.battlearena.api.entity.enums.BattleResult;

public class BattleHistorySummaryDto {

    private long totalBattles;
    private long victories;
    private long defeats;
    private long draws;
    private double winRate;
    private int bestScore;
    private double averageScore;
    private int totalRankPointsGained;

    public static BattleHistorySummaryDto from(List<BattleParticipant> history) {
        BattleHistorySummaryDto summary = new BattleHistorySummaryDto();

        if (history == null || history.isEmpty()) {
            summary.totalBattles = 0;
            summary.victories = 0;
            summary.defeats = 0;
            summary.draws = 0;
            summary.winRate = 0.0;
            summary.bestScore = 0;
            summary.averageScore = 0.0;
            summary.totalRankPointsGained = 0;
            return summary;
        }

        summary.totalBattles = history.size();
        summary.victories = history.stream().filter(p -> p.getResult() == BattleResult.WIN).count();
        summary.defeats = history.stream().filter(p -> p.getResult() == BattleResult.LOSS).count();
        summary.draws = history.stream().filter(p -> p.getResult() == BattleResult.DRAW).count();

        summary.winRate = (summary.totalBattles > 0)
                ? ((double) summary.victories / summary.totalBattles) * 100.0
                : 0.0;

        BigDecimal bd = new BigDecimal(Double.toString(summary.winRate));
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        summary.winRate = bd.doubleValue();

        summary.bestScore = history.stream()
                .mapToInt(BattleParticipant::getFinalScore)
                .max()
                .orElse(0);

        summary.averageScore = history.stream()
                .mapToDouble(BattleParticipant::getFinalScore)
                .average()
                .orElse(0.0);

        summary.totalRankPointsGained = history.stream()
                .map(BattleParticipant::getRankPointsChange)
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
                .sum();

        return summary;
    }

    public long getTotalBattles() {
        return totalBattles;
    }

    public long getVictories() {
        return victories;
    }

    public long getDefeats() {
        return defeats;
    }

    public long getDraws() {
        return draws;
    }

    public double getWinRate() {
        return winRate;
    }

    public int getBestScore() {
        return bestScore;
    }

    public double getAverageScore() {
        return averageScore;
    }

    public int getTotalRankPointsGained() {
        return totalRankPointsGained;
    }
}