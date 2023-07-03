package com.example.betpawa.mapper;

import com.example.betpawa.model.AccountOperationDto;
import com.example.betpawa.persistence.entity.AccountOperation;

public class AccountOperationMapper {

    public static AccountOperationDto mapToDto(AccountOperation entity) {
        return entity == null ? null :
                AccountOperationDto.builder()
                        .id(entity.getId())
                        .type(entity.getType())
                        .amount(entity.getAmount())
                        .created(entity.getCreated())
                        .build();
    }

    public static AccountOperation mapToEntity(AccountOperationDto dto) {
        return dto == null ? null :
                AccountOperation.builder()
                        .id(dto.getId())
                        .type(dto.getType())
                        .amount(dto.getAmount())
                        .created(dto.getCreated())
                        .build();
    }
}
