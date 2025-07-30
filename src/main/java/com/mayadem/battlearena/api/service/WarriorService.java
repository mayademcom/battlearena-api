package com.mayadem.battlearena.api.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mayadem.battlearena.api.dto.WarriorRegistrationRequest;
import com.mayadem.battlearena.api.dto.WarriorResponse;
import com.mayadem.battlearena.api.entity.Warrior;
import com.mayadem.battlearena.api.exception.DuplicateResourceException;
import com.mayadem.battlearena.api.repository.WarriorRepository;

@Service
public class WarriorService {

    private final WarriorRepository warriorRepository;
    private final PasswordEncoder passwordEncoder;

    public WarriorService(WarriorRepository warriorRepository, PasswordEncoder passwordEncoder) {
        this.warriorRepository = warriorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public WarriorResponse registerWarrior(WarriorRegistrationRequest request) {
        if (warriorRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new DuplicateResourceException("Username '" + request.getUsername() + "' is already taken.");
        }

        if (warriorRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Email '" + request.getEmail() + "' is already registered.");
        }

        Warrior newWarrior = new Warrior();
        newWarrior.setUsername(request.getUsername());
        newWarrior.setEmail(request.getEmail());

        String hashedPassword = passwordEncoder.encode(request.getPassword());
        newWarrior.setPassword(hashedPassword);

        newWarrior.setDisplayName(request.getUsername());
        Warrior savedWarrior = warriorRepository.save(newWarrior);

        return WarriorResponse.fromEntity(savedWarrior);
    }
}