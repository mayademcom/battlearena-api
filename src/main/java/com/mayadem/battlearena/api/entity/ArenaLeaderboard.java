package com.mayadem.battlearena.api.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "arena_leaderboard") // DB’deki view ismi
public class ArenaLeaderboard {

    @Id
    private Long id;

    @Column(name = "rank_position")
    private int rankPosition;

    private String username;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "rank_points")
    private int rankPoints;

    @Column(name = "total_battles")
    private int totalBattles;

    private int victories;

    private int defeats;

    @Column(name = "win_rate")
    private double winRate;

    // Getter & Setter’lar
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getRankPosition() {
        return rankPosition;
    }

    public void setRankPosition(int rankPosition) {
        this.rankPosition = rankPosition;
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
}
