package com.mayadem.battlearena.api.service;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mayadem.battlearena.api.dto.BattleHistoryDto;
import com.mayadem.battlearena.api.dto.BattleHistoryPageDto;
import com.mayadem.battlearena.api.dto.BattleHistorySummaryDto;
import com.mayadem.battlearena.api.dto.BattleStatsDto; 
import com.mayadem.battlearena.api.entity.BattleParticipant;
import com.mayadem.battlearena.api.entity.Warrior;
import com.mayadem.battlearena.api.entity.enums.BattleType;
import com.mayadem.battlearena.api.exception.ResourceNotFoundException;
import com.mayadem.battlearena.api.repository.BattleParticipantRepository;


@Service
public class BattleHistoryService {

    private final BattleParticipantRepository battleParticipantRepository;

    public BattleHistoryService(BattleParticipantRepository battleParticipantRepository) {
        this.battleParticipantRepository = battleParticipantRepository;
    }

    @Transactional(readOnly = true)
    public BattleHistoryPageDto getWarriorBattleHistory(Warrior warrior, Optional<BattleType> battleType, Pageable pageable) {

        Page<BattleParticipant> participantPage;
        if (battleType.isPresent()) {
            participantPage = battleParticipantRepository.findBattleHistoryByWarriorAndType(warrior, battleType.get(), pageable);
        } else {
            participantPage = battleParticipantRepository.findBattleHistoryByWarrior(warrior, pageable);
        }

        BattleStatsDto statsDto = battleParticipantRepository.findBattleStatsByWarrior(warrior);

        BattleHistorySummaryDto summaryDto = BattleHistorySummaryDto.from(statsDto);

        return BattleHistoryPageDto.from(participantPage, summaryDto);
    }

    
    @Transactional(readOnly = true)
    public BattleHistoryDto getBattleDetails(Warrior warrior, Long battleRoomId) {
        BattleParticipant participant = battleParticipantRepository
            .findByWarriorAndBattleRoomId(warrior, battleRoomId)

            .orElseThrow(() -> new ResourceNotFoundException("Battle details not found or you do not have permission to view it."));
        return BattleHistoryDto.from(participant);
    }
    @Transactional(readOnly = true)
    public BattleHistorySummaryDto getBattleHistorySummary(Warrior warrior) {
    BattleStatsDto statsDto = battleParticipantRepository.findBattleStatsByWarrior(warrior);
    return BattleHistorySummaryDto.from(statsDto);
}
}
