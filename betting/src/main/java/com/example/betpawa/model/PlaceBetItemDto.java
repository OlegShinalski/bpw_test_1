package com.example.betpawa.model;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class PlaceBetItemDto {
    private Long id;
    private BigDecimal odds;
}
