package com.mayadem.battlearena.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mayadem.battlearena.api.dto.AuthResponse;
import com.mayadem.battlearena.api.dto.LoginRequest;
import com.mayadem.battlearena.api.dto.WarriorRegistrationRequest;
import com.mayadem.battlearena.api.dto.WarriorResponse;
import com.mayadem.battlearena.api.service.AuthService;
import com.mayadem.battlearena.api.service.WarriorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/warriors") 
public class WarriorController {

    private final WarriorService warriorService;
    private final AuthService authService; 

    
    public WarriorController(WarriorService warriorService, AuthService authService) {
        this.warriorService = warriorService;
        this.authService = authService;
    }

    
    @PostMapping("/register")
    public ResponseEntity<WarriorResponse> registerWarrior(@Valid @RequestBody WarriorRegistrationRequest request) {
        WarriorResponse response = warriorService.registerWarrior(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}