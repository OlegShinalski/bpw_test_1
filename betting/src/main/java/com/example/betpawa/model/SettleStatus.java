package com.example.betpawa.model;

import lombok.Data;
import lombok.Getter;

@Data
public class SettleStatus {
    @Getter
    private int affected;
    @Getter
    private int won;
    @Getter
    private int lost;
    @Getter
    private int pending;

    public void incAffected() {
        affected++;
    }

    public void incWon() {
        won++;
    }

    public void incLost() {
        lost++;
    }

    public void incPending() {
        pending++;
    }
}
