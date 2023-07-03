package com.example.betpawa.persistence.entity;

import java.math.BigDecimal;

import org.hibernate.annotations.GenericGenerator;

import com.example.betpawa.model.enums.StateType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bet_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BetItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    private Long betItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bet_id")
    @JsonIgnore
    private Bet bet;

    private BigDecimal odds;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private StateType state = StateType.PENDING;

    public boolean isPending() {
        return StateType.PENDING==state;
    }

    public boolean isWin() {
        return StateType.WIN==state;
    }
}
