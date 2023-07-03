package com.example.betpawa.model;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlaceBetDto {

    private Long accountId;
    private BigDecimal stake;
    @Builder.Default
    private List<PlaceBetItemDto> items = Lists.newArrayList();

}
