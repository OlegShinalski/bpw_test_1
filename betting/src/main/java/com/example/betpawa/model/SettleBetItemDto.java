package com.example.betpawa.model;

import com.example.betpawa.model.enums.WinStateType;

import lombok.Data;

@Data
public class SettleBetItemDto {
    private Long betItemId;
    private WinStateType state;
}
