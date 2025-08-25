package com.mayadem.battlearena.api.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mayadem.battlearena.api.dto.BattleOpponentDto;
import com.mayadem.battlearena.api.dto.BattleResultResponseDto;
import com.mayadem.battlearena.api.dto.SubmitBattleResultRequestDto;
import com.mayadem.battlearena.api.entity.BattleParticipant;
import com.mayadem.battlearena.api.entity.BattleRoom;
import com.mayadem.battlearena.api.entity.Warrior;
import com.mayadem.battlearena.api.entity.enums.BattleResult;
import com.mayadem.battlearena.api.entity.enums.BattleStatus;
import com.mayadem.battlearena.api.exception.ResourceNotFoundException;
import com.mayadem.battlearena.api.repository.BattleParticipantRepository;
import com.mayadem.battlearena.api.repository.BattleRoomRepository;
import com.mayadem.battlearena.api.repository.WarriorRepository;

@Service
public class BattleCompletionService {

    private final ScoringService scoringService;
    private final WarriorRepository warriorRepository;
    private final BattleRoomRepository battleRoomRepository;
    private final BattleParticipantRepository battleParticipantRepository;

    public BattleCompletionService(ScoringService scoringService,
            WarriorRepository warriorRepository,
            BattleRoomRepository battleRoomRepository,
            BattleParticipantRepository battleParticipantRepository) {
        this.scoringService = scoringService;
        this.warriorRepository = warriorRepository;
        this.battleRoomRepository = battleRoomRepository;
        this.battleParticipantRepository = battleParticipantRepository;
    }

    @Transactional
    public Object submitAndProcessScore(SubmitBattleResultRequestDto request, Warrior requester) {
        BattleRoom battleRoom = battleRoomRepository.findById(request.getBattleRoomId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "BattleRoom not found with ID: " + request.getBattleRoomId()));

        BattleParticipant currentParticipant = battleRoom.getParticipants().stream()
                .filter(p -> p.getWarrior().equals(requester))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Player is not a participant in this battle."));

        if (currentParticipant.getFinalScore() != null) {
            throw new IllegalStateException("You have already submitted your score for this battle.");
        }

        currentParticipant.setFinalScore(request.getScore());
        battleParticipantRepository.save(currentParticipant);

        BattleRoom updatedBattleRoom = battleRoomRepository.findById(request.getBattleRoomId())
                .orElseThrow(() -> new IllegalStateException("Impossible state: BattleRoom with ID "
                        + request.getBattleRoomId() + " disappeared mid-transaction."));

        boolean allScoresSubmitted = updatedBattleRoom.getParticipants().stream()
                .allMatch(p -> p.getFinalScore() != null);

        if (allScoresSubmitted) {

            return finalizeBattle(updatedBattleRoom, currentParticipant);
        } else {
            return Map.of("status", "pending", "message",
                    "Score submitted successfully. Waiting for other participant(s).");
        }
    }

    private BattleResultResponseDto finalizeBattle(BattleRoom battleRoom, BattleParticipant me) {
        List<BattleParticipant> participants = battleRoom.getParticipants();

        if (participants.size() != battleRoom.getMaxParticipants()) {
            throw new IllegalStateException(
                    String.format("Battle finalization requires %d participants, but %d were found.",
                            battleRoom.getMaxParticipants(), participants.size()));
        }

        BattleParticipant opponent = participants.stream()
                .filter(p -> !p.equals(me))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "Battle finalization error: Opponent warrior could not be found in participants list."));

        int myScore = me.getFinalScore();
        int opponentScore = opponent.getFinalScore();

        BattleResult myResult = scoringService.determineBattleResult(myScore, opponentScore);
        BattleResult opponentResult = scoringService.determineBattleResult(opponentScore, myScore);

        me.setResult(myResult);
        opponent.setResult(opponentResult);

        int myRankPointsChange = 0;
        int opponentRankPointsChange = 0;

        int rankChange = scoringService.calculateRankPointsChange(myScore, opponentScore, battleRoom.getBattleType());

        if (myResult == BattleResult.WIN) {
            myRankPointsChange = rankChange;
            opponentRankPointsChange = -rankChange;
        } else if (myResult == BattleResult.LOSS) {
            myRankPointsChange = -rankChange;
            opponentRankPointsChange = rankChange;
        }

        Warrior myWarrior = me.getWarrior();
        me.setRankPointsBefore(myWarrior.getRankPoints());
        myWarrior.setRankPoints(Math.max(0, myWarrior.getRankPoints() + myRankPointsChange));
        me.setRankPointsAfter(myWarrior.getRankPoints());
        me.setRankPointsChange(myRankPointsChange);
        myWarrior.updateStats(myResult, myScore);

        Warrior opponentWarrior = opponent.getWarrior();
        opponent.setRankPointsBefore(opponentWarrior.getRankPoints());
        opponentWarrior.setRankPoints(Math.max(0, opponentWarrior.getRankPoints() + opponentRankPointsChange));
        opponent.setRankPointsAfter(opponentWarrior.getRankPoints());
        opponent.setRankPointsChange(opponentRankPointsChange);
        opponentWarrior.updateStats(opponentResult, opponentScore);

        warriorRepository.save(myWarrior);
        warriorRepository.save(opponentWarrior);

        battleRoom.setStatus(BattleStatus.COMPLETED);
        battleRoom.setCompletedAt(Instant.now());
        battleRoomRepository.save(battleRoom);

        BattleOpponentDto opponentDto = new BattleOpponentDto();
        opponentDto.setUsername(opponentWarrior.getUsername());
        opponentDto.setDisplayName(opponentWarrior.getDisplayName());
        opponentDto.setScore(opponentScore);
        opponentDto.setResult(opponentResult);
        opponentDto.setRankPointsBefore(opponent.getRankPointsBefore());
        opponentDto.setRankPointsAfter(opponent.getRankPointsAfter());

        BattleResultResponseDto responseDto = new BattleResultResponseDto();
        responseDto.setBattleRoomId(battleRoom.getId());
        responseDto.setBattleType(battleRoom.getBattleType());
        responseDto.setScore(myScore);
        responseDto.setResult(myResult);
        responseDto.setRankPointsChange(myRankPointsChange);
        responseDto.setNewRankPoints(myWarrior.getRankPoints());
        responseDto.setCompletedAt(LocalDateTime.now());
        responseDto.setOpponent(opponentDto);

        return responseDto;
    }
}