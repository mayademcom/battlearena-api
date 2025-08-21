package com.mayadem.battlearena.api.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

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
import com.mayadem.battlearena.api.entity.enums.BattleType;
import com.mayadem.battlearena.api.repository.BattleRoomRepository;

@Service
public class BattleCompletionService {

    private final ScoringService scoringService;
    private final BattleRoomRepository battleRoomRepository;
 

    public BattleCompletionService(ScoringService scoringService,
                                   BattleRoomRepository battleRoomRepository) {
        this.scoringService = scoringService;
        this.battleRoomRepository = battleRoomRepository;
    }

    @Transactional
    public BattleResultResponseDto completeBattle(SubmitBattleResultRequestDto request, Warrior me) {

        BattleRoom battleRoom = battleRoomRepository.findById(request.getBattleRoomId())
                .orElseThrow(() -> new IllegalArgumentException("BattleRoom not found with ID: " + request.getBattleRoomId()));

        BattleParticipant meAsParticipant = findParticipant(battleRoom, me);
        BattleParticipant opponentAsParticipant = findOpponent(battleRoom, me);
        Warrior opponent = opponentAsParticipant.getWarrior();

       
        int myScore = request.getMyScore();
        int opponentScore = request.getOpponentScore();

        BattleResult myResult = scoringService.determineBattleResult(myScore, opponentScore);
        BattleResult opponentResult = scoringService.determineBattleResult(opponentScore, myScore);

       
        meAsParticipant.setFinalScore(myScore);
        meAsParticipant.setResult(myResult);
        opponentAsParticipant.setFinalScore(opponentScore);
        opponentAsParticipant.setResult(opponentResult);
        
        int myRankPointsBefore = me.getRankPoints();
        int opponentRankPointsBefore = opponent.getRankPoints();

        meAsParticipant.setRankPointsBefore(myRankPointsBefore);
        opponentAsParticipant.setRankPointsBefore(opponentRankPointsBefore);
        
        
        int rankPointsChange = 0;
        if (battleRoom.getBattleType() == BattleType.RANKED) {
            Warrior winner = (myResult == BattleResult.WIN) ? me : opponent;
            Warrior loser = (myResult == BattleResult.WIN) ? opponent : me;
            int winnerScore = (myResult == BattleResult.WIN) ? myScore : opponentScore;
            int loserScore = (myResult == BattleResult.WIN) ? opponentScore : myScore;
            
            rankPointsChange = scoringService.calculateRankPointsChange(winnerScore, loserScore, battleRoom.getBattleType());

            winner.setRankPoints(winner.getRankPoints() + rankPointsChange);
            loser.setRankPoints(Math.max(0, loser.getRankPoints() - rankPointsChange));
        }
        
        me.updateStats(myResult, myScore);
        opponent.updateStats(opponentResult, opponentScore);

        meAsParticipant.setRankPointsAfter(me.getRankPoints());
        opponentAsParticipant.setRankPointsAfter(opponent.getRankPoints());
        meAsParticipant.setRankPointsChange((myResult == BattleResult.WIN) ? rankPointsChange : -rankPointsChange);
        opponentAsParticipant.setRankPointsChange((opponentResult == BattleResult.WIN) ? rankPointsChange : -rankPointsChange);

        battleRoom.setStatus(BattleStatus.COMPLETED);
        battleRoom.setCompletedAt(Instant.now());

        BattleRoom savedRoom = battleRoomRepository.save(battleRoom);

        
        return buildResponseDto(savedRoom, meAsParticipant, opponentAsParticipant);
    }

    private BattleParticipant findParticipant(BattleRoom battleRoom, Warrior warrior) {
        return battleRoom.getParticipants().stream()
                .filter(p -> p.getWarrior().getId().equals(warrior.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Warrior with ID " + warrior.getId() + " is not a participant in this battle."));
    }

    private BattleParticipant findOpponent(BattleRoom battleRoom, Warrior me) {
        return battleRoom.getParticipants().stream()
                .filter(p -> !p.getWarrior().getId().equals(me.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Opponent not found in this battle."));
    }
    
    

private BattleResultResponseDto buildResponseDto(BattleRoom room, BattleParticipant me, BattleParticipant opponent) {
    
    BattleOpponentDto opponentDto = new BattleOpponentDto();
    opponentDto.setUsername(opponent.getWarrior().getUsername());
    opponentDto.setDisplayName(opponent.getWarrior().getDisplayName());
    opponentDto.setScore(opponent.getFinalScore());
    opponentDto.setResult(opponent.getResult());
    opponentDto.setRankPointsBefore(opponent.getRankPointsBefore());
    opponentDto.setRankPointsAfter(opponent.getRankPointsAfter());

    
    BattleResultResponseDto response = new BattleResultResponseDto();
    response.setBattleRoomId(room.getId());
    response.setBattleType(room.getBattleType());
    response.setResult(me.getResult());
    response.setRankPointsChange(me.getRankPointsChange());
    response.setNewRankPoints(me.getRankPointsAfter());
    response.setCompletedAt(LocalDateTime.ofInstant(room.getCompletedAt(), ZoneId.systemDefault()));
    response.setOpponent(opponentDto);
     
    response.setMyScore(me.getFinalScore());
    response.setOpponentScore(opponent.getFinalScore());

    return response;
}
}