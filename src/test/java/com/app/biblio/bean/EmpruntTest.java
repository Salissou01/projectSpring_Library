package com.app.biblio.bean;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EmpruntTest {

    private Emprunt emprunt;

    @BeforeEach
    public void setUp() {
        emprunt = new Emprunt();
    }

    @Test
    public void testId() {
        Long expectedId = 1L;
        emprunt.setId(expectedId);
        assertEquals(expectedId, emprunt.getId());
    }

    @Test
    public void testStatut() {
        StatutEmprunt expectedStatut = StatutEmprunt.EN_ATTENTE;
        emprunt.setStatut(expectedStatut);
        assertEquals(expectedStatut, emprunt.getStatut());
    }

    @Test
    public void testLivre() {
        Livre expectedLivre = new Livre();
        expectedLivre.setTitle("Test Livre");
        emprunt.setLivre(expectedLivre);
        assertEquals(expectedLivre, emprunt.getLivre());
    }

    @Test
    public void testUser() {
        User expectedUser = new User();
        expectedUser.setUsername("Test User");
        emprunt.setUser(expectedUser);
        assertEquals(expectedUser, emprunt.getUser());
    }

    @Test
    public void testDateEmprunt() {
        Date expectedDate = new Date();
        emprunt.setDateEmprunt(expectedDate);
        assertEquals(expectedDate, emprunt.getDateEmprunt());
    }

    @Test
    public void testDateRetourPrevu() {
        Date expectedDate = new Date();
        emprunt.setDateRetourPrevu(expectedDate);
        assertEquals(expectedDate, emprunt.getDateRetourPrevu());
    }

    @Test
    public void testNombreExemplaires() {
        int expectedNombre = 2;
        emprunt.setNombreExemplaires(expectedNombre);
        assertEquals(expectedNombre, emprunt.getNombreExemplaires());
    }
}
