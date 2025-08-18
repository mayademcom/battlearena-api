package com.mayadem.battlearena.api.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mayadem.battlearena.api.dto.BattleOpponentDto;
import com.mayadem.battlearena.api.dto.BattleResultResponseDto;
import com.mayadem.battlearena.api.dto.SubmitBattleResultRequestDto;
import com.mayadem.battlearena.api.entity.BattleRoom;
import com.mayadem.battlearena.api.entity.Warrior;
import com.mayadem.battlearena.api.entity.enums.BattleResult;
import com.mayadem.battlearena.api.entity.enums.BattleType;
import com.mayadem.battlearena.api.repository.BattleRoomRepository;
import com.mayadem.battlearena.api.repository.WarriorRepository;

@Service
public class BattleCompletionService {

    private final ScoringService scoringService;
    private final WarriorRepository warriorRepository;
    private final BattleRoomRepository battleRoomRepository;

    public BattleCompletionService(ScoringService scoringService,
                                   WarriorRepository warriorRepository,
                                   BattleRoomRepository battleRoomRepository) {
        this.scoringService = scoringService;
        this.warriorRepository = warriorRepository;
        this.battleRoomRepository = battleRoomRepository;
    }

    @Transactional
    public BattleResultResponseDto completeBattle(SubmitBattleResultRequestDto request, String username) {

        BattleRoom battleRoom = battleRoomRepository.findById(request.getBattleRoomId())
                .orElseThrow(() -> new IllegalArgumentException("BattleRoom not found"));

        Warrior me = warriorRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Warrior not found"));

        // Bu iki metot BattleRoom içinde senin yazdıkların
        Warrior opponent = battleRoom.getOpponentOf(me);
        int myScore = request.getScore();
        int opponentScore = battleRoom.getOpponentScoreOf(me);

        BattleResult myResult = scoringService.determineBattleResult(myScore, opponentScore);
        BattleResult opponentResult = scoringService.determineBattleResult(opponentScore, myScore);

        int rankPointsChange = 0;
        if (battleRoom.getBattleType() == BattleType.RANKED) {
            if (myResult == BattleResult.WIN) {
                rankPointsChange = scoringService.calculateRankPointsChange(myScore, opponentScore, battleRoom.getBattleType());
                me.setRankPoints(me.getRankPoints() + rankPointsChange);
                opponent.setRankPoints(Math.max(0, opponent.getRankPoints() - rankPointsChange));
            } else if (myResult == BattleResult.LOSS) {
                rankPointsChange = scoringService.calculateRankPointsChange(opponentScore, myScore, battleRoom.getBattleType());
                me.setRankPoints(Math.max(0, me.getRankPoints() - rankPointsChange));
                opponent.setRankPoints(opponent.getRankPoints() + rankPointsChange);
            } // DRAW ise 0 değişir
        }

        // İstatistik güncelle (Warrior#updateStats metodunun sende tanımlı olduğunu varsayıyoruz)
        me.updateStats(myResult, myScore);
        opponent.updateStats(opponentResult, opponentScore);

        warriorRepository.save(me);
        warriorRepository.save(opponent);

        // Rakibin önceki puanını doğru hesapla:
        int opponentRankPointsAfter = opponent.getRankPoints();
        int opponentRankPointsBefore =
                (myResult == BattleResult.WIN)  ? opponentRankPointsAfter + rankPointsChange :
                (myResult == BattleResult.LOSS) ? opponentRankPointsAfter - rankPointsChange :
                                                  opponentRankPointsAfter;

        BattleOpponentDto opponentDto = new BattleOpponentDto();
        opponentDto.setUsername(opponent.getUsername());
        opponentDto.setDisplayName(opponent.getDisplayName());
        opponentDto.setScore(opponentScore);
        opponentDto.setResult(opponentResult); // <-- enum veriyoruz
        opponentDto.setRankPointsBefore(opponentRankPointsBefore);
        opponentDto.setRankPointsAfter(opponentRankPointsAfter);

        BattleResultResponseDto response = new BattleResultResponseDto();
        response.setBattleRoomId(request.getBattleRoomId());
        response.setBattleType(battleRoom.getBattleType()); // <-- enum veriyoruz
        response.setFinalScore(myScore + " - " + opponentScore);
        response.setResult(myResult); // <-- enum veriyoruz
        response.setRankPointsChange(rankPointsChange);
        response.setNewRankPoints(me.getRankPoints());
        response.setCompletedAt(LocalDateTime.now());
        response.setOpponent(opponentDto);

        return response;
    }
}
