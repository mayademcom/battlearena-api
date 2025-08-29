package com.mayadem.battlearena.api.dto;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import com.mayadem.battlearena.api.entity.BattleParticipant;
import com.mayadem.battlearena.api.entity.BattleRoom;
import com.mayadem.battlearena.api.entity.enums.BattleResult;
import com.mayadem.battlearena.api.entity.enums.BattleStatus;
import com.mayadem.battlearena.api.entity.enums.BattleType;

public class BattleHistoryDto {

    private Long battleRoomId;
    private BattleType battleType;
    private BattleStatus status; 
    private Integer myScore;
    private BattleResult myResult;
    private Integer rankPointsChange;
    private Integer rankPointsBefore; 
    private Integer rankPointsAfter; 
    private LocalDateTime completedAt;
    private OpponentDto opponent;
    private Long durationMinutes;

    public static class OpponentDto {
        private String displayName;
        private Integer score;

        public OpponentDto(String displayName, Integer score) {
            this.displayName = displayName;
            this.score = score;
        }

        public String getDisplayName() { return displayName; }
        public Integer getScore() { return score; }
    }

    public static BattleHistoryDto from(BattleParticipant myParticipant) {
        BattleHistoryDto dto = new BattleHistoryDto();
        BattleRoom battleRoom = myParticipant.getBattleRoom();

        dto.battleRoomId = battleRoom.getId();
        dto.battleType = battleRoom.getBattleType();
        dto.status = battleRoom.getStatus(); 
        dto.myScore = myParticipant.getFinalScore();
        dto.myResult = myParticipant.getResult();
        dto.rankPointsChange = myParticipant.getRankPointsChange();
        dto.rankPointsBefore = myParticipant.getRankPointsBefore(); 
        dto.rankPointsAfter = myParticipant.getRankPointsAfter();   

        if (battleRoom.getCompletedAt() != null) {
            dto.completedAt = LocalDateTime.ofInstant(battleRoom.getCompletedAt(), ZoneId.systemDefault());
        }

        Optional<BattleParticipant> opponentOpt = battleRoom.getParticipants().stream()
                .filter(p -> !p.equals(myParticipant))
                .findFirst();

        if (opponentOpt.isPresent()) {
            BattleParticipant opponent = opponentOpt.get();
            dto.opponent = new OpponentDto(opponent.getWarrior().getDisplayName(), opponent.getFinalScore());
        }

        if (battleRoom.getStartedAt() != null && battleRoom.getCompletedAt() != null) {
            Duration duration = Duration.between(battleRoom.getStartedAt(), battleRoom.getCompletedAt());
            dto.durationMinutes = duration.toMinutes();
        }

        return dto;
    }

   
    public Long getBattleRoomId() { return battleRoomId; }
    public BattleType getBattleType() { return battleType; }
    public BattleStatus getStatus() { return status; }
    public Integer getMyScore() { return myScore; }
    public BattleResult getMyResult() { return myResult; }
    public Integer getRankPointsChange() { return rankPointsChange; }
    public Integer getRankPointsBefore() { return rankPointsBefore; }
    public Integer getRankPointsAfter() { return rankPointsAfter; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public OpponentDto getOpponent() { return opponent; }
    public Long getDurationMinutes() { return durationMinutes; }
}