package com.app.biblio.bean;

import static org.junit.jupiter.api.Assertions.assertEquals;


import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ReservationTest {

    private Reservation reservation;

    @BeforeEach
    public void setUp() {
        reservation = new Reservation();
    }

    @Test
    public void testId() {
        Long expectedId = 1L;
        reservation.setId(expectedId);
        assertEquals(expectedId, reservation.getId());
    }

    @Test
    public void testUser() {
        User expectedUser = new User();
        expectedUser.setUsername("Test User");
        reservation.setUser(expectedUser);
        assertEquals(expectedUser, reservation.getUser());
    }

    @Test
    public void testLivre() {
        Livre expectedLivre = new Livre();
        expectedLivre.setTitle("Test Livre");
        reservation.setLivre(expectedLivre);
        assertEquals(expectedLivre, reservation.getLivre());
    }

    @Test
    public void testDate() {
        LocalDateTime expectedDate = LocalDateTime.now();
        reservation.setDate(expectedDate);
        assertEquals(expectedDate, reservation.getDate());
    }

    @Test
    public void testStatut() {
        StatutReservation expectedStatut = StatutReservation.EN_ATTENTE;
        reservation.setStatut(expectedStatut);
        assertEquals(expectedStatut, reservation.getStatut());
    }

    
}
