package com.mayadem.battlearena.api.dto;

import java.time.LocalDateTime;
import com.mayadem.battlearena.api.entity.enums.BattleResult;
import com.mayadem.battlearena.api.entity.enums.BattleType;

public class BattleResultResponseDto {

    private Long battleRoomId;
    private BattleType battleType;
    private int finalScore;
    private BattleResult result;
    private int rankPointsChange;
    private int newRankPoints;
    private LocalDateTime completedAt;
    private BattleOpponentDto opponent;

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

    public int getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(int finalScore) {
        this.finalScore = finalScore;
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
