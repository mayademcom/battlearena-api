package com.mayadem.battlearena.api.controller;

import com.mayadem.battlearena.api.service.BattleCompletionService;
import com.mayadem.battlearena.api.service.BattleRoomService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mayadem.battlearena.api.dto.BattleResultResponseDto;
import com.mayadem.battlearena.api.dto.BattleRoomDto;
import com.mayadem.battlearena.api.dto.StartBattleRequestDto;
import com.mayadem.battlearena.api.dto.SubmitBattleResultRequestDto;
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

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/battles")
public class BattleController {

    private final BattleRoomService battleRoomService;
    private final BattleCompletionService battleCompletionService;

    public BattleController(BattleRoomService battleRoomService, BattleCompletionService battleCompletionService) {
        this.battleRoomService = battleRoomService;
        this.battleCompletionService = battleCompletionService;
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

    @PostMapping("/submit-result")
    public ResponseEntity<?> submitBattleResult(@Valid @RequestBody SubmitBattleResultRequestDto request,
            Principal principal) {
        try {
            // Principal'den kullanıcı adı
            String username = principal.getName();

            // BattleCompletionService ile battle sonucunu tamamla
            BattleResultResponseDto result = battleCompletionService.completeBattle(request, username);

            return ResponseEntity.ok(result);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    @GetMapping("/{battleRoomId}/status")
    public ResponseEntity<String> getBattleStatus(@PathVariable Long battleRoomId) {
        return battleRoomService.getBattleStatus(battleRoomId)
                .map(status -> ResponseEntity.ok(status))
                .orElse(ResponseEntity.badRequest().body("BattleRoom not found."));
    }

}