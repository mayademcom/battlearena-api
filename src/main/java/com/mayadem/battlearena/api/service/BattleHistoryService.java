package com.mayadem.battlearena.api.service;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mayadem.battlearena.api.dto.BattleHistoryPageDto;
import com.mayadem.battlearena.api.entity.Warrior;
import com.mayadem.battlearena.api.entity.enums.BattleType;
import com.mayadem.battlearena.api.repository.BattleParticipantRepository;

@Service
public class BattleHistoryService {

    private final BattleParticipantRepository battleParticipantRepository;

    public BattleHistoryService(BattleParticipantRepository battleParticipantRepository) {
        this.battleParticipantRepository = battleParticipantRepository;
    }

    public BattleHistoryPageDto getWarriorBattleHistory(Warrior warrior, Optional<BattleType> battleType,
            Pageable pageable) {
        return null;
    }
}