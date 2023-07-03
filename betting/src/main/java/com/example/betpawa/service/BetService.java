package com.example.betpawa.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.example.betpawa.mapper.BetMapper;
import com.example.betpawa.model.BetDto;
import com.example.betpawa.model.BetListItemDto;
import com.example.betpawa.model.PlaceBetDto;
import com.example.betpawa.persistence.entity.Bet;
import com.example.betpawa.persistence.repositary.BetRepositary;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class BetService {

    private final BetRepositary betRepository;
    private final BetWalletService betWalletService;

    @Async
    @Transactional
    public BetDto getById(Long id) {
        return BetMapper.mapToDto(betRepository.getFirstById(id));
    }

    @Async
    @Transactional
    public List<BetListItemDto> findAllBets(Long accountId) {
        List<Bet> bets = accountId == null ? betRepository.findAll() : betRepository.findByAccountId(accountId);
        return bets.stream()
                .map(e -> BetListItemDto.builder()
                        .id(e.getId())
                        .accountId(e.getAccountId())
                        .created(e.getCreated())
                        .totalOdds(e.getTotalOdds())
                        .state(e.getState())
                        .winState(e.getWinState())
                        .build())
                .collect(Collectors.toList());
    }

    public CompletableFuture<Bet> placeBetAsync(PlaceBetDto betDto) {
        Bet bet = betRepository.save(BetMapper.mapToEntity(betDto));
        return betWalletService.placeBetAsync(bet);
    }

    public Bet placeBet(PlaceBetDto bet) {
        betWalletService.placeBet(bet.getAccountId(), bet.getStake());
        return betRepository.save(BetMapper.mapToEntity(bet));
    }

}
