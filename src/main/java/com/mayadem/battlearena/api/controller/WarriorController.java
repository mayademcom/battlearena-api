package com.mayadem.battlearena.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mayadem.battlearena.api.dto.LoginRequest;
import com.mayadem.battlearena.api.dto.LoginResponse;
import com.mayadem.battlearena.api.dto.UpdateProfileRequestDto;
import com.mayadem.battlearena.api.dto.WarriorProfileDto;
import com.mayadem.battlearena.api.dto.WarriorRegistrationRequest;
import com.mayadem.battlearena.api.dto.WarriorRegistrationResponse;
import com.mayadem.battlearena.api.entity.Warrior;
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
    public ResponseEntity<WarriorRegistrationResponse> registerWarrior(
            @Valid @RequestBody WarriorRegistrationRequest request) {
        WarriorRegistrationResponse response = warriorService.registerWarrior(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(warriorService.login(request));
    }

    @GetMapping("/profile")
    public ResponseEntity<WarriorProfileDto> getAuthenticatedWarriorProfile(Authentication authentication) {
        Warrior authenticatedWarrior = (Warrior) authentication.getPrincipal();
        String username = authenticatedWarrior.getUsername();
        WarriorProfileDto profileDto = warriorService.getWarriorProfile(username);
        return new ResponseEntity<>(profileDto, HttpStatus.OK);
    }

    @PutMapping("/profile")
    public ResponseEntity<WarriorProfileDto> updateAuthenticatedWarriorProfile(@Valid @RequestBody UpdateProfileRequestDto requestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Warrior authenticatedWarrior = (Warrior) authentication.getPrincipal();
        String username = authenticatedWarrior.getUsername();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}