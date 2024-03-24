package com.app.biblio.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.app.biblio.bean.Livre;
import com.app.biblio.bean.Reservation;
import com.app.biblio.bean.StatutReservation;
import com.app.biblio.bean.User;
import com.app.biblio.repository.LivreRepository;
import com.app.biblio.repository.ReservationRepository;


@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private LivreRepository livreRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Override
    public Reservation createReservation(User user, Long livreId) {
        Livre livre = livreRepository.findById(livreId).orElseThrow(() -> new RuntimeException("Livre non trouvé"));
        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setLivre(livre);
        return reservationRepository.save(reservation);
    }
    @Override
    public void save(Reservation reservation) {
        reservationRepository.save(reservation);
    }
	@Override
	public Page<Reservation> getAllReservationByCin(String cin, Pageable pageable) {
		return reservationRepository.findByUserCIN(cin,pageable);
	}
	@Override
	public Page<Reservation> findAll(Pageable pageable) {
		return reservationRepository.findAll(pageable);
	}
	@Override
	public Reservation findById(Long reservationId) {
		return reservationRepository.getById(reservationId);
	}
	@Override
	public Reservation findFirstReservationByLivre(Long bookId) {
	    List<Reservation> reservations = reservationRepository.findByLivreOrderByDateAsc(bookId);
	    if (!reservations.isEmpty()) {
	        return reservations.get(0); // Retourne la première réservation de la liste
	    } else {
	        return null; // Ou gérer le cas où aucune réservation n'est trouvée
	    }
	}
	@Override
    public Reservation findNextReservationByLivre(Long livreId, User currentUser) {
        // Trouver toutes les réservations pour le livre spécifié, triées par date de réservation
        List<Reservation> reservations = reservationRepository.findByLivreOrderByDateAsc(livreId);
        Reservation nextReservation = null;

        for (Reservation reservation : reservations) {
            // Si la réservation actuelle est celle de l'utilisateur actuel, passer à la suivante
            if (reservation.getUser().equals(currentUser)) {
                continue;
            }
            // Si la réservation suivante n'a pas encore été trouvée, la définir comme telle
            if (nextReservation == null) {
                nextReservation = reservation;
            } else {
                // Si une réservation suivante a déjà été trouvée, arrêter la recherche
                break;
            }
        }

        return nextReservation;
    }

	@Override
	public boolean isReservationValidated(Long reservationId) {
	    Reservation reservation = reservationRepository.getById(reservationId);
	    return reservation != null && reservation.getStatut() == StatutReservation.VALIDE;
	}
	@Override
	public Reservation findByLivreIsbnAndUserCin(String iSBN, String cin) {
		
		return reservationRepository.findByLivreISBNAndUserCIN(iSBN,cin);
	}
	 @Override
	    public List<Reservation> findByLivreOrderByDateAsc(Long livreId) {
	        return reservationRepository.findByLivreOrderByDateAsc(livreId);
	    }
	    @Override
	    public boolean deleteById(Long reservationId) {
	        return reservationRepository.findById(reservationId)
	                .map(reservation -> {
	                    reservationRepository.delete(reservation);
	                    return true;
	                })
	                .orElse(false);
	    }
	
	    @Override
	    public long countByStatut(StatutReservation statut) {
	        return reservationRepository.countByStatut(statut);
	    }

}
