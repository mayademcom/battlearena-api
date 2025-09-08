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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.mayadem.battlearena.api.dto.BattleHistorySummaryDto;
import com.mayadem.battlearena.api.dto.BattleRoomDto;
import com.mayadem.battlearena.api.dto.LeaderboardEntryDto;
import com.mayadem.battlearena.api.dto.LeaderboardStatsDto;
import com.mayadem.battlearena.api.dto.StartBattleRequestDto;
import com.mayadem.battlearena.api.dto.SubmitBattleResultRequestDto;
import com.mayadem.battlearena.api.entity.Warrior;
import com.mayadem.battlearena.api.exception.ResourceNotFoundException;
import com.mayadem.battlearena.api.service.BattleCompletionService;
import com.mayadem.battlearena.api.service.BattleRoomService;
import com.mayadem.battlearena.api.service.LeaderboardService;
import com.mayadem.battlearena.api.dto.BattleHistoryPageDto;
import com.mayadem.battlearena.api.entity.enums.BattleType;
import com.mayadem.battlearena.api.service.BattleHistoryService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.Optional;
import jakarta.validation.Valid;

import com.mayadem.battlearena.api.dto.ArenaLeaderboardDto;
import com.mayadem.battlearena.api.dto.BattleHistoryDto;

@RestController
@RequestMapping("/api/battles")
public class BattleController {

    private final BattleRoomService battleRoomService;
    private final BattleCompletionService battleCompletionService;
    private final BattleHistoryService battleHistoryService;
    private final LeaderboardService leaderboardService;
    private static final int MAX_PAGE_SIZE = 100;

    public BattleController(BattleRoomService battleRoomService, BattleCompletionService battleCompletionService,
            BattleHistoryService battleHistoryService, LeaderboardService leaderboardService) {
        this.battleRoomService = battleRoomService;
        this.battleCompletionService = battleCompletionService;
        this.battleHistoryService = battleHistoryService;
        this.leaderboardService = leaderboardService;
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
            @AuthenticationPrincipal Warrior requester) {
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

    @GetMapping("/history")
    public ResponseEntity<BattleHistoryPageDto> getBattleHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) BattleType battleType,
            @AuthenticationPrincipal Warrior warrior) {

        validatePageableParameters(page, size);

        Pageable pageable = PageRequest.of(page, size);

        BattleHistoryPageDto historyPage = battleHistoryService.getWarriorBattleHistory(
                warrior,
                Optional.ofNullable(battleType),
                pageable);

        return ResponseEntity.ok(historyPage);
    }

    private void validatePageableParameters(int page, int size) {
        if (page < 0) {
            throw new IllegalArgumentException("Page index must not be less than zero!");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Page size must be greater than zero!");
        }
        if (size > MAX_PAGE_SIZE) {
            throw new IllegalArgumentException("Page size must not be greater than " + MAX_PAGE_SIZE);
        }
    }

    @GetMapping("/history/{battleRoomId}")
    public ResponseEntity<BattleHistoryDto> getBattleDetails(
            @PathVariable Long battleRoomId,
            @AuthenticationPrincipal Warrior warrior) {

        BattleHistoryDto battleDetails = battleHistoryService.getBattleDetails(warrior, battleRoomId);
        return ResponseEntity.ok(battleDetails);
    }

    @GetMapping("/history/stats")
    public ResponseEntity<BattleHistorySummaryDto> getBattleStats(@AuthenticationPrincipal Warrior warrior) {
        BattleHistorySummaryDto summary = battleHistoryService.getBattleHistorySummary(warrior);
        return ResponseEntity.ok(summary);
    }

    // Ana leaderboard
    @GetMapping("/leaderboard")
    public ResponseEntity<ArenaLeaderboardDto> getArenaLeaderboard(@AuthenticationPrincipal Warrior currentWarrior) {
        ArenaLeaderboardDto dto = leaderboardService.getArenaLeaderboard(currentWarrior.getId());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/leaderboard/around-me")
    public ResponseEntity<List<LeaderboardEntryDto>> getLeaderboardAroundMe(
            @RequestParam(defaultValue = "5") int range,
            @AuthenticationPrincipal Warrior currentWarrior) {
        List<LeaderboardEntryDto> around = leaderboardService.getWarriorsAroundMe(currentWarrior.getId(), range);
        return ResponseEntity.ok(around);
    }

    // Global stats
    @GetMapping("/leaderboard/stats")
    public ResponseEntity<LeaderboardStatsDto> getLeaderboardStats() {
        LeaderboardStatsDto stats = leaderboardService.getGlobalStats();
        return ResponseEntity.ok(stats);
    }
}
