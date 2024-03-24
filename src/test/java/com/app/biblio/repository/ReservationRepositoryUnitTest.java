package com.app.biblio.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.app.biblio.bean.Reservation;
import com.app.biblio.service.ReservationServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ReservationRepositoryUnitTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    @Test
    public void testGetAllReservationByCin() {
        // Given
        String cin = "123456789";
        Pageable pageable = PageRequest.of(0, 10);
        Page<Reservation> expectedPage = new PageImpl<>(Collections.emptyList());
        when(reservationRepository.findByUserCIN(cin, pageable)).thenReturn(expectedPage);

        // When
        Page<Reservation> actualPage = reservationService.getAllReservationByCin(cin, pageable);

        // Then
        assertThat(actualPage).isEqualTo(expectedPage);
    }

    @Test
    public void testFindByLivreISBNAndUserCIN() {
        // Given
        String isbn = "123456789";
        String cin = "987654321";
        Reservation expectedReservation = new Reservation();
        when(reservationRepository.findByLivreISBNAndUserCIN(isbn, cin)).thenReturn(expectedReservation);

        // When
        Reservation actualReservation = reservationService.findByLivreIsbnAndUserCin(isbn, cin);

        // Then
        assertThat(actualReservation).isEqualTo(expectedReservation);
    }
}
