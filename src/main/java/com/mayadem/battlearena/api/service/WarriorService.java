package com.mayadem.battlearena.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mayadem.battlearena.api.dto.AuthResponse; 
import com.mayadem.battlearena.api.dto.LoginRequest;
import com.mayadem.battlearena.api.dto.WarriorRegistrationRequest;
import com.mayadem.battlearena.api.dto.WarriorRegistrationResponse;
import com.mayadem.battlearena.api.entity.Warrior;
import com.mayadem.battlearena.api.exception.DuplicateResourceException;
import com.mayadem.battlearena.api.repository.WarriorRepository;

@Service
public class WarriorService implements UserDetailsService{

    private static final Logger log = LoggerFactory.getLogger(WarriorService.class);

    private final WarriorRepository warriorRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public WarriorService(WarriorRepository warriorRepository, PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager, JwtService jwtService) {
        this.warriorRepository = warriorRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
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
        Warrior savedWarrior = warriorRepository.save(newWarrior);
        return WarriorRegistrationResponse.fromEntity(savedWarrior);
    }

    public AuthResponse login(LoginRequest request) {
        try {
        
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getLoginIdentifier(), request.getPassword())
            );

            UserDetails userDetails = warriorRepository.findByUsernameOrEmail(request.getLoginIdentifier(), request.getLoginIdentifier())
                    .orElseThrow(() -> new IllegalStateException("User not found after successful authentication"));

            String jwtToken = jwtService.generateToken(userDetails);
            log.info("Login successful for user: {}", userDetails.getUsername());
            return new AuthResponse(jwtToken);

        } catch (AuthenticationException e) {
          
            log.warn("Failed login attempt for identifier: {}", request.getLoginIdentifier());
     
            throw e;
        }
    }

     @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        return warriorRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with identifier: " + usernameOrEmail));
    }
}