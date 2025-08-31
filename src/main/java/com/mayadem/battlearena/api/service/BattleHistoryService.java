package com.mayadem.battlearena.api.service;

import com.mayadem.battlearena.api.dto.BattleHistoryPageDto;
import com.mayadem.battlearena.api.dto.BattleHistorySummaryDto;
import com.mayadem.battlearena.api.dto.BattleStatsDto; 
import com.mayadem.battlearena.api.entity.BattleParticipant;
import com.mayadem.battlearena.api.entity.Warrior;
import com.mayadem.battlearena.api.entity.enums.BattleType;
import com.mayadem.battlearena.api.repository.BattleParticipantRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

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

}