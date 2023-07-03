package com.example.betpawa.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDto {

    private Long id;
    private String email;
    private BigDecimal amount;

    @JsonIgnore
    @Builder.Default
    private List<AccountOperationDto> operations = new ArrayList<>();

    public void addOperation(AccountOperationDto operation) {
        getOperations().add(operation);
    }
}
