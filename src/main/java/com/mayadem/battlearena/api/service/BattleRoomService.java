package com.mayadem.battlearena.api.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mayadem.battlearena.api.dto.BattleRoomDto;
import com.mayadem.battlearena.api.dto.StartBattleRequestDto;
import com.mayadem.battlearena.api.entity.Warrior;
import com.mayadem.battlearena.api.repository.BattleRoomRepository;

@Service
public class BattleRoomService {

    private final BattleRoomRepository battleRoomRepository;

    public BattleRoomService(BattleRoomRepository battleRoomRepository) {
        this.battleRoomRepository = battleRoomRepository;
    }

    @Transactional
    public BattleRoomDto createBattleRoom(StartBattleRequestDto requestDto, Warrior creator) {

        return null;
    }
}