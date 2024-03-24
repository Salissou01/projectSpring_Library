package com.app.biblio.restController;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

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
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.app.biblio.bean.Reservation;
import com.app.biblio.service.ReservationService;

@ExtendWith(MockitoExtension.class)
public class ReservationRestControllerTest {

    @Mock
    private ReservationService reservationService;

    @InjectMocks
    private ReservationRestController reservationRestController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllReservations() {
        // Setup
        List<Reservation> reservations = new ArrayList<>();
        Page<Reservation> reservationPage = new PageImpl<>(reservations);
        when(reservationService.findAll(Pageable.unpaged())).thenReturn(reservationPage);

        // Execute
        ResponseEntity<Page<Reservation>> response = reservationRestController.getAllReservations(Pageable.unpaged());

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reservationPage, response.getBody());
    }


    @Test
    public void testDeleteReservation() {
        // Setup
        Long reservationId = 1L;
        when(reservationService.deleteById(reservationId)).thenReturn(true);

        // Execute
        ResponseEntity<?> response = reservationRestController.deleteReservation(reservationId);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("La réservation avec l'ID " + reservationId + " a été supprimée avec succès.", response.getBody());
    }

   
}
