package com.example.betpawa.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.betpawa.wallet.WalletErrorCode;
import com.example.betpawa.exception.BusinessException;
import com.example.betpawa.model.AccountDto;
import com.example.betpawa.model.AccountOperationDto;
import com.example.betpawa.persistence.entity.Account;
import com.example.betpawa.persistence.entity.AccountOperation;
import com.example.betpawa.persistence.entity.AccountOperationType;
import com.example.betpawa.persistence.repositary.AccountRepository;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    @InjectMocks
    private WalletService walletService;
    @Mock
    private AccountRepository accountRepository;

    @BeforeEach
    void setup() {
    }

    @Test
    void shouldGetById() {
        Account account = Account.builder().build();

        given(accountRepository.getFirstById(1L))
                .willReturn(account);

        Account result = walletService.getById(1L);

        assertThat(result).isEqualTo(account);
        verify(accountRepository).getFirstById(1L);
    }

    @Test
    void shouldGetByEmail() {
        String eMail = "aaa@bbb.ccc";
        Account account = Account.builder().email(eMail).build();

        given(accountRepository.findByEmail(eMail))
                .willReturn(account);

        AccountDto result = walletService.getByEmail(eMail);

        //        assertThat(result).isEqualTo(account);
        verify(accountRepository).findByEmail(eMail);
    }

    @Test
    void shouldCreateValidAccount() {
        String eMail = "aaa@bbb.ccc";
        Account account = Account.builder().email(eMail).build();
        given(accountRepository.existsByEmail(eMail))
                .willReturn(false);
        given(accountRepository.save(account))
                .willReturn(account);

        Account result = walletService.create(account);

        assertThat(result).isEqualTo(account);
        verify(accountRepository).existsByEmail(eMail);
        verify(accountRepository).save(account);
    }

    @Test
    void shouldNotCreateExistingAccount() {
        String eMail = "aaa@bbb.ccc";
        Account account = Account.builder().email(eMail).build();
        given(accountRepository.existsByEmail(eMail))
                .willReturn(true);

        assertThrows(BusinessException.class, () -> {
            walletService.create(account);
        });

        verify(accountRepository).existsByEmail(eMail);
        verify(accountRepository, never()).save(account);
    }

    @Test
    void shouldSaveAccount() {
        String eMail = "aaa@bbb.ccc";
        Long id = 1L;
        Account account = Account.builder().id(id).email(eMail).build();
        given(accountRepository.getFirstById(id))
                .willReturn(account);
        given(accountRepository.save(account))
                .willReturn(account);

        Account result = walletService.save(account);

        assertThat(result).isEqualTo(account);
        verify(accountRepository).getFirstById(id);
        verify(accountRepository).save(account);
    }

    @Test
    void shouldNotSaveNonExistingAccount() {
        String eMail = "aaa@bbb.ccc";
        Long id = 1L;
        Account account = Account.builder().id(id).email(eMail).build();
        given(accountRepository.getFirstById(id))
                .willReturn(null);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            walletService.save(account);
        });

        assertThat(exception).hasMessage(WalletErrorCode.INVALID_ACCOUNT.toString());
        verify(accountRepository).getFirstById(id);
        verify(accountRepository, never()).save(account);
    }

    @Test
    public void shouldDepositAccount() {
        Long id = 1L;
        Account account = Account.builder().id(id).amount(BigDecimal.TEN).build();

        given(accountRepository.getFirstById(id))
                .willReturn(account);
        given(accountRepository.save(any(Account.class)))
                .will(returnsFirstArg());

        AccountDto result = walletService.deposit(id, BigDecimal.ONE);

        verify(accountRepository).getFirstById(id);
        verify(accountRepository).save(account);
        assertThat(result.getAmount()).isEqualTo(BigDecimal.TEN.add(BigDecimal.ONE));
        assertThat(result.getOperations()).hasSize(1);
        AccountOperationDto operation = result.getOperations().get(0);
        assertThat(operation).extracting(AccountOperationDto::getType).isEqualTo(AccountOperationType.DEPOSIT);
        assertThat(operation).extracting(AccountOperationDto::getAmount).isEqualTo(BigDecimal.ONE);
    }

    @Test
    public void shouldNotDepositNonExistingAccount() {
        Long id = 1L;
        Account account = Account.builder().id(id).amount(BigDecimal.TEN).build();
        given(accountRepository.getFirstById(id))
                .willReturn(null);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            walletService.deposit(id, BigDecimal.ONE);
        });

        assertThat(exception).hasMessage(WalletErrorCode.INVALID_ACCOUNT.toString());
        verify(accountRepository).getFirstById(id);
        verify(accountRepository, never()).save(account);
    }

    @Test
    public void shouldWithdrawAccount() {
        Long id = 1L;
        Account account = Account.builder().id(id).amount(BigDecimal.TEN).build();

        given(accountRepository.getFirstById(id))
                .willReturn(account);
        given(accountRepository.save(any(Account.class)))
                .will(returnsFirstArg());

        AccountDto result = walletService.withdraw(id, BigDecimal.ONE);

        verify(accountRepository).getFirstById(id);
        verify(accountRepository).save(account);
        assertThat(result.getAmount()).isEqualTo(BigDecimal.TEN.subtract(BigDecimal.ONE));
        assertThat(result.getOperations()).hasSize(1);
        AccountOperationDto operation = result.getOperations().get(0);
        assertThat(operation).extracting(AccountOperationDto::getType).isEqualTo(AccountOperationType.WITHDRAWAL);
        assertThat(operation).extracting(AccountOperationDto::getAmount).isEqualTo(BigDecimal.ONE);
    }

    @Test
    public void shouldNotWithdrawNonExistingAccount() {
        Long id = 1L;
        Account account = Account.builder().id(id).amount(BigDecimal.TEN).build();
        given(accountRepository.getFirstById(id))
                .willReturn(null);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            walletService.withdraw(id, BigDecimal.ONE);
        });

        assertThat(exception).hasMessage(WalletErrorCode.INVALID_ACCOUNT.toString());
        verify(accountRepository).getFirstById(id);
        verify(accountRepository, never()).save(account);
    }

    @Test
    public void shouldNotWithdrawWhenNotEnoughtMoneyOnAccount() {
    }

    @Test
    public void shouldPlaceBet() {
    }

    @Test
    public void shouldNotPlaceBetOnInvalidAccount() {
    }

    @Test
    public void shouldWinBet() {
    }

    @Test
    public void shouldNotWinBetOnInvalidAccount() {
    }

}
