package com.mayadem.battlearena.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.mayadem.battlearena.api.dto.AuthResponse;
import com.mayadem.battlearena.api.dto.LoginRequest;
import com.mayadem.battlearena.api.repository.WarriorRepository;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final WarriorRepository warriorRepository;

    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService, WarriorRepository warriorRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.warriorRepository = warriorRepository;
    }

    public AuthResponse login(LoginRequest request) {
      
        String identifier = request.getLoginIdentifier();

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(identifier, request.getPassword())
        );

        UserDetails userDetails = warriorRepository.findByUsernameOrEmail(identifier, identifier)
                .orElseThrow(() -> new IllegalStateException("User not found after successful authentication"));

        String jwtToken = jwtService.generateToken(userDetails);
        log.info("Login successful for user: {}", userDetails.getUsername());
        return new AuthResponse(jwtToken);
    }
}