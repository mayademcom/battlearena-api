package com.mayadem.battlearena.api.controller;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mayadem.battlearena.api.dto.BattleRoomDto;
import com.mayadem.battlearena.api.dto.StartBattleRequestDto;
import com.mayadem.battlearena.api.dto.SubmitBattleResultRequestDto;
import com.mayadem.battlearena.api.entity.Warrior;
import com.mayadem.battlearena.api.exception.ResourceNotFoundException;
import com.mayadem.battlearena.api.service.BattleCompletionService;
import com.mayadem.battlearena.api.service.BattleRoomService;

import jakarta.validation.Valid;

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
    public ResponseEntity<Object> submitBattleResult(@Valid @RequestBody SubmitBattleResultRequestDto request,
                                                     @AuthenticationPrincipal Warrior requester){
        try {
            Object result = battleCompletionService.submitAndProcessScore(request, requester);

            return ResponseEntity.ok(result);

        } catch (IllegalArgumentException | ResourceNotFoundException e) {

            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    @GetMapping("/{battleRoomId}/status")
    public ResponseEntity<String> getBattleStatus(@PathVariable Long battleRoomId) {
        return battleRoomService.getBattleStatus(battleRoomId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().body("BattleRoom not found."));
    }
}