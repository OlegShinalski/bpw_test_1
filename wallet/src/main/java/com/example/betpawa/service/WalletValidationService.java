package com.example.betpawa.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.betpawa.wallet.WalletErrorCode;
import com.example.betpawa.exception.BusinessException;
import com.example.betpawa.persistence.entity.Account;

@Service
public class WalletValidationService {

    public void validateAccount(Account account) {
        if (account == null) {
            throw new BusinessException(WalletErrorCode.INVALID_ACCOUNT);
        }
    }

    public void validateMoney(Account account, BigDecimal amount) {
        if (account.getAmount().compareTo(amount) < 0) {
            throw new BusinessException(WalletErrorCode.NOT_ENOUGHT_MONEY, "Amount=" + amount);
        }
    }
}
