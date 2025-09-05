package com.mayadem.battlearena.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mayadem.battlearena.api.dto.BattleTypeStatsDto;
import com.mayadem.battlearena.api.entity.BattleParticipant;
import com.mayadem.battlearena.api.entity.BattleRoom;
import com.mayadem.battlearena.api.entity.Warrior;
import com.mayadem.battlearena.api.entity.enums.BattleStatus;
import com.mayadem.battlearena.api.entity.enums.BattleType;
import com.mayadem.battlearena.api.repository.projection.DailyStatsProjection;
import com.mayadem.battlearena.api.repository.projection.OpponentInfoProjection;
import com.mayadem.battlearena.api.repository.projection.OverallStatsProjection;
import com.mayadem.battlearena.api.repository.projection.RecentPerformanceProjection;

@Repository
public interface BattleParticipantRepository extends JpaRepository<BattleParticipant, Long> {

    boolean existsByBattleRoomAndWarrior(BattleRoom battleRoom, Warrior warrior);
    List<BattleParticipant> findByWarriorAndBattleRoomStatus(Warrior warrior, BattleStatus status);


    @Query("SELECT bp FROM BattleParticipant bp JOIN bp.battleRoom br " +
           "WHERE bp.warrior = :warrior " +
           "AND br.status = com.mayadem.battlearena.api.entity.enums.BattleStatus.COMPLETED " +
           "ORDER BY br.completedAt DESC")
    Page<BattleParticipant> findBattleHistoryByWarrior(@Param("warrior") Warrior warrior, Pageable pageable);

    @Query("SELECT bp FROM BattleParticipant bp JOIN bp.battleRoom br " +
           "WHERE bp.warrior = :warrior " +
           "AND br.battleType = :battleType " +
           "AND br.status = com.mayadem.battlearena.api.entity.enums.BattleStatus.COMPLETED " +
           "ORDER BY br.completedAt DESC")
    Page<BattleParticipant> findBattleHistoryByWarriorAndType(@Param("warrior") Warrior warrior, @Param("battleType") BattleType battleType, Pageable pageable);

    @Query(value = "SELECT w.display_name AS displayName, bp.final_score AS finalScore " +
                   "FROM battle_participants bp " +
                   "JOIN warriors w ON bp.warrior_id = w.id " +
                   "WHERE bp.battle_room_id = :battleRoomId " +
                   "AND bp.warrior_id != :warriorId",
           nativeQuery = true)
    OpponentInfoProjection findOpponentInfo(@Param("warriorId") Long warriorId, @Param("battleRoomId") Long battleRoomId);

    @Query("SELECT new com.mayadem.battlearena.api.dto.BattleStatsDto(" +
       "COUNT(bp), " +
       "SUM(CASE WHEN bp.result = com.mayadem.battlearena.api.entity.enums.BattleResult.WIN THEN 1 ELSE 0 END), " +
       "SUM(CASE WHEN bp.result = com.mayadem.battlearena.api.entity.enums.BattleResult.LOSS THEN 1 ELSE 0 END), " +
       "SUM(CASE WHEN bp.result = com.mayadem.battlearena.api.entity.enums.BattleResult.DRAW THEN 1 ELSE 0 END)) " +
       "FROM BattleParticipant bp " +
       "WHERE bp.warrior = :warrior " +
       "AND bp.battleRoom.status = com.mayadem.battlearena.api.entity.enums.BattleStatus.COMPLETED")
    BattleStatsDto findBattleStatsByWarrior(@Param("warrior") Warrior warrior);

    List<BattleParticipant> findAllByWarriorAndBattleRoomStatus(Warrior warrior, com.mayadem.battlearena.api.entity.enums.BattleStatus status);

    Optional<BattleParticipant> findByWarriorAndBattleRoomId(Warrior warrior, Long battleRoomId);

    @Query("""
        SELECT new com.mayadem.battlearena.api.repository.projection.OverallStatsProjection(
            COUNT(bp),
            SUM(CASE WHEN bp.result = com.mayadem.battlearena.api.entity.enums.BattleResult.WIN THEN 1 ELSE 0 END),
            SUM(CASE WHEN bp.result = com.mayadem.battlearena.api.entity.enums.BattleResult.LOSS THEN 1 ELSE 0 END),
            SUM(CASE WHEN bp.result = com.mayadem.battlearena.api.entity.enums.BattleResult.DRAW THEN 1 ELSE 0 END),
            CASE WHEN COUNT(bp) > 0 THEN
                (SUM(CASE WHEN bp.result = com.mayadem.battlearena.api.entity.enums.BattleResult.WIN THEN 1.0 ELSE 0.0 END) / COUNT(bp)) * 100.0
            ELSE 0.0 END,
            MAX(bp.finalScore),
            AVG(bp.finalScore),
            SUM(bp.rankPointsChange)
        )
        FROM BattleParticipant bp
        WHERE bp.warrior = :warrior AND bp.battleRoom.status = com.mayadem.battlearena.api.entity.enums.BattleStatus.COMPLETED
    """)
    Optional<OverallStatsProjection> findOverallStatsByWarrior(@Param("warrior") Warrior warrior);

    @Query(value = """
        WITH ranked_battles AS (
            SELECT
                br.completed_at,
                bp.result,
                LAG(bp.result, 1) OVER (ORDER BY br.completed_at) as prev_result
            FROM battle_participants bp
            JOIN battle_rooms br ON bp.battle_room_id = br.id
            WHERE bp.warrior_id = :warriorId AND br.status = 'COMPLETED'
        ),
        streak_groups AS (
            SELECT
                result,
                completed_at,
                SUM(CASE WHEN result <> prev_result THEN 1 ELSE 0 END) OVER (ORDER BY completed_at) as streak_group
            FROM ranked_battles
        ),
        streak_counts AS (
            SELECT
                result,
                streak_group,
                COUNT(*) as streak_length,
                MAX(completed_at) as last_battle_time
            FROM streak_groups
            GROUP BY result, streak_group
        )
        SELECT
            (SELECT streak_length FROM streak_counts ORDER BY last_battle_time DESC LIMIT 1) as current_streak,
            (SELECT result FROM streak_counts ORDER BY last_battle_time DESC LIMIT 1) as current_streak_type,
            COALESCE((SELECT MAX(streak_length) FROM streak_counts WHERE result = 'WIN'), 0) as longest_win_streak
    """, nativeQuery = true)
    List<Object[]> findStreakInfoByWarrior(@Param("warriorId") Long warriorId);

    @Query("""
        SELECT new com.mayadem.battlearena.api.dto.BattleTypeStatsDto(
            br.battleType,
            CAST(COUNT(bp) AS int),
            CAST(SUM(CASE WHEN bp.result = com.mayadem.battlearena.api.entity.enums.BattleResult.WIN THEN 1 ELSE 0 END) AS int),
            CAST(SUM(CASE WHEN bp.result = com.mayadem.battlearena.api.entity.enums.BattleResult.LOSS THEN 1 ELSE 0 END) AS int),
            CAST(SUM(CASE WHEN bp.result = com.mayadem.battlearena.api.entity.enums.BattleResult.DRAW THEN 1 ELSE 0 END) AS int),
            (SUM(CASE WHEN bp.result = com.mayadem.battlearena.api.entity.enums.BattleResult.WIN THEN 1.0 ELSE 0.0 END) / COUNT(bp)) * 100.0,
            MAX(bp.finalScore),
            AVG(bp.finalScore),
            CAST(SUM(bp.rankPointsChange) AS int)
        )
        FROM BattleParticipant bp
        JOIN bp.battleRoom br
        WHERE bp.warrior = :warrior AND br.status = com.mayadem.battlearena.api.entity.enums.BattleStatus.COMPLETED
        GROUP BY br.battleType
    """)
    List<BattleTypeStatsDto> findStatsByBattleType(@Param("warrior") Warrior warrior);

    @Query("""
        SELECT new com.mayadem.battlearena.api.repository.projection.RecentPerformanceProjection(
            COUNT(bp),
            SUM(CASE WHEN bp.result = com.mayadem.battlearena.api.entity.enums.BattleResult.WIN THEN 1 ELSE 0 END),
            SUM(bp.rankPointsChange)
        )
        FROM BattleParticipant bp
        JOIN bp.battleRoom br
        WHERE bp.warrior = :warrior
        AND br.status = com.mayadem.battlearena.api.entity.enums.BattleStatus.COMPLETED
        AND br.completedAt >= :since
    """)
    Optional<RecentPerformanceProjection> findRecentStats(@Param("warrior") Warrior warrior, @Param("since") java.time.Instant since);

    @Query(value = """
        SELECT
            CAST(br.completed_at AS DATE) as battleDate,
            COUNT(*) as battleCount
        FROM battle_participants bp
        JOIN battle_rooms br ON bp.battle_room_id = br.id
        WHERE
            bp.warrior_id = :warriorId AND
            br.status = 'COMPLETED' AND
            br.completed_at >= :since
        GROUP BY battleDate
        ORDER BY battleDate ASC
    """, nativeQuery = true)
    List<DailyStatsProjection> findDailyBattleCounts(
        @Param("warriorId") Long warriorId,
        @Param("since") java.time.Instant since
    );

}