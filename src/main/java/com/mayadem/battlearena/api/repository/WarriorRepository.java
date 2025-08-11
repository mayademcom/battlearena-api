package com.mayadem.battlearena.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mayadem.battlearena.api.entity.Warrior;

@Repository
public interface WarriorRepository extends JpaRepository<Warrior, Long> {

    
    Optional<Warrior> findByUsername(String username);

    
    Optional<Warrior> findByEmail(String email);

    Optional<Warrior> findByUsernameOrEmail(String username, String email);

    Optional<Warrior> findByDisplayNameAndIdNot(String displayName, Long id);
}