package com.mayadem.battlearena.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mayadem.battlearena.api.dto.LoginRequest;
import com.mayadem.battlearena.api.dto.LoginResponse;
import com.mayadem.battlearena.api.dto.WarriorRegistrationRequest;
import com.mayadem.battlearena.api.dto.WarriorRegistrationResponse;
import com.mayadem.battlearena.api.entity.Warrior;
import com.mayadem.battlearena.api.exception.DuplicateResourceException;
import com.mayadem.battlearena.api.repository.WarriorRepository;

@Service
public class WarriorService {

    private static final Logger log = LoggerFactory.getLogger(WarriorService.class);

    private final WarriorRepository warriorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    
    public WarriorService(WarriorRepository warriorRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.warriorRepository = warriorRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public WarriorRegistrationResponse registerWarrior(WarriorRegistrationRequest request) {
        if (warriorRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new DuplicateResourceException("Username '" + request.getUsername() + "' is already taken.");
        }
        if (warriorRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Email '" + request.getEmail() + "' is already registered.");
        }
        Warrior newWarrior = new Warrior();
        newWarrior.setUsername(request.getUsername());
        newWarrior.setEmail(request.getEmail());
        newWarrior.setPassword(passwordEncoder.encode(request.getPassword()));
        newWarrior.setDisplayName(request.getUsername());
        newWarrior.setActive(true);
        Warrior savedWarrior = warriorRepository.save(newWarrior);
        return WarriorRegistrationResponse.fromEntity(savedWarrior);
    }

    
    public LoginResponse login(LoginRequest request) {
        String identifier = request.getLoginIdentifier();
        log.debug("Login attempt for identifier: {}", identifier);

        Warrior warrior = warriorRepository.findByUsernameOrEmail(identifier, identifier)
                .orElseThrow(() -> {
                    log.warn("Failed login attempt: User not found with identifier '{}'", identifier);
                    return new BadCredentialsException("Invalid credentials.");
                });

        if (!passwordEncoder.matches(request.getPassword(), warrior.getPassword())) {
            log.warn("Failed login attempt: Invalid password for user '{}'", identifier);
            throw new BadCredentialsException("Invalid credentials.");
        }

        if (!warrior.isEnabled()) {
            log.warn("Failed login attempt: User account is disabled for user '{}'", identifier);
            throw new DisabledException("User account is disabled.");
        }

        String jwtToken = jwtService.generateToken(warrior);
        log.info("Login successful for user: {}", warrior.getUsername());
        return new LoginResponse(jwtToken);
    }
}