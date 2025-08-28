package com.mayadem.battlearena.api.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mayadem.battlearena.api.dto.OverallStatsDto;
import com.mayadem.battlearena.api.dto.RecentPerformanceDto;
import com.mayadem.battlearena.api.dto.enums.PerformanceTrend;
import com.mayadem.battlearena.api.dto.enums.StreakType;
import com.mayadem.battlearena.api.entity.Warrior;
import com.mayadem.battlearena.api.repository.BattleParticipantRepository;
import com.mayadem.battlearena.api.repository.projection.OverallStatsProjection;
import com.mayadem.battlearena.api.repository.projection.RecentPerformanceProjection;

@Service
public class WarriorStatisticsService {

    private final BattleParticipantRepository battleParticipantRepository;

    public WarriorStatisticsService(BattleParticipantRepository battleParticipantRepository) {
        this.battleParticipantRepository = battleParticipantRepository;
    }

    public void getDetailedStatsForWarrior(Warrior warrior) {

    }

    @Transactional(readOnly = true)
    private OverallStatsDto buildOverallStats(Warrior warrior) {

        OverallStatsProjection stats = battleParticipantRepository.findOverallStatsByWarrior(warrior)
                .orElse(new OverallStatsProjection(0, 0, 0, 0, 0.0, 0, 0.0, 0L));

        Object[] streakInfo = battleParticipantRepository.findStreakInfoByWarrior(warrior.getId());

        int currentStreak = 0;
        StreakType streakType = StreakType.NONE;
        int longestWinStreak = 0;

        if (streakInfo != null && streakInfo.length > 0 && streakInfo[0] != null) {
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

    @Transactional(readOnly = true)
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