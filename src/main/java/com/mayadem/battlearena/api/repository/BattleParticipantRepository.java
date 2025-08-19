package com.mayadem.battlearena.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mayadem.battlearena.api.entity.BattleParticipant;
import com.mayadem.battlearena.api.entity.BattleRoom;
import com.mayadem.battlearena.api.entity.Warrior;
import com.mayadem.battlearena.api.entity.enums.BattleStatus;

@Repository
public interface BattleParticipantRepository extends JpaRepository<BattleParticipant, Long> {
    boolean existsByBattleRoomAndWarrior(BattleRoom battleRoom, Warrior warrior);
    List<BattleParticipant> findByWarriorAndBattleRoomStatus(Warrior warrior, BattleStatus status);
}