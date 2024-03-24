package com.app.biblio.bean.request;

import java.math.BigDecimal;

public class PenaliteRequest {
    private BigDecimal penaliteParJour;

    // Getters et setters
    public BigDecimal getPenaliteParJour() {
        return penaliteParJour;
    }

    public void setPenaliteParJour(BigDecimal penaliteParJour) {
        this.penaliteParJour = penaliteParJour;
    }
}
