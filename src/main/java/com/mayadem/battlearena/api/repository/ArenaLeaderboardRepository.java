package com.mayadem.battlearena.api.repository;

import com.mayadem.battlearena.api.entity.ArenaLeaderboard;
import com.mayadem.battlearena.api.dto.LeaderboardStatsDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ArenaLeaderboardRepository extends JpaRepository<ArenaLeaderboard, Long> {

    // En iyi 100 oyuncu (JPQL'de LIMIT yok → Pageable kullanılmalı)
    @Query("SELECT a FROM ArenaLeaderboard a ORDER BY a.rankPoints DESC")
    List<ArenaLeaderboard> findTop100(Pageable pageable);

    // Belirli oyuncunun pozisyonu
    @Query("SELECT a FROM ArenaLeaderboard a WHERE a.id = :warriorId")
    Optional<ArenaLeaderboard> findWarriorPosition(@Param("warriorId") Long warriorId);

    // Global istatistikler
    @Query("SELECT new com.mayadem.battlearena.api.dto.LeaderboardStatsDto( " +
            "COUNT(a), AVG(a.rankPoints), MAX(a.rankPoints), MAX(a.username)) " +
            "FROM ArenaLeaderboard a")
    LeaderboardStatsDto findGlobalStats();

    // Rank aralığındaki oyuncular
    @Query("SELECT a FROM ArenaLeaderboard a " +
            "WHERE a.rankPosition BETWEEN :startRank AND :endRank " +
            "ORDER BY a.rankPosition ASC")
    List<ArenaLeaderboard> findWarriorsAroundRank(@Param("startRank") int startRank,
                                                  @Param("endRank") int endRank);
}
