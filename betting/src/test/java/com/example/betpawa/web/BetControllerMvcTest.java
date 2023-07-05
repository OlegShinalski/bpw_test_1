package com.example.betpawa.web;

import com.example.betpawa.TestUtils;
import com.example.betpawa.model.BetDto;
import com.example.betpawa.model.BetListItemDto;
import com.example.betpawa.model.PlaceBetDto;
import com.example.betpawa.service.BetService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BetController.class)
public class BetControllerMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BetService service;

    @Test
    public void shouldPlaceBetAsync() throws Exception {
        BetDto betDto = BetDto.builder().id(1L).accountId(123L).build();

        given(service.placeBetAsync(any(PlaceBetDto.class)))
                .willReturn(CompletableFuture.completedFuture(betDto));

        MvcResult mvcResult = this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/bet/placeAsync")
                                .content(TestUtils.asJsonString(PlaceBetDto.builder().accountId(123L).stake(BigDecimal.ONE).build()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(TestUtils.asJsonString(betDto))));
    }

    @Test
    public void shouldGetBetById() throws Exception {
        Long id = 111L;

        BetDto dto = BetDto.builder().id(id).accountId(123L).build();

        given(service.getById(id))
                .willReturn(dto);

        MvcResult mvcResult = this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/bet/" + id.toString())
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(TestUtils.asJsonString(dto))));
    }

    @Test
    public void shouldFindAllBets() throws Exception {
        Long id = 111L;

        BetListItemDto dto = BetListItemDto.builder().id(1L).accountId(id).build();

        given(service.findAllBets(null))
                .willReturn(Lists.newArrayList(dto));

        MvcResult mvcResult = this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/bets")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("[" + TestUtils.asJsonString(dto) + "]")));
    }

    @Test
    public void shouldFindAllBetsByAccountId() throws Exception {
        Long accountId = 111L;

        BetListItemDto dto = BetListItemDto.builder().id(1L).accountId(accountId).build();

        given(service.findAllBets(accountId))
                .willReturn(Lists.newArrayList(dto));

        MvcResult mvcResult = this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/bets/" + accountId)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("[" + TestUtils.asJsonString(dto) + "]")));
    }

}
