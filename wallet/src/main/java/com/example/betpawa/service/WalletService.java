package com.example.betpawa.service;

import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.betpawa.wallet.WalletErrorCode;
import com.example.betpawa.exception.BusinessException;
import com.example.betpawa.mapper.AccountMapper;
import com.example.betpawa.model.AccountDto;
import com.example.betpawa.persistence.entity.Account;
import com.example.betpawa.persistence.entity.AccountOperation;
import com.example.betpawa.persistence.entity.AccountOperationType;
import com.example.betpawa.persistence.repositary.AccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@Validated
@RequiredArgsConstructor
public class WalletService {

    private final AccountRepository accountRepository;

    @Async
    public CompletableFuture<List<AccountDto>> findAll() {
        return accountRepository.findAllBy()
                .thenApply(accounts -> accounts.stream().map(AccountMapper::mapToDtoWithoutOperations).collect(toList()));
    }

    @Async
    @Transactional
    public AccountDto getByEmail(String email) {
        return AccountMapper.mapToDto(accountRepository.findByEmail(email));
    }

    @Async
    @Transactional
    public Account getById(Long id) {
        return accountRepository.getFirstById(id);
    }

    @Async
    @Transactional
    public AccountDto getAccountDtoById(Long id) {
        return AccountMapper.mapToDto(accountRepository.getFirstById(id));
    }

    @Async
    @Transactional
    public Account create(Account entity) {
        if (accountRepository.existsByEmail(entity.getEmail())) {
            throw new BusinessException(WalletErrorCode.ACCOUNT_EXISTS);
        }
        Account save = accountRepository.save(entity);
        return save;
    }

    @Async
    @Transactional
    public Account save(Account entity) {
        if (accountRepository.getFirstById(entity.getId()) == null) {
            throw new BusinessException(WalletErrorCode.INVALID_ACCOUNT);
        }
        return accountRepository.save(entity);
    }

    @Async
    @Transactional
    public AccountDto deposit(Long id, BigDecimal summa) {
        Account account = accountRepository.getFirstById(id);
        if (account == null) {
            throw new BusinessException(WalletErrorCode.INVALID_ACCOUNT);
        }
        account.setAmount(summa.add(account.getAmount()));
        addOperation(account, summa, AccountOperationType.DEPOSIT);
        return AccountMapper.mapToDto(accountRepository.save(account));
    }

    @Async
    @Transactional
    public AccountDto withdraw(Long id, BigDecimal withdrawSumma) {
        Account account = accountRepository.getFirstById(id);
        if (account == null) {
            throw new BusinessException(WalletErrorCode.INVALID_ACCOUNT);
        }
        BigDecimal amount = account.getAmount();
        if (amount.compareTo(withdrawSumma) < 0) {
            throw new BusinessException(WalletErrorCode.NOT_ENOUGHT_MONEY, "Not enough money in account");
        }
        account.setAmount(amount.subtract(withdrawSumma));
        addOperation(account, withdrawSumma, AccountOperationType.WITHDRAWAL);
        return AccountMapper.mapToDto(accountRepository.save(account));
    }

    private void addOperation(Account account, BigDecimal summa, AccountOperationType type) {
        account.addOperation(
                AccountOperation.builder().type(type).amount(summa).created(LocalDateTime.now()).build()
        );
    }

    public Account placeBet(Long id, BigDecimal betAmount) {
        Account account = getById(id);
        if (account == null) {
            throw new BusinessException(WalletErrorCode.INVALID_ACCOUNT);
        }
        BigDecimal summa = account.getAmount();
        if (summa.compareTo(betAmount) < 0) {
            throw new BusinessException(WalletErrorCode.NOT_ENOUGHT_MONEY, "Not enough money in account");
        }
        account.setAmount(summa.subtract(betAmount));
        addOperation(account, betAmount, AccountOperationType.BET);
        return accountRepository.save(account);
    }

    public Account winBet(Long id, BigDecimal betAmount) {
        Account account = getById(id);
        if (account == null) {
            throw new BusinessException(WalletErrorCode.INVALID_ACCOUNT);
        }
        account.setAmount(account.getAmount().add(betAmount));
        addOperation(account, betAmount, AccountOperationType.WIN);
        return accountRepository.save(account);
    }

}
