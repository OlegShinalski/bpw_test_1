package com.example.betpawa.mapper;

import com.example.betpawa.model.BetItemDto;
import com.example.betpawa.persistence.entity.BetItem;

public class BetItemMapper {

    public static BetItemDto mapToDto(BetItem entity) {
        return entity == null ? null :
                BetItemDto.builder()
                        .id(entity.getId())
                        .betItemId(entity.getBetItemId())
                        .odds(entity.getOdds())
                        .state(entity.getState())
                        .build();
    }

    public static BetItem mapToEntity(BetItemDto dto) {
        return dto == null ? null :
                BetItem.builder()
                        .id(dto.getId())
                        .betItemId(dto.getBetItemId())
                        .odds(dto.getOdds())
                        .state(dto.getState())
                        .build();
    }
}
