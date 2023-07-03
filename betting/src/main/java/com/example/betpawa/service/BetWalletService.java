package com.example.betpawa.service;

import static com.example.betpawa.mapper.BetMapper.mapToDto;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

import com.betpawa.wallet.AmountRequest;
import com.betpawa.wallet.AmountResponse;
import com.betpawa.wallet.WalletInteractionServiceGrpc;
import com.example.betpawa.exception.BusinessExceptionUtil;
import com.example.betpawa.model.BetDto;
import com.example.betpawa.persistence.entity.Bet;

import lombok.SneakyThrows;
import net.devh.boot.grpc.client.inject.GrpcClient;

@Service
public class BetWalletService {

    @GrpcClient("myWalletService")
    private WalletInteractionServiceGrpc.WalletInteractionServiceBlockingStub walletStub;

    @GrpcClient("myWalletService")
    private WalletInteractionServiceGrpc.WalletInteractionServiceStub walletStubAsync;

    public CompletableFuture<Bet> placeBetAsync(Bet bet) {
        AmountRequest request = AmountRequest.newBuilder()
                .setAccountId(bet.getAccountId())
                .setAmount(bet.getStake().toString())
                .setInfo("Info")
                .build();

        CompletableFutureObserver observer = new CompletableFutureObserver<AmountResponse, BetDto>(response -> mapToDto(bet));
        walletStubAsync.reserveMoney(request, observer);
        return observer.getFuture();
    }

    public BigDecimal placeBet(Long accountId, BigDecimal amount) {
        AmountRequest request = AmountRequest.newBuilder()
                .setAccountId(accountId)
                .setAmount(amount.toString())
                .setInfo("Info")
                .build();

        try {
            AmountResponse response = walletStub.reserveMoney(request);
            return new BigDecimal(response.getAmount());

        } catch (Throwable t) {
            throw BusinessExceptionUtil.transform(t);
        }
    }

    @SneakyThrows
    public CompletableFuture<AmountResponse> winBetAsync(Long accountId, BigDecimal amount) {
        AmountRequest request = AmountRequest.newBuilder()
                .setAccountId(accountId)
                .setAmount(amount.toString())
                .setInfo("Info")
                .build();

        CompletableFutureObserver observer = new CompletableFutureObserver<AmountResponse, AmountResponse>(e -> e);
        walletStubAsync.winMoney(request, observer);
        return observer.getFuture();
    }

    public BigDecimal winBet(Long accountId, BigDecimal amount) {
        AmountRequest request = AmountRequest.newBuilder()
                .setAccountId(accountId)
                .setAmount(amount.toString())
                .setInfo("Info")
                .build();
        try {
            AmountResponse response = walletStub.winMoney(request);
            return new BigDecimal(response.getAmount());

        } catch (Throwable t) {
            throw BusinessExceptionUtil.transform(t);
        }
    }

}
