package com.mayadem.battlearena.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import com.mayadem.battlearena.api.entity.BattleRoom;

@Repository
public interface BattleRoomRepository extends JpaRepository<BattleRoom, Long> {

    Optional<BattleRoom> findByRoomCode(String roomCode);
}