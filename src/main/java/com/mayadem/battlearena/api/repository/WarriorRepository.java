package com.mayadem.battlearena.api.repository;

import com.mayadem.battlearena.api.entity.Warrior;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WarriorRepository extends JpaRepository<Warrior, Long> {

    
    Optional<Warrior> findByUsername(String username);

    
    Optional<Warrior> findByEmail(String email);
}