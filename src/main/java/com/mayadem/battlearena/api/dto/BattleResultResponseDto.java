package com.mayadem.battlearena.api.dto;

import java.time.LocalDateTime;

import com.mayadem.battlearena.api.entity.BattleParticipant;
import com.mayadem.battlearena.api.entity.BattleRoom;
import com.mayadem.battlearena.api.entity.enums.BattleResult;
import com.mayadem.battlearena.api.entity.enums.BattleType;

public class BattleResultResponseDto {

    private Long battleRoomId;
    private BattleType battleType;
    private int score;
    private BattleResult result;
    private int rankPointsChange;
    private int newRankPoints;
    private LocalDateTime completedAt;
    private BattleOpponentDto opponent;

    public static BattleResultResponseDto from(BattleRoom battleRoom, BattleParticipant me, BattleOpponentDto opponentDto, int myRankPointsChange) {
        BattleResultResponseDto dto = new BattleResultResponseDto();
        dto.setBattleRoomId(battleRoom.getId());
        dto.setBattleType(battleRoom.getBattleType());
        dto.setScore(me.getFinalScore());
        dto.setResult(me.getResult());
        dto.setRankPointsChange(myRankPointsChange);
        dto.setNewRankPoints(me.getWarrior().getRankPoints());
        dto.setCompletedAt(LocalDateTime.now());
        dto.setOpponent(opponentDto);
        return dto;
    }

    public Long getBattleRoomId() {
        return battleRoomId;
    }

    public void setBattleRoomId(Long battleRoomId) {
        this.battleRoomId = battleRoomId;
    }

    public BattleType getBattleType() {
        return battleType;
    }

    public void setBattleType(BattleType battleType) {
        this.battleType = battleType;
    }

    public int getScore() {
        return score;
    } 

    public void setScore(int score) {
        this.score = score;
    }

    public BattleResult getResult() {
        return result;
    }

    public void setResult(BattleResult result) {
        this.result = result;
    }

    public int getRankPointsChange() {
        return rankPointsChange;
    }

    public void setRankPointsChange(int rankPointsChange) {
        this.rankPointsChange = rankPointsChange;
    }

    public int getNewRankPoints() {
        return newRankPoints;
    }

    public void setNewRankPoints(int newRankPoints) {
        this.newRankPoints = newRankPoints;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public BattleOpponentDto getOpponent() {
        return opponent;
    }

    public void setOpponent(BattleOpponentDto opponent) {
        this.opponent = opponent;
    }

}