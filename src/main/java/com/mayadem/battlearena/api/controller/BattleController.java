package com.mayadem.battlearena.api.controller;

import com.mayadem.battlearena.api.service.BattleRoomService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mayadem.battlearena.api.dto.BattleRoomDto;
import com.mayadem.battlearena.api.dto.StartBattleRequestDto;
import com.mayadem.battlearena.api.entity.Warrior;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@RestController
@RequestMapping("/api/battles")
public class BattleController {

    private final BattleRoomService battleRoomService;

    public BattleController(BattleRoomService battleRoomService) {
        this.battleRoomService = battleRoomService;
    }

    @PostMapping
    public ResponseEntity<BattleRoomDto> createBattle(
            @Valid @RequestBody StartBattleRequestDto requestDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Warrior creator = (Warrior) authentication.getPrincipal();

        BattleRoomDto createdBattleRoom = battleRoomService.createBattleRoom(requestDto, creator);

        return new ResponseEntity<>(createdBattleRoom, HttpStatus.CREATED);
    }

    @PostMapping("/{roomCode}/join")
    public ResponseEntity<BattleRoomDto> joinBattle(@PathVariable String roomCode) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Warrior joiningWarrior = (Warrior) authentication.getPrincipal();

        BattleRoomDto updatedRoom = battleRoomService.joinBattleRoom(roomCode, joiningWarrior);

        return new ResponseEntity<>(updatedRoom, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<BattleRoomDto>> getAvailableBattles() {

        List<BattleRoomDto> availableRooms = battleRoomService.getAvailableBattleRooms();

        return new ResponseEntity<>(availableRooms, HttpStatus.OK);
    }

}