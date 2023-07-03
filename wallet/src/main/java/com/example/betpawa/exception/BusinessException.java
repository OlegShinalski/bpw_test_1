package com.example.betpawa.exception;

import com.betpawa.wallet.WalletErrorCode;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private WalletErrorCode errorCode;

    public BusinessException(WalletErrorCode errorCode) {
        super(errorCode.name());
        this.errorCode = errorCode;
    }

    public BusinessException(WalletErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
