package com.mayadem.battlearena.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mayadem.battlearena.api.entity.BattleParticipant;

@Repository
public interface BattleParticipantRepository extends JpaRepository<BattleParticipant, Long> {

}