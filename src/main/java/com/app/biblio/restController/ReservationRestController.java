package com.app.biblio.restController;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.biblio.bean.CustomUserDetails;
import com.app.biblio.bean.Emprunt;
import com.app.biblio.bean.Livre;
import com.app.biblio.bean.Reservation;
import com.app.biblio.bean.StatutEmprunt;
import com.app.biblio.bean.StatutReservation;
import com.app.biblio.bean.User;
import com.app.biblio.bean.request.EmpruntRequest;
import com.app.biblio.bean.request.ReservationRequest;
import com.app.biblio.service.EmpruntService;
import com.app.biblio.service.LivreService;
import com.app.biblio.service.NotificationService;
import com.app.biblio.service.PenaliteService;
import com.app.biblio.service.ReservationService;
import com.app.biblio.service.UserService;

@RestController
@RequestMapping("/api/reservations")
public class ReservationRestController {

    @Autowired
    private ReservationService reservationService;
    @Autowired
    private EmpruntService empruntService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private LivreService livreService;
    @Autowired
    private PenaliteService penaliteService;


    @Autowired
    private UserService userService;
    @GetMapping
    public ResponseEntity<Page<Reservation>> getAllReservations(Pageable pageable) {
        Page<Reservation> reservations = reservationService.findAll(pageable);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/{cin}")
    public ResponseEntity<Page<Reservation>> getReservationsByCin(@PathVariable String cin, Pageable pageable) {
        Page<Reservation> reservations = reservationService.getAllReservationByCin(cin, pageable);
        if (reservations.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reservations);
    }

    @DeleteMapping("/delete/{reservationId}")
    public ResponseEntity<?> deleteReservation(@PathVariable Long reservationId) {
        boolean isDeleted = reservationService.deleteById(reservationId);
        if (isDeleted) {
            return ResponseEntity.ok("La réservation avec l'ID " + reservationId + " a été supprimée avec succès.");
        } else {
        	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La réservation avec l'ID " + reservationId + " n'existe pas.");
        }
    }
    
    @PostMapping("/add")
    public ResponseEntity<?> addReservation(@RequestBody ReservationRequest reservationRequest) {

        if (penaliteService.hasUnpaidPenalties(reservationRequest.getUserCin())) {
            return ResponseEntity.badRequest().body("L'utilisateur a des pénalités impayées. Vous ne pouvez pas ajouter une réservation pour lui.");
        }

        Livre livre = livreService.findByIsbn(reservationRequest.getLivreIsbn());
        User user = userService.findByCin(reservationRequest.getUserCin());

        if (user != null && livre != null) {
          
            if (livre.getAvailableCopies() > 0) {
                return ResponseEntity.badRequest().body("Le livre est actuellement disponible. Vous ne pouvez pas le réserver.");
            }

      
            LocalDateTime dateReservation;
            if (reservationRequest.getDateReservation() == null) {
                
                dateReservation = LocalDateTime.now();
            } else {
                
                dateReservation = reservationRequest.getDateReservation();
            }

            Reservation reservation = new Reservation();
            reservation.setLivre(livre);
            reservation.setUser(user);
            reservation.setDate(dateReservation); 

           
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
                CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
                if (userDetails.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
                    reservation.setStatut(StatutReservation.VALIDE); 
                } else {
                    reservation.setStatut(StatutReservation.EN_ATTENTE); 
                }
            }

            reservationService.save(reservation);

            String message = "Votre réservation pour le livre " + livre.getTitle() + " a été enregistrée et est " + (reservation.getStatut().equals(StatutReservation.VALIDE) ? "maintenant valide." : "en attente de validation.");
            notificationService.createNotification(user, message);

            return ResponseEntity.ok("Réservation ajoutée avec succès.");
        } else {
            return ResponseEntity.badRequest().body("L'utilisateur ou le livre n'existe pas.");
        }
    }
    @PostMapping("/validate/{id}")
    public ResponseEntity<?> validateReservation(@PathVariable Long id) {
        
        Reservation reservation = reservationService.findById(id);
        if (reservation == null) {
        	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La réservation avec l'ID " + id + " n'existe pas.");
        }

        if (!reservation.getStatut().equals(StatutReservation.EN_ATTENTE)) {
            return ResponseEntity.badRequest().body("La réservation avec l'ID " + id + " n'est pas en attente de validation.");
        }

        reservation.setStatut(StatutReservation.VALIDE);
      
        reservationService.save(reservation);

        String message = "Votre réservation du livre " + reservation.getLivre().getTitle() + " a été validée.";
        notificationService.createNotification(reservation.getUser(), message);

        return ResponseEntity.ok("La réservation a été validée avec succès.");
    }

    @PostMapping("/cancel/{id}")
    public ResponseEntity<?> cancelReservation(@PathVariable Long id) {
       
        Reservation reservation = reservationService.findById(id);
        if (reservation == null) {
        	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La réservation avec l'ID " + id + " n'existe pas.");

        }

        reservation.setStatut(StatutReservation.ANNULE);
      
        reservationService.save(reservation);

        String message = "Votre réservation du livre " + reservation.getLivre().getTitle() + " a été annulée.";
        notificationService.createNotification(reservation.getUser(), message);

        return ResponseEntity.ok("La réservation a été annulée avec succès.");
    }
    
    @PostMapping("/retirer")
    public ResponseEntity<?> retirerReservation(@RequestBody EmpruntRequest empruntRequest) {
    
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Vous devez être connecté pour retirer une réservation.");
        }

        if (empruntRequest.getDateRetourPrevu().before(empruntRequest.getDateEmprunt())) {
            return ResponseEntity.badRequest().body("La date de retour prévue ne peut pas être antérieure à la date d'emprunt.");
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(empruntRequest.getDateEmprunt());
        cal.add(Calendar.DATE, 14); 
        Date maxDateRetour = cal.getTime();
        if (empruntRequest.getDateRetourPrevu().after(maxDateRetour)) {
            return ResponseEntity.badRequest().body("La date de retour prévue ne peut pas dépasser la date d'emprunt de plus de 14 jours.");
        }

        Reservation reservation = reservationService.findByLivreIsbnAndUserCin(empruntRequest.getIsbn(), empruntRequest.getCin());
        if (reservation == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La réservation pour le livre avec l'ISBN " + empruntRequest.getIsbn() + " et le CIN " + empruntRequest.getCin() + " n'existe pas.");
        }

        if (!reservation.getStatut().equals(StatutReservation.VALIDE)) {
            return ResponseEntity.badRequest().body("La réservation n'est pas valide.");
        }

        reservation.setStatut(StatutReservation.RETIRE);
  
        reservationService.save(reservation);

        Emprunt emprunt = new Emprunt();
        emprunt.setDateEmprunt(empruntRequest.getDateEmprunt());
        emprunt.setDateRetourPrevu(empruntRequest.getDateRetourPrevu());
        emprunt.setUser(reservation.getUser()); 
        emprunt.setLivre(reservation.getLivre());
        emprunt.setStatut(StatutEmprunt.EMPRUNTE);
      
        empruntService.save(emprunt);

        Livre livre = livreService.findByIsbn(empruntRequest.getIsbn());
        if (livre != null) {
            int newAvailable = livre.getAvailableCopies() - empruntRequest.getNombreExemplaires();
            if (newAvailable < 0) {
                return ResponseEntity.badRequest().body("Le nombre d'exemplaires demandés dépasse le nombre disponible.");
            }
            livre.setAvailableCopies(newAvailable);
            livreService.save(livre);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Le livre avec l'ISBN " + empruntRequest.getIsbn() + " n'existe pas.");
        }
 
        String message = "La réservation pour le livre " + reservation.getLivre().getTitle() + " a été retirée et un nouvel emprunt a été créé.";
        notificationService.createNotification(reservation.getUser(), message);


        return ResponseEntity.ok("La réservation a été retirée, un nouvel emprunt a été créé.");
    }
    
}
