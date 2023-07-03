package com.example.betpawa.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.betpawa.persistence.entity.AccountOperationType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountOperationDto {

    private Long id;
    private AccountOperationType type;
    private BigDecimal amount;
    private LocalDateTime created;

}
