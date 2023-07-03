package com.example.betpawa.persistence.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class Amount implements Serializable {

    private BigDecimal summa;

    public BigDecimal getSumma() {
        return summa;
    }

    public void setSumma(BigDecimal summa) {
        this.summa = summa;
    }
}
