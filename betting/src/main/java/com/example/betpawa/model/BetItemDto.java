package com.example.betpawa.model;

import java.math.BigDecimal;

import com.example.betpawa.model.enums.StateType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BetItemDto {

    private Long id;

    private Long betItemId;

    private BigDecimal odds;

    private StateType state = StateType.PENDING;

}
