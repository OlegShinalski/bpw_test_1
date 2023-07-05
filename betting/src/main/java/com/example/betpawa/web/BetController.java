package com.example.betpawa.web;

import com.example.betpawa.mapper.BetMapper;
import com.example.betpawa.model.BetDto;
import com.example.betpawa.model.BetListItemDto;
import com.example.betpawa.model.PlaceBetDto;
import com.example.betpawa.service.BetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@RestController
public class BetController {

    private final BetService betService;

    @GetMapping("/bet/{id}")
    @ResponseBody
    public CompletableFuture<BetDto> getBetById(@PathVariable Long id) {
        return CompletableFuture.supplyAsync(() -> betService.getById(id));
    }

    @GetMapping(value = {"/bets", "/bets/{accountId}"})
    @ResponseBody
    public CompletableFuture<List<BetListItemDto>> findAllBets(@PathVariable(required = false) Long accountId) {
        return CompletableFuture.supplyAsync(() -> betService.findAllBets(accountId));
    }

    @PostMapping("/bet/place")
    @ResponseBody
    public BetDto placeBet(@RequestBody PlaceBetDto bet) {
        return BetMapper.mapToDto(betService.placeBet(bet));
    }

    @PostMapping("/bet/placeAsync")
    @ResponseBody
    public CompletableFuture<BetDto> placeBetAsync(@RequestBody PlaceBetDto bet) {
        return betService.placeBetAsync(bet);
    }

}
