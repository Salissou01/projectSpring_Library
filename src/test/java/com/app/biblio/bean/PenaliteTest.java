package com.app.biblio.bean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

public class PenaliteTest {

    @Test
    public void testConstructorAndGetters() {
        // Given
        Retour retour = new Retour();
        BigDecimal montant = new BigDecimal("10.00");
        StatutPenalite statutPenalite = StatutPenalite.NON_PAYEE;

        // When
        Penalite penalite = new Penalite(retour, montant, statutPenalite);

        // Then
        assertNotNull(penalite);
        assertEquals(retour, penalite.getRetour());
        assertEquals(montant, penalite.getMontant());
        assertEquals(statutPenalite, penalite.getStatutPenalite());
    }

    @Test
    public void testSetters() {
        // Given
        Penalite penalite = new Penalite();
        Retour retour = new Retour();
        BigDecimal montant = new BigDecimal("20.00");
        StatutPenalite statutPenalite = StatutPenalite.PAYEE;

        // When
        penalite.setRetour(retour);
        penalite.setMontant(montant);
        penalite.setStatutPenalite(statutPenalite);

        // Then
        assertEquals(retour, penalite.getRetour());
        assertEquals(montant, penalite.getMontant());
        assertEquals(statutPenalite, penalite.getStatutPenalite());
    }
}
