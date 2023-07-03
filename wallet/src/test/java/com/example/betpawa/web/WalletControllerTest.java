package com.example.betpawa.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.betpawa.persistence.entity.Account;
import com.example.betpawa.service.WalletService;

import lombok.SneakyThrows;

@ExtendWith(MockitoExtension.class)
class WalletControllerTest {

    @InjectMocks
    private WalletController controller;
    @Mock
    private WalletService service;

    @SneakyThrows
    @Test
    void createAccount() {
        Long id = 12345L;
        Account account = Mockito.mock(Account.class);

        when(account.getId()).thenReturn(id);
        given(service.create(account))
                .willReturn(account);

        Long result = controller.createAccount(account).get();

        verify(service).create(account);
        assertThat(result).isEqualTo(id);
    }

    @SneakyThrows
    @Test
    void getAccountBalance() {
        Long id = 12345L;
        Account account = Mockito.mock(Account.class);
        when(account.getAmount()).thenReturn(BigDecimal.TEN);

        given(service.getById(id))
                .willReturn(account);

        BigDecimal result = controller.getAccountBalance(id).get();

        verify(service).getById(id);
        assertThat(result).isEqualTo(BigDecimal.TEN);
    }

}
