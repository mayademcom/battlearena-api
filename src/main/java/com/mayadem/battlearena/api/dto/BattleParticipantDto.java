package com.mayadem.battlearena.api.dto;

import com.mayadem.battlearena.api.entity.BattleParticipant;
import com.mayadem.battlearena.api.entity.enums.BattleResult;

public class BattleParticipantDto {

    private Long warriorId;
    private String displayName;
    private Integer finalScore;
    private BattleResult result;
    private Integer position;

    public static BattleParticipantDto fromEntity(BattleParticipant participant) {
        BattleParticipantDto dto = new BattleParticipantDto();
        dto.setWarriorId(participant.getWarrior().getId());
        dto.setDisplayName(participant.getWarrior().getDisplayName());
        dto.setFinalScore(participant.getFinalScore());
        dto.setResult(participant.getResult());
        dto.setPosition(participant.getPosition());
        return dto;
    }

    public Long getWarriorId() {
        return warriorId;
    }

    public void setWarriorId(Long warriorId) {
        this.warriorId = warriorId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(Integer finalScore) {
        this.finalScore = finalScore;
    }

    public BattleResult getResult() {
        return result;
    }

    public void setResult(BattleResult result) {
        this.result = result;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }
}