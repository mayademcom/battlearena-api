package com.mayadem.battlearena.api.entity;

import java.time.Instant;

import com.mayadem.battlearena.api.entity.enums.BattleResult;

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
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "battle_participants")
public class BattleParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "battle_room_id", nullable = false)
    private BattleRoom battleRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warrior_id", nullable = false)
    private Warrior warrior;

    @Column(name = "joined_at", nullable = false, updatable = false)
    private Instant joinedAt;

    @Column(name = "final_score")
    private Integer finalScore;

    @Column(name = "rank_points_before")
    private Integer rankPointsBefore;

    @Column(name = "rank_points_after")
    private Integer rankPointsAfter;

    @Column(name = "rank_points_change")
    private Integer rankPointsChange;

    @Enumerated(EnumType.STRING)
    @Column(name = "result")
    private BattleResult result;

    @Column(name = "position")
    private Integer position;

    @PrePersist
    protected void onCreate() {
        this.joinedAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BattleRoom getBattleRoom() {
        return battleRoom;
    }

    public void setBattleRoom(BattleRoom battleRoom) {
        this.battleRoom = battleRoom;
    }

    public Warrior getWarrior() {
        return warrior;
    }

    public void setWarrior(Warrior warrior) {
        this.warrior = warrior;
    }

    public Instant getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(Instant joinedAt) {
        this.joinedAt = joinedAt;
    }

    public Integer getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(Integer finalScore) {
        this.finalScore = finalScore;
    }

    public Integer getRankPointsBefore() {
        return rankPointsBefore;
    }

    public void setRankPointsBefore(Integer rankPointsBefore) {
        this.rankPointsBefore = rankPointsBefore;
    }

    public Integer getRankPointsAfter() {
        return rankPointsAfter;
    }

    public void setRankPointsAfter(Integer rankPointsAfter) {
        this.rankPointsAfter = rankPointsAfter;
    }

    public Integer getRankPointsChange() {
        return rankPointsChange;
    }

    public void setRankPointsChange(Integer rankPointsChange) {
        this.rankPointsChange = rankPointsChange;
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