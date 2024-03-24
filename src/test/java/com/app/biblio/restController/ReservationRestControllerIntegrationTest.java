package com.app.biblio.restController;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.app.biblio.bean.Livre;
import com.app.biblio.bean.Reservation;
import com.app.biblio.bean.StatutReservation;
import com.app.biblio.service.NotificationService;
import com.app.biblio.service.ReservationService;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ReservationRestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ReservationService reservationService;
    
    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private ReservationRestController reservationRestController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(reservationRestController).build();
    }

    @Test
    public void testValidateReservation() throws Exception {
        // Setup
        Long reservationId = 1L;
        Reservation reservation = new Reservation();
        Livre livre = new Livre();
        livre.setTitle("Titre du livre");
       
        reservation.setLivre(livre);
        reservation.setId(reservationId);
        reservation.setStatut(StatutReservation.EN_ATTENTE);

        // Mocking service method calls
        when(reservationService.findById(reservationId)).thenReturn(reservation);

        // Execute
        mockMvc.perform(post("/api/reservations/validate/{id}", reservationId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Verify
       
        verify(reservationService).findById(reservationId);
        // Verify that the reservation's status is updated to VALID after the request is processed
        assertEquals(StatutReservation.VALIDE, reservation.getStatut());
    }
}
