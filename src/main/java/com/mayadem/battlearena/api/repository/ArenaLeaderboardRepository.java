package com.mayadem.battlearena.api.repository;

import com.mayadem.battlearena.api.entity.ArenaLeaderboard;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ArenaLeaderboardRepository extends JpaRepository<ArenaLeaderboard, Long> {

        interface LeaderboardStatsProjection {
                Long getTotalActiveWarriors();

                Double getAverageRankPoints();

                Integer getHighestRankPoints();

                String getTopWarriorUsername();
        }

        // En iyi 100 oyuncu (JPQL'de LIMIT yok → Pageable kullanılmalı)
        @Query("SELECT a FROM ArenaLeaderboard a ORDER BY a.rankPoints DESC")
        List<ArenaLeaderboard> findTop100(Pageable pageable);

        // Belirli oyuncunun pozisyonu
        @Query("SELECT a FROM ArenaLeaderboard a WHERE a.id = :warriorId")
        Optional<ArenaLeaderboard> findWarriorPosition(@Param("warriorId") Long warriorId);

        // Global istatistikler
        @Query(value = """
                            SELECT COUNT(*) AS total_active_warriors,
                                   AVG(rank_points) AS average_rank_points,
                                   MAX(rank_points) AS highest_rank_points,
                                   (SELECT username FROM arena_leaderboard ORDER BY rank_points DESC LIMIT 1) AS top_warrior_username
                            FROM arena_leaderboard
                        """, nativeQuery = true)
        LeaderboardStatsProjection findGlobalStatsProjection();

        // Rank aralığındaki oyuncular
        @Query("SELECT a FROM ArenaLeaderboard a " +
                        "WHERE a.rankPosition BETWEEN :startRank AND :endRank " +
                        "ORDER BY a.rankPosition ASC")
        List<ArenaLeaderboard> findWarriorsAroundRank(@Param("startRank") int startRank,
                        @Param("endRank") int endRank);
}
