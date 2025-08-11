package com.mayadem.battlearena.api.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;


import com.mayadem.battlearena.api.dto.ChangePasswordRequestDto;
import com.mayadem.battlearena.api.dto.ChangePasswordResponseDto;
import com.mayadem.battlearena.api.dto.LoginRequest;
import com.mayadem.battlearena.api.dto.LoginResponse;
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

    @PutMapping("/change-password")
    public ResponseEntity<ChangePasswordResponseDto> changePassword(
    @Valid @RequestBody ChangePasswordRequestDto requestDto,
    @AuthenticationPrincipal Warrior authenticatedWarrior) {

    // ID’yi elle kullanıcıdan alma – backend'de set et
    requestDto.setWarriorId(authenticatedWarrior.getId());

    warriorService.changePassword(requestDto);

    return ResponseEntity.ok(
        new ChangePasswordResponseDto("Password changed successfully", LocalDateTime.now())
    );
}

}