package com.mayadem.battlearena.api.service;

import java.security.SecureRandom;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.mayadem.battlearena.api.entity.BattleRoom;
import com.mayadem.battlearena.api.entity.enums.BattleStatus;
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
        String roomCode = generateUniqueRoomCode();
        BattleRoom newBattleRoom = new BattleRoom();
        newBattleRoom.setRoomCode(roomCode);
        newBattleRoom.setBattleType(requestDto.getBattleType());
        newBattleRoom.setStatus(BattleStatus.WAITING);
        newBattleRoom.setCreatedBy(creator);
        newBattleRoom.setCurrentParticipants(1);
        return null;
    }

    private String generateUniqueRoomCode() {

        final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        final Random random = new SecureRandom();

        final StringBuilder codeBuilder = new StringBuilder(6);

        String roomCode;
        do {

            codeBuilder.setLength(0);

            for (int i = 0; i < 6; i++) {

                int randomIndex = random.nextInt(ALPHABET.length());

                codeBuilder.append(ALPHABET.charAt(randomIndex));
            }
            roomCode = codeBuilder.toString();

        } while (battleRoomRepository.findByRoomCode(roomCode).isPresent());

        return roomCode;
    }
}