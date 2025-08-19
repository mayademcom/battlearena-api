package com.mayadem.battlearena.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mayadem.battlearena.api.entity.BattleParticipant;
import com.mayadem.battlearena.api.entity.BattleRoom;
import com.mayadem.battlearena.api.entity.Warrior;

@Repository
public interface BattleParticipantRepository extends JpaRepository<BattleParticipant, Long> {
    boolean existsByBattleRoomAndWarrior(BattleRoom battleRoom, Warrior warrior);
}