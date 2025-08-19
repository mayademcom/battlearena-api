package com.mayadem.battlearena.api.service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mayadem.battlearena.api.dto.BattleRoomDto;
import com.mayadem.battlearena.api.dto.StartBattleRequestDto;
import com.mayadem.battlearena.api.entity.BattleParticipant;
import com.mayadem.battlearena.api.entity.BattleRoom;
import com.mayadem.battlearena.api.entity.Warrior;
import com.mayadem.battlearena.api.entity.enums.BattleResult;
import com.mayadem.battlearena.api.entity.enums.BattleStatus;
import com.mayadem.battlearena.api.exception.BattleRoomNotJoinableException;
import com.mayadem.battlearena.api.exception.ResourceNotFoundException;
import com.mayadem.battlearena.api.repository.BattleParticipantRepository;
import com.mayadem.battlearena.api.repository.BattleRoomRepository;

@Service
public class BattleRoomService {

    private final BattleRoomRepository battleRoomRepository;
    private final BattleParticipantRepository battleParticipantRepository;

    public BattleRoomService(BattleRoomRepository battleRoomRepository,
            BattleParticipantRepository battleParticipantRepository) {
        this.battleRoomRepository = battleRoomRepository;
        this.battleParticipantRepository = battleParticipantRepository;
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
        creator.setTotalBattles(creator.getTotalBattles() + 1);
        BattleParticipant firstParticipant = new BattleParticipant();
        firstParticipant.setWarrior(creator);
        firstParticipant.setBattleRoom(newBattleRoom);
        firstParticipant.setResult(BattleResult.IN_PROGRESS);
        newBattleRoom.getParticipants().add(firstParticipant);
        BattleRoom savedBattleRoom = battleRoomRepository.save(newBattleRoom);
        return BattleRoomDto.fromEntity(savedBattleRoom);
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

    @Transactional
    public BattleRoomDto joinBattleRoom(String roomCode, Warrior joiningWarrior) {

        BattleRoom battleRoom = battleRoomRepository.findByRoomCode(roomCode)
                .orElseThrow(() -> new ResourceNotFoundException("Battle room not found with code: " + roomCode));

        validateRoomIsJoinable(battleRoom, joiningWarrior);

        addParticipantAndUpdateRoom(battleRoom, joiningWarrior);

        BattleRoom updatedRoom = battleRoomRepository.save(battleRoom);

        return BattleRoomDto.fromEntity(updatedRoom);
    }

    public List<BattleRoomDto> getAvailableBattleRooms() {
        List<BattleRoom> availableRooms = battleRoomRepository.findByStatus(BattleStatus.WAITING);
        List<BattleRoomDto> availableRoomDtos = new ArrayList<>();
        for (BattleRoom room : availableRooms) {
            availableRoomDtos.add(BattleRoomDto.fromEntity(room));
        }
        return availableRoomDtos;
    }

    private void validateRoomIsJoinable(BattleRoom battleRoom, Warrior joiningWarrior) {
        if (battleRoom.getStatus() != BattleStatus.WAITING) {
            throw new BattleRoomNotJoinableException(
                    "Battle room is not waiting for players. Current status: " + battleRoom.getStatus());
        }

        if (battleRoom.getCurrentParticipants() >= battleRoom.getMaxParticipants()) {
            throw new BattleRoomNotJoinableException("Battle room is full.");
        }

        if (battleParticipantRepository.existsByBattleRoomAndWarrior(battleRoom, joiningWarrior)) {
            throw new BattleRoomNotJoinableException("Player is already in this battle room.");
        }
    }

    private void addParticipantAndUpdateRoom(BattleRoom battleRoom, Warrior joiningWarrior) {

        joiningWarrior.setTotalBattles(joiningWarrior.getTotalBattles() + 1);

        BattleParticipant newParticipant = new BattleParticipant();
        newParticipant.setWarrior(joiningWarrior);
        newParticipant.setBattleRoom(battleRoom);
        newParticipant.setResult(BattleResult.IN_PROGRESS);

        battleRoom.getParticipants().add(newParticipant);

        battleRoom.setCurrentParticipants(battleRoom.getCurrentParticipants() + 1);

        if (battleRoom.getCurrentParticipants().equals(battleRoom.getMaxParticipants())) {
            battleRoom.setStatus(BattleStatus.IN_PROGRESS);
            battleRoom.setStartedAt(java.time.Instant.now());
        }
    }
}