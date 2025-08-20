package com.mayadem.battlearena.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class SubmitBattleResultRequestDto {
    
    @NotNull(message = "Battle room ID cannot be null")
    private Long battleRoomId;

    @Min(value = 1, message = "Score must be a positive integer")
    private int score;

    private String notes;

    public Long getBattleRoomId() {
        return battleRoomId;
    }

    public void setBattleRoomId(Long battleRoomId) {
        this.battleRoomId = battleRoomId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
