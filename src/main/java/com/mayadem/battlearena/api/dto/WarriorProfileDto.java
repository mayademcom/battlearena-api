package com.mayadem.battlearena.api.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mayadem.battlearena.api.entity.Warrior;

public class WarriorProfileDto {

    private Long id;
    private String username;
    private String displayName;
    private String email;

    
    private Integer totalBattles;
    private Integer victories;
    private Integer defeats;
    private Double winRate;
    private Integer bestScore;
    private Integer rankPoints;

    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastLogin;

    
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getDisplayName() { return displayName; }
    public String getEmail() { return email; }
    public Integer getTotalBattles() { return totalBattles; }
    public Integer getVictories() { return victories; }
    public Integer getDefeats() { return defeats; }
    public Double getWinRate() { return winRate; }
    public Integer getBestScore() { return bestScore; }
    public Integer getRankPoints() { return rankPoints; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getLastLogin() { return lastLogin; }
    public void setWinRate(Warrior warrior) {
    if (warrior.getTotalBattles() > 0) {
        double winRateValue = ((double) warrior.getVictories() / warrior.getTotalBattles()) * 100.0;
        this.winRate = Math.round(winRateValue * 100.0) / 100.0;
    } else {
        this.winRate = 0.0;
    }
}


    /**
     * 
     * 
     * @param warrior 
     * @return 
     */
    public static WarriorProfileDto fromEntity(Warrior warrior) {
        WarriorProfileDto dto = new WarriorProfileDto();

        dto.id = warrior.getId();
        dto.username = warrior.getUsername();
        dto.displayName = warrior.getDisplayName();
        dto.email = warrior.getEmail();
        dto.totalBattles = warrior.getTotalBattles();
        dto.victories = warrior.getVictories();
        dto.defeats = warrior.getDefeats();
        dto.bestScore = warrior.getBestScore();
        dto.rankPoints = warrior.getRankPoints();

        
        if (warrior.getTotalBattles() > 0) {
            double rate = ((double) warrior.getVictories() / warrior.getTotalBattles()) * 100;
            BigDecimal bd = new BigDecimal(Double.toString(rate));
            bd = bd.setScale(2, RoundingMode.HALF_UP);
            dto.winRate = bd.doubleValue();
        } else {
            dto.winRate = 0.0;
        }

        
        if (warrior.getCreatedAt() != null) {
            dto.createdAt = warrior.getCreatedAt().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDateTime();
        }
        if (warrior.getLastLogin() != null) {
            dto.lastLogin = warrior.getLastLogin().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDateTime();
        }

        return dto;
    }
}