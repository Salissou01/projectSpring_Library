package com.app.biblio.bean.request;

import java.math.BigDecimal;

public class PaiementRequest {

    private BigDecimal montantPaiement;

    // Constructeur par défaut
    public PaiementRequest() {
    }

    // Getters et setters
    public BigDecimal getMontantPaiement() {
        return montantPaiement;
    }

    public void setMontantPaiement(BigDecimal montantPaiement) {
        this.montantPaiement = montantPaiement;
    }
}
