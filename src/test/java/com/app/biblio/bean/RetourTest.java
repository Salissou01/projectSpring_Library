package com.app.biblio.bean;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RetourTest {

    private Retour retour;

    @BeforeEach
    public void setUp() {
        retour = new Retour();
    }

    @Test
    public void testId() {
        Long expectedId = 1L;
        retour.setId(expectedId);
        assertEquals(expectedId, retour.getId());
    }

    @Test
    public void testEmprunt() {
        Emprunt expectedEmprunt = new Emprunt();
        retour.setEmprunt(expectedEmprunt);
        assertEquals(expectedEmprunt, retour.getEmprunt());
    }

    @Test
    public void testStatutRetour() {
        StatutRetour expectedStatutRetour = StatutRetour.EN_ATTENTE;
        retour.setStatutRetour(expectedStatutRetour);
        assertEquals(expectedStatutRetour, retour.getStatutRetour());
    }

    @Test
    public void testDateRetourEffectif() {
        Date expectedDate = new Date();
        retour.setDateRetourEffectif(expectedDate);
        assertEquals(expectedDate, retour.getDateRetourEffectif());
    }

    @Test
    public void testNote() {
        Integer expectedNote = 4;
        retour.setNote(expectedNote);
        assertEquals(expectedNote, retour.getNote());
    }
}
