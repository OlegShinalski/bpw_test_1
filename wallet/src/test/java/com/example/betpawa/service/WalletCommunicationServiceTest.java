package com.example.betpawa.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.betpawa.wallet.AmountRequest;
import com.betpawa.wallet.AmountResponse;
import com.example.betpawa.persistence.entity.Account;

import io.grpc.stub.StreamObserver;
import lombok.Data;

@ExtendWith(MockitoExtension.class)
public class WalletCommunicationServiceTest {

    @InjectMocks
    private WalletCommunicationService walletCommunicationService;
    @Mock
    private WalletService walletService;
    @Mock
    private WalletValidationService validationService;

    @BeforeEach
    void setup() {
    }

    @Data
    private static class StreamObserverMock implements StreamObserver<AmountResponse> {
        private AmountResponse nextAmountResponse;
        private Throwable throwable;
        private boolean completed = false;

        @Override
        public void onNext(AmountResponse amountResponse) {
            nextAmountResponse = amountResponse;
        }

        @Override
        public void onError(Throwable throwable) {
        }

        @Override
        public void onCompleted() {
            completed = true;
        }
    }

    @Test
    public void winMoney() {
        Long accountId = 1L;
        BigDecimal amount = new BigDecimal("123.45");
        Account account = Account.builder().id(accountId).build();

        given(walletService.getById(accountId)).willReturn(account);

        AmountRequest request = AmountRequest.newBuilder().setAmount(amount.toString()).setAccountId(accountId).build();

        StreamObserverMock observer = new StreamObserverMock();
        walletCommunicationService.winMoney(request, observer);

        verify(walletService).getById(accountId);
        verify(validationService).validateAccount(account);
        verify(walletService).winBet(accountId, amount);

        assertThat(observer.isCompleted()).isTrue();
        assertThat(observer.getThrowable()).isNull();
        AmountResponse response = observer.getNextAmountResponse();
        assertThat(new BigDecimal(response.getAmount())).isEqualTo(amount);
    }

    @Test
    public void reserveMoney() {
        Long accountId = 1L;
        BigDecimal amount = new BigDecimal("123.45");
        Account account = Account.builder().id(accountId).build();

        given(walletService.getById(accountId)).willReturn(account);

        AmountRequest request = AmountRequest.newBuilder().setAmount(amount.toString()).setAccountId(accountId).build();

        StreamObserverMock observer = new StreamObserverMock();
        walletCommunicationService.reserveMoney(request, observer);

        verify(walletService).getById(accountId);
        verify(validationService).validateAccount(account);
        verify(validationService).validateMoney(account, amount);
        verify(walletService).placeBet(accountId, amount);

        assertThat(observer.isCompleted()).isTrue();
        assertThat(observer.getThrowable()).isNull();
        AmountResponse response = observer.getNextAmountResponse();
        assertThat(new BigDecimal(response.getAmount())).isEqualTo(amount);
    }

}
