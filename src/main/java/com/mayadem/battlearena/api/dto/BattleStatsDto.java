package com.mayadem.battlearena.api.dto;

public class BattleStatsDto {

    private final long totalBattles;
    private final long victories;
    private final long defeats;
    private final long draws;

    public BattleStatsDto(long totalBattles, long victories, long defeats, long draws) {
        this.totalBattles = totalBattles;
        this.victories = victories;
        this.defeats = defeats;
        this.draws = draws;
    }

    public long getTotalBattles() { return totalBattles; }
    public long getVictories() { return victories; }
    public long getDefeats() { return defeats; }
    public long getDraws() { return draws; }
}