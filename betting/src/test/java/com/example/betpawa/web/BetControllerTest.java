package com.example.betpawa.web;

import com.example.betpawa.model.BetDto;
import com.example.betpawa.model.BetListItemDto;
import com.example.betpawa.model.PlaceBetDto;
import com.example.betpawa.model.enums.BetStateType;
import com.example.betpawa.persistence.entity.Bet;
import com.example.betpawa.service.BetService;
import lombok.SneakyThrows;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BetControllerTest {

    @InjectMocks
    private BetController controller;
    @Mock
    private BetService service;

    @Test
    void placeBet() {
        Long id = 12345L;

        PlaceBetDto dto = PlaceBetDto.builder().accountId(id).stake(BigDecimal.ONE).build();
        Bet bet = Bet.builder().accountId(id).build();

        given(service.placeBet(dto))
                .willReturn(bet);

        BetDto result = controller.placeBet(dto);

        verify(service).placeBet(dto);
        assertThat(result.getAccountId()).isEqualTo(id);
        assertThat(result.getState()).isEqualTo(BetStateType.PENDING);
    }

    @SneakyThrows
    @Test
    void placeBetAsync() {
        Long id = 12345L;

        PlaceBetDto dto = PlaceBetDto.builder().accountId(id).stake(BigDecimal.ONE).build();
        BetDto betDto = BetDto.builder().id(1L).accountId(id).build();

        given(service.placeBetAsync(dto))
                .willReturn(CompletableFuture.completedFuture(betDto));

        BetDto result = controller.placeBetAsync(dto).get();

        verify(service).placeBetAsync(dto);
        assertThat(result).isEqualTo(betDto);
    }

    @SneakyThrows
    @Test
    void getBetById() {
        Long id = 12345L;

        BetDto dto = BetDto.builder().id(id).build();

        given(service.getById(id))
                .willReturn(dto);

        BetDto result = controller.getBetById(id).get();

        verify(service).getById(id);
        assertThat(result.getId()).isEqualTo(id);
    }

    @SneakyThrows
    @Test
    void findAllBets() {
        Long id = 12345L;

        BetListItemDto dto = BetListItemDto.builder().id(1L).accountId(id).build();

        given(service.findAllBets(id))
                .willReturn(Lists.newArrayList(dto));

        controller.findAllBets(id).get();

        verify(service).findAllBets(id);
    }
}
