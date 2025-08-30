package com.mayadem.battlearena.api.dto;

public class LeaderboardEntryDto {

    private int rank;
    private Long warriorId;
    private String username;
    private String displayName;
    private int rankPoints;
    private int totalBattles;
    private int victories;
    private int defeats;
    private double winRate;
    private boolean isCurrentWarrior;

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public Long getWarriorId() {
        return warriorId;
    }

    public void setWarriorId(Long warriorId) {
        this.warriorId = warriorId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getRankPoints() {
        return rankPoints;
    }

    public void setRankPoints(int rankPoints) {
        this.rankPoints = rankPoints;
    }

    public int getTotalBattles() {
        return totalBattles;
    }

    public void setTotalBattles(int totalBattles) {
        this.totalBattles = totalBattles;
    }

    public int getVictories() {
        return victories;
    }

    public void setVictories(int victories) {
        this.victories = victories;
    }

    public int getDefeats() {
        return defeats;
    }

    public void setDefeats(int defeats) {
        this.defeats = defeats;
    }

    public double getWinRate() {
        return winRate;
    }

    public void setWinRate(double winRate) {
        this.winRate = winRate;
    }

    public boolean isCurrentWarrior() {
        return isCurrentWarrior;
    }

    public void setCurrentWarrior(boolean isCurrentWarrior) {
        this.isCurrentWarrior = isCurrentWarrior;
    }

}
