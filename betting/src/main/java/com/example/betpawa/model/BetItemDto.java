package com.example.betpawa.model;

import com.example.betpawa.model.enums.StateType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BetItemDto {

    private Long id;

    private Long betItemId;

    private BigDecimal odds;

    @Builder.Default
    private StateType state = StateType.PENDING;

}
