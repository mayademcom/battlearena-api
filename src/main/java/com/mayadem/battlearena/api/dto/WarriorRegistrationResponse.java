package com.mayadem.battlearena.api.dto;

import java.sql.Timestamp;

import com.mayadem.battlearena.api.entity.Warrior;


public class WarriorRegistrationResponse {

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
  
      
      @param warrior 
     * @return
     */
    

    public static WarriorRegistrationResponse fromEntity(Warrior warrior) {
        WarriorRegistrationResponse response = new WarriorRegistrationResponse(); 
        response.id = warrior.getId();
        response.username = warrior.getUsername();
        response.email = warrior.getEmail();
        response.displayName = warrior.getDisplayName();
        response.createdAt = warrior.getCreatedAt();
        return response;
    }
}