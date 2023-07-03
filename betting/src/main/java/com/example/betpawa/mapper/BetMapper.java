package com.example.betpawa.mapper;

import static java.util.stream.Collectors.toList;

import com.example.betpawa.model.BetDto;
import com.example.betpawa.model.PlaceBetDto;
import com.example.betpawa.persistence.entity.Bet;
import com.example.betpawa.persistence.entity.BetItem;

public class BetMapper {

    public static BetDto mapToDto(Bet entity) {
        return entity == null ? null :
                BetDto.builder()
                        .id(entity.getId())
                        .accountId(entity.getAccountId())
                        .stake(entity.getStake())
                        .state(entity.getState())
                        .winState(entity.getWinState())
                        .created(entity.getCreated())
                        .items(entity.getItems().stream().map(BetItemMapper::mapToDto).collect(toList()))
                        .build();
    }

    public static Bet mapToEntity(BetDto dto) {
        if (dto == null) {
            return null;
        } else {
            Bet bet = Bet.builder()
                    .id(dto.getId())
                    .accountId(dto.getAccountId())
                    .stake(dto.getStake())
                    .state(dto.getState())
                    .winState(dto.getWinState())
                    .created(dto.getCreated())
                    .build();
            dto.getItems().stream().map(BetItemMapper::mapToEntity).forEach(bet::addItem);
            return bet;
        }
    }

    public static Bet mapToEntity(PlaceBetDto dto) {
        Bet bet = Bet.builder().accountId(dto.getAccountId()).stake(dto.getStake())
                .build();
        dto.getItems().stream().map(e -> BetItem.builder().betItemId(e.getId()).odds(e.getOdds()).build())
                .forEach(bet::addItem);
        return bet;
    }
}
