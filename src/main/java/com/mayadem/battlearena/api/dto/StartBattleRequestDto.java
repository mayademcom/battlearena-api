package com.mayadem.battlearena.api.dto;

import com.mayadem.battlearena.api.entity.enums.BattleType;

import jakarta.validation.constraints.NotNull;

public class StartBattleRequestDto {

    @NotNull(message = "Battle type must be specified.")
    private BattleType battleType;

    public StartBattleRequestDto() {
    }

    public StartBattleRequestDto(BattleType battleType) {
        this.battleType = battleType;
    }

    public BattleType getBattleType() {
        return battleType;
    }

    public void setBattleType(BattleType battleType) {
        this.battleType = battleType;
    }
}
