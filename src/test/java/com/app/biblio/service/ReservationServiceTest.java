package com.app.biblio.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.app.biblio.bean.Reservation;
import com.app.biblio.repository.ReservationRepository;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllReservationByCin() {
        // Setup
        String cin = "123456789";
        List<Reservation> reservations = new ArrayList<>();
        Reservation reservation1 = new Reservation();
        reservations.add(reservation1);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Reservation> expectedPage = new PageImpl<>(reservations);

        // Utilisez la méthode existante pour récupérer les réservations par CIN et pagination
        when(reservationRepository.findByUserCIN(cin, pageable)).thenReturn(expectedPage);

        // Execute
        Page<Reservation> result = reservationService.getAllReservationByCin(cin, pageable);

        // Verify
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }


    @Test
    public void testFindByLivreOrderByDateAsc() {
        // Setup
        Long livreId = 1L;
        List<Reservation> reservations = new ArrayList<>();
        // Utilisez LocalDateTime au lieu de LocalDate
        LocalDateTime date1 = LocalDateTime.of(2023, 1, 1, 0, 0); // Ajoutez l'heure et les minutes si nécessaire
        LocalDateTime date2 = LocalDateTime.of(2023, 1, 2, 0, 0); // Ajoutez l'heure et les minutes si nécessaire
        Reservation reservation1 = new Reservation();
        reservation1.setDate(date1); // Utilisez setDate avec LocalDateTime
        Reservation reservation2 = new Reservation();
        reservation2.setDate(date2); // Utilisez setDate avec LocalDateTime
        reservations.add(reservation1);
        reservations.add(reservation2);

        when(reservationRepository.findByLivreOrderByDateAsc(livreId)).thenReturn(reservations);

        // Execute
        List<Reservation> result = reservationService.findByLivreOrderByDateAsc(livreId);

        // Verify
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(date1, result.get(0).getDate());
        assertEquals(date2, result.get(1).getDate());
    }


}
