package com.mayadem.battlearena.api.dto;

public class LoginRequest {
    private String loginIdentifier; 
    private String password;

    
    public String getLoginIdentifier() {
        return loginIdentifier;
    }

    public void setLoginIdentifier(String loginIdentifier) {
        this.loginIdentifier = loginIdentifier;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}