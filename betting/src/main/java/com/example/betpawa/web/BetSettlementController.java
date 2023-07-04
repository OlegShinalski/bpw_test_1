package com.example.betpawa.web;

import com.example.betpawa.model.SettleBetItemDto;
import com.example.betpawa.model.SettleStatus;
import com.example.betpawa.service.BetSettlementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class BetSettlementController {

    private final BetSettlementService betSettlementService;

    @PostMapping("/bet/settle")
    @ResponseBody
    public SettleStatus settleBet(@RequestBody SettleBetItemDto settleDto) {
        return betSettlementService.settleBetItem(settleDto.getBetItemId(), settleDto.getState());
    }

}
