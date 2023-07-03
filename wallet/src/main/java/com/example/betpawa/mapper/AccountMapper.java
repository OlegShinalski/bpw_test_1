package com.example.betpawa.mapper;

import static java.util.stream.Collectors.toList;

import com.example.betpawa.model.AccountDto;
import com.example.betpawa.persistence.entity.Account;

public class AccountMapper {

    public static AccountDto mapToDto(Account entity) {
        return entity == null ? null :
                AccountDto.builder()
                        .id(entity.getId())
                        .email(entity.getEmail())
                        .amount(entity.getAmount())
                        .operations(entity.getOperations().stream().map(AccountOperationMapper::mapToDto).collect(toList()))
                        .build();
    }

    public static AccountDto mapToDtoWithoutOperations(Account entity) {
        return entity == null ? null :
                AccountDto.builder()
                        .id(entity.getId())
                        .email(entity.getEmail())
                        .amount(entity.getAmount())
                        .build();
    }

    public static Account mapToEntity(AccountDto dto) {
        if (dto == null) {
            return null;
        } else {
            Account account = Account.builder()
                    .id(dto.getId())
                    .email(dto.getEmail())
                    .amount(dto.getAmount())
                    .build();
            dto.getOperations().stream().map(AccountOperationMapper::mapToEntity).forEach(account::addOperation);
            return account;
        }
    }
}
