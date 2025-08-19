package com.mayadem.battlearena.api.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.mayadem.battlearena.api.dto.BattleOpponentDto;
import com.mayadem.battlearena.api.entity.enums.BattleStatus;
import com.mayadem.battlearena.api.entity.enums.BattleType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "battle_rooms")
public class BattleRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_code", unique = true, nullable = false)
    private String roomCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "battle_type", nullable = false)
    private BattleType battleType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BattleStatus status;

    @Column(name = "max_participants", nullable = false)
    private Integer maxParticipants = 2;

    @Column(name = "current_participants", nullable = false)
    private Integer currentParticipants = 0;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_warrior_id", nullable = false)
    private Warrior createdBy;

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name = "completed_at")
    private Instant completedAt;

    @OneToMany(mappedBy = "battleRoom", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BattleParticipant> participants = new ArrayList<>();

    public List<BattleParticipant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<BattleParticipant> participants) {
        this.participants = participants;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
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

    public Warrior getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Warrior createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }

    public BattleOpponentDto getOpponentInfo(Warrior me) {
    for (BattleParticipant participant : participants) {
        if (!participant.getWarrior().equals(me)) {
            BattleOpponentDto dto = new BattleOpponentDto();
            dto.setUsername(participant.getWarrior().getUsername());
            dto.setDisplayName(participant.getWarrior().getDisplayName());
            dto.setScore(participant.getFinalScore() != null ? participant.getFinalScore() : 0);
            return dto;
        }
    }
    throw new IllegalStateException("Opponent not found");
}

}