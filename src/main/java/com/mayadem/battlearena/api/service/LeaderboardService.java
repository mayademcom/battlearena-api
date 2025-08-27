package com.mayadem.battlearena.api.service;

import com.mayadem.battlearena.api.dto.ArenaLeaderboardDto;
import com.mayadem.battlearena.api.dto.LeaderboardEntryDto;
import com.mayadem.battlearena.api.dto.LeaderboardStatsDto;
import com.mayadem.battlearena.api.entity.ArenaLeaderboard;
import com.mayadem.battlearena.api.repository.ArenaLeaderboardRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LeaderboardService {

    private final ArenaLeaderboardRepository leaderboardRepository;

    public LeaderboardService(ArenaLeaderboardRepository leaderboardRepository) {
        this.leaderboardRepository = leaderboardRepository;
    }

    /**
     * Top 100 oyuncuyu DTO olarak getir ve mevcut savaşçıyı vurgula
     */
    public List<LeaderboardEntryDto> getTop100(Long currentWarriorId) {
        List<ArenaLeaderboard> top100 = leaderboardRepository.findTop100();
        return top100.stream()
                .map(entity -> toDto(entity, currentWarriorId))
                .collect(Collectors.toList());
    }

    /**
     * Global istatistikleri getir
     */
    public LeaderboardStatsDto getGlobalStats() {
        return leaderboardRepository.findGlobalStats();
    }

    /**
     * Mevcut savaşçının etrafındaki oyuncuları DTO olarak getir
     */
    public List<LeaderboardEntryDto> getWarriorsAroundMe(Long warriorId, int range) {
        Optional<ArenaLeaderboard> warriorOpt = leaderboardRepository.findWarriorPosition(warriorId);
        if (warriorOpt.isEmpty()) {
            return List.of();
        }

        int currentRank = warriorOpt.get().getRankPosition();
        int startRank = Math.max(1, currentRank - range);
        int endRank = currentRank + range;

        List<ArenaLeaderboard> around = leaderboardRepository.findWarriorsAroundRank(startRank, endRank);
        return around.stream()
                .map(entity -> toDto(entity, warriorId))
                .collect(Collectors.toList());
    }

    /**
     * Komple leaderboard DTO'sunu hazırla (top 100 + mevcut savaşçı pozisyonu + global stats + son güncelleme)
     */
    public ArenaLeaderboardDto getArenaLeaderboard(Long currentWarriorId) {
        ArenaLeaderboardDto dto = new ArenaLeaderboardDto();
        dto.setTopWarriors(getTop100(currentWarriorId));

        Optional<ArenaLeaderboard> currentWarrior = leaderboardRepository.findWarriorPosition(currentWarriorId);
        currentWarrior.ifPresent(warrior -> dto.setCurrentWarriorPosition(warrior.getRankPosition()));

        dto.setGlobalStats(getGlobalStats());
        dto.setLastUpdated(LocalDateTime.now());

        return dto;
    }

    /**
     * Entity -> DTO dönüşümü
     */
    private LeaderboardEntryDto toDto(ArenaLeaderboard entity, Long currentWarriorId) {
        LeaderboardEntryDto dto = new LeaderboardEntryDto();
        dto.setRank(entity.getRankPosition());
        dto.setWarriorId(entity.getId());
        dto.setUsername(entity.getUsername());
        dto.setDisplayName(entity.getDisplayName());
        dto.setRankPoints(entity.getRankPoints());
        dto.setTotalBattles(entity.getTotalBattles());
        dto.setVictories(entity.getVictories());
        dto.setDefeats(entity.getDefeats());
        dto.setWinRate(entity.getWinRate());
        dto.setCurrentWarrior(entity.getId().equals(currentWarriorId));
        return dto;
    }
}
