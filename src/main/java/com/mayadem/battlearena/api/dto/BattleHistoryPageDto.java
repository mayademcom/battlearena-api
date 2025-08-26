package com.mayadem.battlearena.api.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import com.mayadem.battlearena.api.entity.BattleParticipant;

public class BattleHistoryPageDto {

    private BattleHistorySummaryDto summary;
    private List<BattleHistoryDto> battles;
    private int currentPage;
    private int totalPages;
    private long totalBattles;

    public static BattleHistoryPageDto from(Page<BattleParticipant> participantPage, BattleHistorySummaryDto summary) {
        BattleHistoryPageDto pageDto = new BattleHistoryPageDto();

        pageDto.summary = summary;

        pageDto.currentPage = participantPage.getNumber() + 1;
        pageDto.totalPages = participantPage.getTotalPages();
        pageDto.totalBattles = participantPage.getTotalElements();

        List<BattleHistoryDto> battleDtos = participantPage.getContent().stream()
                .map(BattleHistoryDto::from)
                .collect(Collectors.toList());

        pageDto.battles = battleDtos;

        return pageDto;
    }

    public BattleHistorySummaryDto getSummary() {
        return summary;
    }

    public List<BattleHistoryDto> getBattles() {
        return battles;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public long getTotalBattles() {
        return totalBattles;
    }
}