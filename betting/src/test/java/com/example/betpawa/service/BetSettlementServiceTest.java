package com.example.betpawa.service;

import com.example.betpawa.model.SettleStatus;
import com.example.betpawa.model.enums.BetStateType;
import com.example.betpawa.model.enums.StateType;
import com.example.betpawa.model.enums.WinStateType;
import com.example.betpawa.persistence.entity.Bet;
import com.example.betpawa.persistence.entity.BetItem;
import com.example.betpawa.persistence.repositary.BetItemRepositary;
import com.example.betpawa.persistence.repositary.BetRepositary;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BetSettlementServiceTest {
    @InjectMocks
    private BetSettlementService betSettlementService;
    @Mock
    private BetWalletService betWalletService;
    @Mock
    private BetRepositary betRepository;
    @Mock
    private BetItemRepositary betItemRepositary;

    @Test
    void shouldPendingBetWhenSomeItemsArePending() {
        Long accountId = 123L;
        Long betItemId = 100L;
        Bet bet = Bet.builder().id(1L).accountId(accountId).stake(BigDecimal.TEN).state(BetStateType.PENDING).build();
        BetItem item1 = BetItem.builder().id(10L).betItemId(betItemId).state(StateType.PENDING).build();
        BetItem item2 = BetItem.builder().id(11L).betItemId(101L).state(StateType.PENDING).build();
        BetItem item3 = BetItem.builder().id(12L).betItemId(102L).state(StateType.PENDING).build();
        bet.addItem(item1);
        bet.addItem(item2);
        bet.addItem(item3);

        given(betItemRepositary.findByBetItemId(betItemId))
                .willReturn(Lists.newArrayList(item1));

        SettleStatus status = betSettlementService.settleBetItem(100L, WinStateType.WIN);

        assertThat(status.getAffected()).isEqualTo(1);
        assertThat(status.getWon()).isEqualTo(0);
        assertThat(status.getLost()).isEqualTo(0);
        assertThat(status.getPending()).isEqualTo(1);
    }

    @Test
    void shouldWinBetWhenAllItemsAreWin() {
        Long accountId1 = 123L;
        Long accountId2 = 124L;
        Long betItemId = 100L;
        Bet bet1 = Bet.builder().id(1L).accountId(accountId1).stake(BigDecimal.TEN).state(BetStateType.PENDING).build();
        BetItem item1 = BetItem.builder().id(10L).betItemId(betItemId).state(StateType.PENDING).odds(new BigDecimal("1.8")).build();
        BetItem item2 = BetItem.builder().id(11L).betItemId(101L).state(StateType.WIN).odds(new BigDecimal("1.7")).build();
        BetItem item3 = BetItem.builder().id(12L).betItemId(102L).state(StateType.WIN).odds(new BigDecimal("1.9")).build();
        bet1.addItem(item1);
        bet1.addItem(item2);
        bet1.addItem(item3);

        Bet bet2 = Bet.builder().id(2L).accountId(accountId2).stake(BigDecimal.TEN).state(BetStateType.PENDING).build();
        BetItem item2_1 = BetItem.builder().id(20L).betItemId(betItemId).state(StateType.PENDING).odds(new BigDecimal("1.7")).build();
        BetItem item2_2 = BetItem.builder().id(21L).betItemId(201L).state(StateType.WIN).odds(new BigDecimal("1.8")).build();
        bet2.addItem(item2_1);
        bet2.addItem(item2_2);

        given(betItemRepositary.findByBetItemId(betItemId))
                .willReturn(Lists.newArrayList(item1, item2_1));

        SettleStatus status = betSettlementService.settleBetItem(100L, WinStateType.WIN);

        verify(betRepository).save(bet1);
        verify(betWalletService).winBetAsync(accountId1, new BigDecimal("58.140"));
        verify(betWalletService).winBetAsync(accountId2, new BigDecimal("30.60"));

        assertThat(status.getAffected()).isEqualTo(2);
        assertThat(status.getWon()).isEqualTo(2);
        assertThat(status.getLost()).isEqualTo(0);
        assertThat(status.getPending()).isEqualTo(0);
    }

    @Test
    void shouldLooseBetWhenItemLoose() {
        Long accountId = 123L;
        Long betItemId = 100L;
        Bet bet = Bet.builder().id(1L).accountId(accountId).stake(BigDecimal.TEN).state(BetStateType.PENDING).build();
        BetItem item1 = BetItem.builder().id(10L).betItemId(betItemId).state(StateType.PENDING).odds(new BigDecimal("1.8")).build();
        BetItem item2 = BetItem.builder().id(11L).betItemId(101L).state(StateType.WIN).odds(new BigDecimal("1.7")).build();
        BetItem item3 = BetItem.builder().id(12L).betItemId(102L).state(StateType.WIN).odds(new BigDecimal("1.9")).build();
        bet.addItem(item1);
        bet.addItem(item2);
        bet.addItem(item3);

        given(betItemRepositary.findByBetItemId(betItemId))
                .willReturn(Lists.newArrayList(item1));

        SettleStatus status = betSettlementService.settleBetItem(100L, WinStateType.LOOSE);

        verify(betRepository).save(bet);
        verify(betWalletService, never()).winBetAsync(any(), any());

        assertThat(status.getAffected()).isEqualTo(1);
        assertThat(status.getWon()).isEqualTo(0);
        assertThat(status.getLost()).isEqualTo(1);
        assertThat(status.getPending()).isEqualTo(0);
    }
}
