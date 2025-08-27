package com.mayadem.battlearena.api.dto;

public class LeaderboardStatsDto {
    private long totalActiveWarriors;
    private double averageRankPoints;
    private int highestRankPoints;
    private String topWarriorUsername;

    public long getTotalActiveWarriors() {
        return totalActiveWarriors;
    }

    public void setTotalActiveWarriors(long totalActiveWarriors) {
        this.totalActiveWarriors = totalActiveWarriors;
    }

    public double getAverageRankPoints() {
        return averageRankPoints;
    }

    public void setAverageRankPoints(double averageRankPoints) {
        this.averageRankPoints = averageRankPoints;
    }

    public int getHighestRankPoints() {
        return highestRankPoints;
    }

    public void setHighestRankPoints(int highestRankPoints) {
        this.highestRankPoints = highestRankPoints;
    }

    public String getTopWarriorUsername() {
        return topWarriorUsername;
    }

    public void setTopWarriorUsername(String topWarriorUsername) {
        this.topWarriorUsername = topWarriorUsername;
    }
}
