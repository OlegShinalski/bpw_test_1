package com.example.betpawa.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.betpawa.model.enums.BetStateType;
import com.example.betpawa.model.enums.WinStateType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class BetListItemDto implements Serializable {

    private Long id;
    private Long accountId;
    private BigDecimal totalOdds;
    private LocalDateTime created;
    private BetStateType state;
    private WinStateType winState;

}
