package com.example.betpawa.web;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.betpawa.mapper.BetMapper;
import com.example.betpawa.model.BetDto;
import com.example.betpawa.model.BetListItemDto;
import com.example.betpawa.model.PlaceBetDto;
import com.example.betpawa.persistence.entity.Bet;
import com.example.betpawa.service.BetService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class BetController {

    private final BetService betService;

    @GetMapping("/bet/{id}")
    @ResponseBody
    public CompletableFuture<BetDto> getBetById(@PathVariable Long id) {
        return CompletableFuture.supplyAsync(() -> betService.getById(id));
    }

    @GetMapping(value = { "/bets", "/bets/{accountId}" })
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
    public CompletableFuture<Bet> placeBetAsync(@RequestBody PlaceBetDto bet) {
        return betService.placeBetAsync(bet);
    }

}
