package com.mayadem.battlearena.api.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mayadem.battlearena.api.repository.WarriorRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final WarriorRepository warriorRepository;

    public UserDetailsServiceImpl(WarriorRepository warriorRepository) {
        this.warriorRepository = warriorRepository;
    }

    
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        return warriorRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with identifier: " + usernameOrEmail));
    }
}