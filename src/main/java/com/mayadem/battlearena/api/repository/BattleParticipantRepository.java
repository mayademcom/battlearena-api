package com.mayadem.battlearena.api.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mayadem.battlearena.api.dto.BattleStatsDto;
import com.mayadem.battlearena.api.entity.BattleParticipant;
import com.mayadem.battlearena.api.entity.BattleRoom;
import com.mayadem.battlearena.api.entity.Warrior;
import com.mayadem.battlearena.api.entity.enums.BattleStatus;
import com.mayadem.battlearena.api.entity.enums.BattleType;
import com.mayadem.battlearena.api.repository.projection.OpponentInfoProjection;

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

}