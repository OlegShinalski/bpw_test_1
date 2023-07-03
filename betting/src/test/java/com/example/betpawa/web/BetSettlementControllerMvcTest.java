package com.example.betpawa.web;

import com.example.betpawa.TestUtils;
import com.example.betpawa.model.SettleBetItemDto;
import com.example.betpawa.model.SettleStatus;
import com.example.betpawa.model.enums.WinStateType;
import com.example.betpawa.service.BetSettlementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BetSettlementController.class)
public class BetSettlementControllerMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BetSettlementService service;

    @Test
    public void shouldSettleBet() throws Exception {
        SettleStatus status = new SettleStatus();

        given(service.settleBetItem(123L, WinStateType.WIN))
                .willReturn(status);

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/bet/settle")
                                .content(TestUtils.asJsonString(SettleBetItemDto.builder().betItemId(123L).state(WinStateType.WIN).build()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(TestUtils.asJsonString(status))));
    }

}
