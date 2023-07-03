package com.example.betpawa.web;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.betpawa.mapper.AccountMapper;
import com.example.betpawa.model.AccountDto;
import com.example.betpawa.model.AccountOperationDto;
import com.example.betpawa.model.AmountDto;
import com.example.betpawa.persistence.entity.Account;
import com.example.betpawa.service.WalletService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class WalletController {

    private final WalletService walletService;

    @PostMapping("/account")
    @ResponseBody
    public CompletableFuture<Long> createAccount(@RequestBody Account account) {
        return CompletableFuture.supplyAsync(() -> walletService.create(account).getId());
    }

    @GetMapping("/account/operations/{id}")
    @ResponseBody
    public CompletableFuture<List<AccountOperationDto>> getAccountOperations(@PathVariable Long id) {
        return CompletableFuture.supplyAsync(() -> walletService.getAccountDtoById(id).getOperations());
    }

    @PostMapping("/deposit/{id}")
    @ResponseBody
    public CompletableFuture<BigDecimal> deposit(@RequestBody AmountDto amount, @PathVariable Long id) {
        return CompletableFuture.supplyAsync(
                () -> walletService.deposit(id, amount.getSumma()).getAmount()
        );
    }

    @PostMapping("/withdraw/{id}")
    @ResponseBody
    public CompletableFuture<BigDecimal> withdraw(@RequestBody AmountDto amount, @PathVariable Long id) {
        return CompletableFuture.supplyAsync(
                () -> walletService.withdraw(id, amount.getSumma()).getAmount()
        );
    }

    @GetMapping("/account/balance/{id}")
    @ResponseBody
    public CompletableFuture<BigDecimal> getAccountBalance(@PathVariable Long id) {
        return CompletableFuture.supplyAsync(() -> walletService.getById(id).getAmount());
    }
    @GetMapping("/accounts")
    @ResponseBody
    public CompletableFuture<List<AccountDto>> getAccounts() {
        return walletService.findAll();
    }

    @GetMapping("/account")
    @ResponseBody
    public CompletableFuture<AccountDto> getAccount(@RequestParam() String email) {
        return CompletableFuture.supplyAsync(
                () -> walletService.getByEmail(email)
        );
    }

    @GetMapping("/account/{id}")
    @ResponseBody
    public CompletableFuture<AccountDto> getAccountById(@PathVariable Long id) {
        return CompletableFuture.supplyAsync(
                () -> walletService.getAccountDtoById(id)
        );
    }

    @PutMapping("/account")
    @ResponseBody
    public CompletableFuture<AccountDto> updateAccount(@RequestBody AccountDto account) {
        return CompletableFuture.supplyAsync(
                () -> AccountMapper.mapToDto(walletService.save(AccountMapper.mapToEntity(account)))
        );
    }

}
