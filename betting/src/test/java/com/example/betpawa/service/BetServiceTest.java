package com.example.betpawa.service;

import com.example.betpawa.mapper.BetMapper;
import com.example.betpawa.model.BetDto;
import com.example.betpawa.model.BetListItemDto;
import com.example.betpawa.model.PlaceBetDto;
import com.example.betpawa.persistence.entity.Bet;
import com.example.betpawa.persistence.repositary.BetRepositary;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BetServiceTest {

    @InjectMocks
    private BetService betService;
    @Mock
    private BetRepositary betRepository;
    @Mock
    private BetWalletService betWalletService;

    @BeforeEach
    void setup() {
    }

    @Test
    void shouldGetById() {
        Bet bet = Bet.builder().id(1L).accountId(123L).build();

        given(betRepository.getFirstById(1L))
                .willReturn(bet);

        BetDto result = betService.getById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getAccountId()).isEqualTo(123L);
        verify(betRepository).getFirstById(1L);
    }

    @Test
    void shouldFindAllBets() {
        Bet bet = Bet.builder().id(1L).accountId(123L).build();

        given(betRepository.findByAccountId(123L))
                .willReturn(Lists.newArrayList(bet));

        List<BetListItemDto> result = betService.findAllBets(123L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAccountId()).isEqualTo(123L);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        verify(betRepository).findByAccountId(123L);
    }

    @Test
    void shouldPlaceBetAsync() {
        PlaceBetDto betDto = PlaceBetDto.builder().accountId(1L).stake(BigDecimal.ONE).build();
        given(betRepository.save(any(Bet.class)))
                .will(returnsFirstArg());

        betService.placeBetAsync(betDto);

        verify(betWalletService).placeBetAsync(any(Bet.class));
    }

    @Test
    void shouldPlaceBet() {
        PlaceBetDto betDto = PlaceBetDto.builder().accountId(1L).stake(BigDecimal.ONE).build();
        given(betRepository.save(any(Bet.class)))
                .will(returnsFirstArg());

        betService.placeBet(betDto);

        verify(betWalletService).placeBet(1L, BigDecimal.ONE);
        verify(betRepository).save(any(Bet.class));
    }

}
