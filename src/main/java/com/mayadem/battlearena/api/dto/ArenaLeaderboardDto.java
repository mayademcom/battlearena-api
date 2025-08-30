package com.mayadem.battlearena.api.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ArenaLeaderboardDto {
    private List<LeaderboardEntryDto> topWarriors; // İlk 100 oyuncu
    private Integer currentWarriorPosition; // Kullanıcının sırası
    private LeaderboardStatsDto globalStats; // Genel istatistikler
    private LocalDateTime lastUpdated;

    public List<LeaderboardEntryDto> getTopWarriors() {
        return topWarriors;
    }

    public void setTopWarriors(List<LeaderboardEntryDto> topWarriors) {
        this.topWarriors = topWarriors;
    }

    public Integer getCurrentWarriorPosition() {
        return currentWarriorPosition;
    }

    public void setCurrentWarriorPosition(Integer currentWarriorPosition) {
        this.currentWarriorPosition = currentWarriorPosition;
    }

    public LeaderboardStatsDto getGlobalStats() {
        return globalStats;
    }

    public void setGlobalStats(LeaderboardStatsDto globalStats) {
        this.globalStats = globalStats;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

}
