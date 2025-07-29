package com.mayadem.battlearena.api.dto;

import com.mayadem.battlearena.api.entity.Warrior;
import java.sql.Timestamp;

// Bu DTO, şifre gibi hassas bilgileri içermez.
public class WarriorResponse {

    private Long id;
    private String username;
    private String email;
    private String displayName;
    private Timestamp createdAt;

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getDisplayName() { return displayName; }
    public Timestamp getCreatedAt() { return createdAt; }

    /**
  
     * için kullanılır. Bu, çok yaygın ve temiz bir yöntemdir.
     * @param warrior Veritabanından gelen Warrior nesnesi.
     * @return Dış dünyaya gösterilmeye hazır, güvenli DTO.
     */
    public static WarriorResponse fromEntity(Warrior warrior) {
        WarriorResponse response = new WarriorResponse();
        response.id = warrior.getId();
        response.username = warrior.getUsername();
        response.email = warrior.getEmail();
        response.displayName = warrior.getDisplayName();
        response.createdAt = warrior.getCreatedAt();
        return response;
    }
}