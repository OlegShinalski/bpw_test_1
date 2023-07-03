package com.example.betpawa.web;

import com.example.betpawa.model.SettleBetItemDto;
import com.example.betpawa.model.SettleStatus;
import com.example.betpawa.model.enums.WinStateType;
import com.example.betpawa.service.BetSettlementService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BetSettlementControllerTest {

    @InjectMocks
    private BetSettlementController controller;
    @Mock
    private BetSettlementService service;

    @Test
    void placeBet() {
        SettleBetItemDto dto = SettleBetItemDto.builder().betItemId(1L).state(WinStateType.WIN).build();

        SettleStatus result = controller.settleBet(dto);

        verify(service).settleBetItem(1L, WinStateType.WIN);
    }
}
