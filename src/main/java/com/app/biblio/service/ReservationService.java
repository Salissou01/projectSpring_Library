package com.app.biblio.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.app.biblio.bean.Reservation;
import com.app.biblio.bean.StatutReservation;
import com.app.biblio.bean.User;

public interface ReservationService {
    Reservation createReservation(User user, Long livreId);

    void save(Reservation reservation);

	
	Page<Reservation> getAllReservationByCin(String cin, Pageable pageable);

	Page<Reservation> findAll(Pageable pageable);

	Reservation findById(Long reservationId);



	Reservation findFirstReservationByLivre(Long bookId);

	Reservation findNextReservationByLivre(Long livreId, User currentUser);

	boolean isReservationValidated(Long reservationId);



	Reservation findByLivreIsbnAndUserCin(String iSBN, String cin);

	List<Reservation> findByLivreOrderByDateAsc(Long livreId);

	boolean deleteById(Long reservationId);

	long countByStatut(StatutReservation statut);


}
