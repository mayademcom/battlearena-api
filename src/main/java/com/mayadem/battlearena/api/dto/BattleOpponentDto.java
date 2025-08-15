package com.mayadem.battlearena.api.dto;

public class BattleOpponentDto {
    private String username;
    private String displayName;
    private int score;
    private String result; // e.g., "WIN" or "LOSS"
    private int rankPointsBefore;
    private int rankPointsAfter;
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
    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }
    public String getResult() {
        return result;
    }
    public void setResult(String result) {
        this.result = result;
    }
    public int getRankPointsBefore() {
        return rankPointsBefore;
    }
    public void setRankPointsBefore(int rankPointsBefore) {
        this.rankPointsBefore = rankPointsBefore;
    }
    public int getRankPointsAfter() {
        return rankPointsAfter;
    }
    public void setRankPointsAfter(int rankPointsAfter) {
        this.rankPointsAfter = rankPointsAfter;
    }
}
