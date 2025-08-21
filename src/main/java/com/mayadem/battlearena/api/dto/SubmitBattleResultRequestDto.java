package com.mayadem.battlearena.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class SubmitBattleResultRequestDto {
    
    @NotNull(message = "Battle room ID cannot be null.")
    private Long battleRoomId;

    @Min(value = 0, message = "Score cannot be negative.")
    private int myScore;

    @Min(value = 0, message = "Opponent score cannot be negative.")
    private int opponentScore;

    private String notes;


    public Long getBattleRoomId() {
        return battleRoomId;
    }

    public void setBattleRoomId(Long battleRoomId) {
        this.battleRoomId = battleRoomId;
    }

    public int getMyScore() {
        return myScore;
    }

    public void setMyScore(int myScore) {
        this.myScore = myScore;
    }

    public int getOpponentScore() {
        return opponentScore;
    }

    public void setOpponentScore(int opponentScore) {
        this.opponentScore = opponentScore;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}