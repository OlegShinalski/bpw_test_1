package com.example.betpawa.model;

import com.example.betpawa.model.enums.WinStateType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SettleBetItemDto {
    private Long betItemId;
    private WinStateType state;
}
