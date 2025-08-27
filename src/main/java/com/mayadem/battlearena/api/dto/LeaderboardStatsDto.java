package com.mayadem.battlearena.api.dto;

public class LeaderboardStatsDto {
    private long totalActiveWarriors;
    private double averageRankPoints;
    private int highestRankPoints;
    private String topWarriorUsername;

    public LeaderboardStatsDto(long totalActiveWarriors,
            double averageRankPoints,
            int highestRankPoints,
            String topWarriorUsername) {
        this.totalActiveWarriors = totalActiveWarriors;
        this.averageRankPoints = averageRankPoints;
        this.highestRankPoints = highestRankPoints;
        this.topWarriorUsername = topWarriorUsername;
    }

    public long getTotalActiveWarriors() {
        return totalActiveWarriors;
    }

    public double getAverageRankPoints() {
        return averageRankPoints;
    }

    public int getHighestRankPoints() {
        return highestRankPoints;
    }

    public String getTopWarriorUsername() {
        return topWarriorUsername;
    }
}
