package com.mayadem.battlearena.api.dto;

import com.mayadem.battlearena.api.entity.BattleRoom;
import com.mayadem.battlearena.api.entity.enums.BattleStatus;
import com.mayadem.battlearena.api.entity.enums.BattleType;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

public class BattleRoomDto {

    private Long id;
    private String roomCode;
    private BattleType battleType;
    private BattleStatus status;
    private Integer maxParticipants;
    private Integer currentParticipants;
    private Instant createdAt;
    private String createdByDisplayName;
    private List<BattleParticipantDto> participants;

    public static BattleRoomDto fromEntity(BattleRoom battleRoom) {
        BattleRoomDto dto = new BattleRoomDto();
        dto.setId(battleRoom.getId());
        dto.setRoomCode(battleRoom.getRoomCode());
        dto.setBattleType(battleRoom.getBattleType());
        dto.setStatus(battleRoom.getStatus());
        dto.setMaxParticipants(battleRoom.getMaxParticipants());
        dto.setCurrentParticipants(battleRoom.getCurrentParticipants());
        dto.setCreatedAt(battleRoom.getCreatedAt());
        dto.setCreatedByDisplayName(battleRoom.getCreatedBy().getDisplayName());

        if (battleRoom.getParticipants() != null) {

            List<BattleParticipantDto> participantDtos = new java.util.ArrayList<>();

            for (com.mayadem.battlearena.api.entity.BattleParticipant participantEntity : battleRoom
                    .getParticipants()) {

                BattleParticipantDto participantDto = BattleParticipantDto.fromEntity(participantEntity);

                participantDtos.add(participantDto);
            }

            dto.setParticipants(participantDtos);
        }

        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public BattleType getBattleType() {
        return battleType;
    }

    public void setBattleType(BattleType battleType) {
        this.battleType = battleType;
    }

    public BattleStatus getStatus() {
        return status;
    }

    public void setStatus(BattleStatus status) {
        this.status = status;
    }

    public Integer getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(Integer maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public Integer getCurrentParticipants() {
        return currentParticipants;
    }

    public void setCurrentParticipants(Integer currentParticipants) {
        this.currentParticipants = currentParticipants;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedByDisplayName() {
        return createdByDisplayName;
    }

    public void setCreatedByDisplayName(String createdByDisplayName) {
        this.createdByDisplayName = createdByDisplayName;
    }

    public List<BattleParticipantDto> getParticipants() {
        return participants;
    }

    public void setParticipants(List<BattleParticipantDto> participants) {
        this.participants = participants;
    }
}