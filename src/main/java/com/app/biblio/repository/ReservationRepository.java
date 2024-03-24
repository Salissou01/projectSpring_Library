package com.app.biblio.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.biblio.bean.Reservation;
import com.app.biblio.bean.StatutReservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
	@Query("SELECT r FROM Reservation r WHERE r.livre.id = :bookId AND r.statut = 'VALIDE' ORDER BY r.date ASC")
    List<Reservation> findByLivreOrderByDateAsc(@Param("bookId") Long bookId);
	Page<Reservation> findByUserCIN(String cin, Pageable pageable);
	Reservation findByLivreISBNAndUserCIN(String iSBN, String cin);
	long countByStatut(StatutReservation statut);
	
}
