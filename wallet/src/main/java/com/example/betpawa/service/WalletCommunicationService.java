package com.example.betpawa.service;

import java.math.BigDecimal;
import java.util.logging.Logger;

import org.springframework.transaction.annotation.Transactional;

import com.betpawa.wallet.AmountRequest;
import com.betpawa.wallet.AmountResponse;
import com.betpawa.wallet.WalletInteractionServiceGrpc;
import com.example.betpawa.Utils;
import com.example.betpawa.persistence.entity.Account;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
@Slf4j
public class WalletCommunicationService extends WalletInteractionServiceGrpc.WalletInteractionServiceImplBase {

    private final WalletService walletService;
    private final WalletValidationService validationService;

    @Override
    @Transactional
    public void winMoney(AmountRequest request, StreamObserver<AmountResponse> responseObserver) {
        Long accountId = request.getAccountId();
        BigDecimal amount = new BigDecimal(request.getAmount());
        Account account = walletService.getById(accountId);

        validationService.validateAccount(account);

        walletService.winBet(accountId, amount);
        log.info("Adding money to acocunt id=" + accountId + " value=" + amount);

        AmountResponse response = AmountResponse.newBuilder()
                .setTimestamp(Utils.getTimestamp())
                .setAmount(amount.toString())
                .setInfo("Ok")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    @Transactional
    public void reserveMoney(AmountRequest request, StreamObserver<AmountResponse> responseObserver) {
        Long accountId = request.getAccountId();
        BigDecimal amount = new BigDecimal(request.getAmount());
        Account account = walletService.getById(accountId);

        validationService.validateAccount(account);
        validationService.validateMoney(account, amount);

        walletService.placeBet(accountId, amount);

        AmountResponse response = AmountResponse.newBuilder()
                .setTimestamp(Utils.getTimestamp())
                .setAmount(amount.toString())
                .setInfo("Ok")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}
