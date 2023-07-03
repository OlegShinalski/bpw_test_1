package com.example.betpawa.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import com.example.betpawa.model.enums.BetStateType;
import com.example.betpawa.model.enums.WinStateType;
import com.example.betpawa.persistence.entity.BetItem;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BetDto {

    private Long id;

    private Long accountId;

    private BigDecimal stake;

    private LocalDateTime created;

    private BetStateType state;

    private WinStateType winState;

    @Builder.Default
    private List<BetItemDto> items = new ArrayList<>();

    public void addItem(BetItemDto item) {
        getItems().add(item);
    }

}
