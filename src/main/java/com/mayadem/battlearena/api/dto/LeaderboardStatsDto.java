package com.mayadem.battlearena.api.dto;

public class LeaderboardStatsDto {
    private Long totalActiveWarriors;
    private Double averageRankPoints;
    private Integer highestRankPoints;
    private String topWarriorUsername;

    public Long getTotalActiveWarriors() {
        return totalActiveWarriors;
    }

    public void setTotalActiveWarriors(Long totalActiveWarriors) {
        this.totalActiveWarriors = totalActiveWarriors;
    }

    public Double getAverageRankPoints() {
        return averageRankPoints;
    }

    public void setAverageRankPoints(Double averageRankPoints) {
        this.averageRankPoints = averageRankPoints;
    }

    public Integer getHighestRankPoints() {
        return highestRankPoints;
    }

    public void setHighestRankPoints(Integer highestRankPoints) {
        this.highestRankPoints = highestRankPoints;
    }

    public String getTopWarriorUsername() {
        return topWarriorUsername;
    }

    public void setTopWarriorUsername(String topWarriorUsername) {
        this.topWarriorUsername = topWarriorUsername;
    }

    public LeaderboardStatsDto(Long totalActiveWarriors,
            Double averageRankPoints,
            Integer highestRankPoints,
            String topWarriorUsername) {
        this.totalActiveWarriors = totalActiveWarriors;
        this.averageRankPoints = averageRankPoints;
        this.highestRankPoints = highestRankPoints;
        this.topWarriorUsername = topWarriorUsername;
    }

}
