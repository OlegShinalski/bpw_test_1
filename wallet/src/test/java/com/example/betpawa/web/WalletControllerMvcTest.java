package com.example.betpawa.web;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.betpawa.model.AccountDto;
import com.example.betpawa.model.AmountDto;
import com.example.betpawa.persistence.entity.Account;
import com.example.betpawa.service.WalletService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(WalletController.class)
public class WalletControllerMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WalletService service;

    @Test
    public void shouldCreateAccount() throws Exception {
        Long id = 12345L;
        String email = "aaa@bbb.ccc";
        Account createdAccount = Account.builder().id(id).email(email).build();

        given(service.create(any(Account.class)))
                .willReturn(createdAccount);

        MvcResult mvcResult = this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/account")
                                .content(asJsonString(Account.builder().email(email).build()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(id.toString())));
    }

    @Test
    public void shouldGetAccountBalance() throws Exception {
        Long id = 12345L;
        String email = "aaa@bbb.ccc";
        Account account = Account.builder().id(id).email(email).amount(BigDecimal.TEN).build();

        given(service.getById(id))
                .willReturn(account);

        MvcResult mvcResult = this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/account/balance/" + id.toString())
                )
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(BigDecimal.TEN.toString())));
    }

    @Test
    public void shouldDepositAccount() throws Exception {
        BigDecimal amount = new BigDecimal("123.45");
        Long id = 12345L;
        String email = "aaa@bbb.ccc";

        given(service.deposit(id, BigDecimal.ONE))
                .willReturn(AccountDto.builder().id(id).email(email).amount(amount).build());

        MvcResult mvcResult = this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/deposit/" + id)
                                .content(asJsonString(AmountDto.builder().summa(BigDecimal.ONE).build()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(amount.toString())));
    }

    @Test
    public void shouldWithdrawAccount() throws Exception {
        BigDecimal amount = new BigDecimal("123.45");
        Long id = 12345L;
        String email = "aaa@bbb.ccc";

        given(service.withdraw(id, BigDecimal.ONE))
                .willReturn(AccountDto.builder().id(id).email(email).amount(amount).build());

        MvcResult mvcResult = this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/withdraw/" + id)
                                .content(asJsonString(AmountDto.builder().summa(BigDecimal.ONE).build()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(amount.toString())));
    }

    @Test
    public void shouldGetAccountByEmail() throws Exception {
        Long id = 12345L;
        String email = "aaa@bbb.ccc";
        AccountDto dto = AccountDto.builder().id(id).email(email).amount(new BigDecimal("123.45")).build();

        given(service.getByEmail(email))
                .willReturn(dto);

        MvcResult mvcResult = this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/account?email=" + email)
                )
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(asJsonString(dto))));
    }

    @Test
    public void shouldGetAccountById() throws Exception {
        Long id = 12345L;
        String email = "aaa@bbb.ccc";
        AccountDto dto = AccountDto.builder().id(id).email(email).amount(new BigDecimal("123.45")).build();

        given(service.getAccountDtoById(id))
                .willReturn(dto);

        MvcResult mvcResult = this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/account/" + id)
                )
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(asJsonString(dto))));
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
