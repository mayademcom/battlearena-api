package com.mayadem.battlearena.api.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mayadem.battlearena.api.dto.OverallStatsDto;
import com.mayadem.battlearena.api.dto.enums.StreakType;
import com.mayadem.battlearena.api.entity.Warrior;
import com.mayadem.battlearena.api.repository.BattleParticipantRepository;
import com.mayadem.battlearena.api.repository.projection.OverallStatsProjection;

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
                longestWinStreak
        );
    }
}