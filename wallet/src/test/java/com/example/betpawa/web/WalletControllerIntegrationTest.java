package com.example.betpawa.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.betpawa.model.AccountDto;
import com.example.betpawa.persistence.entity.Account;

import liquibase.repackaged.org.apache.commons.lang3.RandomStringUtils;
import lombok.SneakyThrows;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class WalletControllerIntegrationTest {
    @Autowired
    private WalletController controller;

    @SneakyThrows
    @Test
    void shouldReturnAccountByEmail() {
        AccountDto dto = controller.getAccount("aaa@gmail.com").get();

        assertThat(dto.getEmail()).isEqualTo("aaa@gmail.com");
    }

    @SneakyThrows
    @Test
    void shouldCreateAndFindCreatedAccount() {
        String eMail = RandomStringUtils.randomAlphanumeric(10) + "@gmail.com";
        Account account = Account.builder().email(eMail).amount(BigDecimal.TEN).build();
        CompletableFuture<Long> result = controller.createAccount(account);
        Long id = result.get();

        assertThat(id).isNotNull();

        AccountDto dto = controller.getAccountById(id).get();

        assertThat(dto.getEmail()).isEqualTo(eMail);
    }

}
