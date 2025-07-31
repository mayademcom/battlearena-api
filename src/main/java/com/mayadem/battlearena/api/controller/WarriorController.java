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
import com.mayadem.battlearena.api.dto.WarriorRegistrationResponse;
import com.mayadem.battlearena.api.service.WarriorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/warriors")
public class WarriorController {

    
    private final WarriorService warriorService;

    public WarriorController(WarriorService warriorService) {
        this.warriorService = warriorService;
    }

    @PostMapping("/register")
    public ResponseEntity<WarriorRegistrationResponse> registerWarrior(@Valid @RequestBody WarriorRegistrationRequest request) {
        WarriorRegistrationResponse response = warriorService.registerWarrior(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
       
        return ResponseEntity.ok(warriorService.login(request));
    }
}