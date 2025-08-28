package com.mayadem.battlearena.api.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mayadem.battlearena.api.dto.BattleTypeStatsDto;
import com.mayadem.battlearena.api.dto.GlobalComparisonDto;
import com.mayadem.battlearena.api.dto.LeaderboardStatsDto;
import com.mayadem.battlearena.api.dto.OverallStatsDto;
import com.mayadem.battlearena.api.dto.RecentPerformanceDto;
import com.mayadem.battlearena.api.dto.WarriorDetailedStatsDto;
import com.mayadem.battlearena.api.dto.enums.PerformanceTrend;
import com.mayadem.battlearena.api.dto.enums.StreakType;
import com.mayadem.battlearena.api.entity.ArenaLeaderboard;
import com.mayadem.battlearena.api.entity.Warrior;
import com.mayadem.battlearena.api.repository.ArenaLeaderboardRepository;
import com.mayadem.battlearena.api.repository.BattleParticipantRepository;
import com.mayadem.battlearena.api.repository.projection.OverallStatsProjection;
import com.mayadem.battlearena.api.repository.projection.RecentPerformanceProjection;

@Service
public class WarriorStatisticsService {

    private final BattleParticipantRepository battleParticipantRepository;
    private final ArenaLeaderboardRepository arenaLeaderboardRepository;

    public WarriorStatisticsService(BattleParticipantRepository battleParticipantRepository,
            ArenaLeaderboardRepository arenaLeaderboardRepository) {
        this.battleParticipantRepository = battleParticipantRepository;
        this.arenaLeaderboardRepository = arenaLeaderboardRepository;
    }

    @Transactional(readOnly = true)
    public WarriorDetailedStatsDto getDetailedStatsForWarrior(Warrior warrior) {

        OverallStatsDto overallStats = buildOverallStats(warrior);

        RecentPerformanceDto recentPerformance = buildRecentPerformance(warrior, overallStats.winRate());

        List<BattleTypeStatsDto> statsByType = battleParticipantRepository.findStatsByBattleType(warrior);

        Integer currentRank = arenaLeaderboardRepository.findWarriorPosition(warrior.getId())
                .map(ArenaLeaderboard::getRankPosition)
                .orElse(null);

        GlobalComparisonDto globalComparison = buildGlobalComparison();

        return new WarriorDetailedStatsDto(
                warrior.getUsername(),
                warrior.getDisplayName(),
                warrior.getRankPoints(),
                currentRank,
                overallStats,
                statsByType,
                recentPerformance,
                globalComparison);
    }

    private GlobalComparisonDto buildGlobalComparison() {
        LeaderboardStatsDto globalStats = arenaLeaderboardRepository.findGlobalStats();
        return new GlobalComparisonDto(
            globalStats.getAverageRankPoints() != null ? globalStats.getAverageRankPoints() : 0.0,
            0.0, 
            globalStats.getTotalActiveWarriors() != null ? globalStats.getTotalActiveWarriors().intValue() : 0
        );
    }

   
    private OverallStatsDto buildOverallStats(Warrior warrior) {

        OverallStatsProjection stats = battleParticipantRepository.findOverallStatsByWarrior(warrior)
                .orElse(new OverallStatsProjection(0, 0, 0, 0, 0.0, 0, 0.0, 0L));

        Object[] streakInfo = battleParticipantRepository.findStreakInfoByWarrior(warrior.getId());

        int currentStreak = 0;
        StreakType streakType = StreakType.NONE;
        int longestWinStreak = 0;

        if (streakInfo != null && streakInfo.length > 0 && streakInfo[0] != null && streakInfo[1] != null) {
            currentStreak = ((Number) streakInfo[0]).intValue();
            streakType = StreakType.valueOf((String) streakInfo[1]);
            longestWinStreak = ((Number) streakInfo[2]).intValue();
        }

        return new OverallStatsDto(
                (int) stats.totalBattles(),
                (int) stats.victories(),
                (int) stats.defeats(),
                (int) stats.draws(),
                stats.winRate() != null ? stats.winRate() : 0.0,
                stats.bestScore() != null ? stats.bestScore() : 0,
                stats.averageScore() != null ? stats.averageScore() : 0.0,
                stats.totalRankPointsGained() != null ? stats.totalRankPointsGained().intValue() : 0,
                currentStreak,
                streakType,
                longestWinStreak);
    }

    
    private RecentPerformanceDto buildRecentPerformance(Warrior warrior, double overallWinRate) {

        Instant since = Instant.now().minus(30, ChronoUnit.DAYS);

        RecentPerformanceProjection recentStats = battleParticipantRepository.findRecentStats(warrior, since)
                .orElse(new RecentPerformanceProjection(0, 0, 0L));

        long battles = recentStats.battles();
        long victories = recentStats.victories();
        long rankPointsChange = recentStats.rankPointsChange() != null ? recentStats.rankPointsChange() : 0L;

        double winRateLast30Days = (battles > 0) ? ((double) victories / battles) * 100.0 : 0.0;
        PerformanceTrend trend;
        double difference = winRateLast30Days - overallWinRate;

        if (battles < 10) {
            trend = PerformanceTrend.STEADY;
        } else if (difference > 5) {
            trend = PerformanceTrend.IMPROVING;
        } else if (difference < -5) {
            trend = PerformanceTrend.DECLINING;
        } else {
            trend = PerformanceTrend.STEADY;
        }

        return new RecentPerformanceDto(
                (int) battles,
                (int) victories,
                Math.round(winRateLast30Days * 100.0) / 100.0,
                (int) rankPointsChange,
                trend,
                Collections.emptyMap());
    }
}