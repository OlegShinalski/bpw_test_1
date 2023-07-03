package com.example.betpawa.service;

import com.example.betpawa.model.SettleStatus;
import com.example.betpawa.model.enums.StateType;
import com.example.betpawa.model.enums.WinStateType;
import com.example.betpawa.persistence.entity.Bet;
import com.example.betpawa.persistence.entity.BetItem;
import com.example.betpawa.persistence.repositary.BetItemRepositary;
import com.example.betpawa.persistence.repositary.BetRepositary;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class BetSettlementService {

    private final BetWalletService betWalletService;
    private final BetRepositary betRepository;
    private final BetItemRepositary betItemRepositary;

    private static ImmutableMap<WinStateType, StateType> STATES = ImmutableMap.of(
            WinStateType.WIN, StateType.WIN,
            WinStateType.LOOSE, StateType.LOOSE);

    @Transactional
    @SneakyThrows
    public SettleStatus settleBetItem(Long betItemId, WinStateType winState) {
        Map<Long, BigDecimal> accounts = Maps.newHashMap();

        SettleStatus status = new SettleStatus();
        List<BetItem> items = betItemRepositary.findByBetItemId(betItemId);
        items.stream().filter(BetItem::isPending).forEach(item -> {
            item.setState(STATES.get(winState));
            settleBet(item.getBet(), winState, accounts);
            incStatuses(status, item.getBet());
            betRepository.save(item.getBet());
        });

        accounts.entrySet().stream().forEach(
                e -> addMoneyToWalletAsync(e.getKey(), e.getValue())
        );
        return status;
    }

    private void incStatuses(SettleStatus status, Bet bet) {
        if (bet.isPending()) {
            status.incPending();
        }
        if (bet.isWin()) {
            status.incWon();
        }
        if (bet.isLoose()) {
            status.incLost();
        }
        status.incAffected();
    }

    @SneakyThrows
    private void settleBet(Bet bet, WinStateType winState, Map<Long, BigDecimal> accounsForUpdate) {
        if (bet.isPending()) {
            if (winState == WinStateType.LOOSE) {
                bet.settledAsLoose();
                log.debug("Bet is LOOSE id=" + bet.getId());

            } else if (allItemsWin(bet)) {
                bet.settledAsWin();
                BigDecimal amount = bet.getStake().multiply(bet.getTotalOdds());
                addAccountMoney(accounsForUpdate, bet.getAccountId(), amount);
                log.debug("Bet is WIN id=" + bet.getId() + " accountId=" + bet.getAccountId() + " amount=" + amount);

            } else {
                log.debug("Bet is still pending id=" + bet.getId());
            }
        }
    }

    private void addAccountMoney(Map<Long, BigDecimal> accounsForUpdate, Long accountId, BigDecimal amount) {
        BigDecimal existingAmoun = accounsForUpdate.getOrDefault(accountId, BigDecimal.ZERO);
        accounsForUpdate.put(accountId, existingAmoun.add(amount));
    }

    private void addMoneyToWalletAsync(Long accountId, BigDecimal stake) {
        log.debug("Adding money to accountId=" + accountId + " amount=" + stake);
        betWalletService.winBetAsync(accountId, stake);
    }

    private boolean allItemsWin(Bet bet) {
        return bet.getItems().stream().allMatch(BetItem::isWin);
    }

}
