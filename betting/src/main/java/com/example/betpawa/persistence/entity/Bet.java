package com.example.betpawa.persistence.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import com.example.betpawa.model.enums.BetStateType;
import com.example.betpawa.model.enums.WinStateType;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bet")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    private Long accountId;

    private BigDecimal stake;

    @CreationTimestamp
    @Getter
    private LocalDateTime created;

    @Enumerated(EnumType.STRING)
    @Getter
    @Builder.Default
    private BetStateType state = BetStateType.PENDING;

    @Enumerated(EnumType.STRING)
    @Getter
    private WinStateType winState;

    @OneToMany(mappedBy = "bet", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnore
    @Builder.Default
    private List<BetItem> items = new ArrayList<>();

    public BigDecimal getTotalOdds() {
        BigDecimal totalOdds = getItems().stream()
                .map(BetItem::getOdds)
                .reduce(BigDecimal.ONE, BigDecimal::multiply);
        return totalOdds;
    }

    public void addItem(BetItem item) {
        item.setBet(this);
        getItems().add(item);
    }

    public boolean isPending() {
        return BetStateType.PENDING==state;
    }

    public boolean isWin() {
        return WinStateType.WIN== winState;
    }

    public boolean isLoose() {
        return WinStateType.LOOSE== winState;
    }

    public void settledAsLoose() {
        state=BetStateType.SETTLED;
        winState =WinStateType.LOOSE;
    }

    public void settledAsWin() {
        state=BetStateType.SETTLED;
        winState =WinStateType.WIN;
    }
}
