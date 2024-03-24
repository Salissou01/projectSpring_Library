package com.app.biblio.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.app.biblio.bean.Livre;
import com.app.biblio.bean.Reservation;
import com.app.biblio.bean.User;
import com.app.biblio.repository.LivreRepository;
import com.app.biblio.repository.ReservationRepository;
import com.app.biblio.repository.UserRepository;

@SpringBootTest
@ActiveProfiles("test")
public class ReservationServiceImplIntegrationTest {

    @Autowired
    private ReservationServiceImpl reservationService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private LivreRepository livreRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testCreateReservation() {
        // Setup
        User user = new User();
        user.setCIN("12345");
        userRepository.save(user);

        Livre livre = new Livre();
        livre.setId(1L);
        livre.setISBN("1234567890");
        livreRepository.save(livre);

        // Execute
        Reservation reservation = reservationService.createReservation(user, livre.getId());

        // Verify
        assertNotNull(reservation);
        assertEquals(user.getId(), reservation.getUser().getId());
        assertEquals(livre.getId(), reservation.getLivre().getId());
    }

    @Test
    public void testFindByLivreIsbnAndUserCin() {
        // Setup
        User user = new User();
        user.setCIN("12345");
        userRepository.save(user);

        Livre livre = new Livre();
        livre.setISBN("1234567890");
        livreRepository.save(livre);

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setLivre(livre);
        reservationRepository.save(reservation);

        // Execute
        Reservation foundReservation = reservationService.findByLivreIsbnAndUserCin("1234567890", "12345");

        // Verify
        assertNotNull(foundReservation);
        assertEquals(user.getId(), foundReservation.getUser().getId());
        assertEquals(livre.getId(), foundReservation.getLivre().getId());
    }
}
