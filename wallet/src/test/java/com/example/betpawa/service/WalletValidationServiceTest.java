package com.example.betpawa.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.betpawa.wallet.WalletErrorCode;
import com.example.betpawa.exception.BusinessException;
import com.example.betpawa.persistence.entity.Account;

@ExtendWith(MockitoExtension.class)
public class WalletValidationServiceTest {

    @InjectMocks
    private WalletValidationService walletValidationService;

    @Test
    void shouldValidateNullAccount() {
        BusinessException result = assertThrows(BusinessException.class, () -> {
            walletValidationService.validateAccount(null);
        });

        assertThat(result.getErrorCode()).isEqualTo(WalletErrorCode.INVALID_ACCOUNT);
        assertThat(result).hasMessage(WalletErrorCode.INVALID_ACCOUNT.toString());
    }

    @Test
    void shouldValidateNotNullAccount() {
        walletValidationService.validateAccount(Account.builder().build());
    }

    @Test
    void shouldValidateAccountAmount() {
        Account account = Account.builder().amount(BigDecimal.ONE).build();
        BusinessException result = assertThrows(BusinessException.class, () -> {
            walletValidationService.validateMoney(account, BigDecimal.TEN);
        });

        assertThat(result.getErrorCode()).isEqualTo(WalletErrorCode.NOT_ENOUGHT_MONEY);
    }

    @Test
    void shouldValidateValidAccountAmount() {
        Account account = Account.builder().amount(BigDecimal.TEN).build();
        walletValidationService.validateMoney(account, BigDecimal.TEN);
    }
}
