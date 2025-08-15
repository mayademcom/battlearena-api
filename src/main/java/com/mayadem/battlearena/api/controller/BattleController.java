package com.mayadem.battlearena.api.controller;

import com.mayadem.battlearena.api.service.BattleRoomService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/battles")
public class BattleController {

    private final BattleRoomService battleRoomService;

    public BattleController(BattleRoomService battleRoomService) {
        this.battleRoomService = battleRoomService;
    }

}