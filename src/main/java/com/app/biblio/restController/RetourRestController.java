package com.app.biblio.restController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.biblio.bean.CustomUserDetails;
import com.app.biblio.bean.Emprunt;
import com.app.biblio.bean.Livre;
import com.app.biblio.bean.Penalite;
import com.app.biblio.bean.Reservation;
import com.app.biblio.bean.Retour;
import com.app.biblio.bean.StatutEmprunt;
import com.app.biblio.bean.StatutPenalite;
import com.app.biblio.bean.StatutRetour;
import com.app.biblio.bean.User;
import com.app.biblio.bean.request.PenaliteRequest;
import com.app.biblio.bean.request.ReturnRequest;
import com.app.biblio.service.EmpruntService;
import com.app.biblio.service.LivreService;
import com.app.biblio.service.NotificationService;
import com.app.biblio.service.PenaliteService;
import com.app.biblio.service.ReservationService;
import com.app.biblio.service.RetourService;


@RestController
@RequestMapping("/api/retours")
public class RetourRestController {

	@Autowired
    private EmpruntService empruntService;

    @Autowired
    private LivreService livreService;
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private PenaliteService penaliteService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private RetourService retourService;


	    @GetMapping("/users/{cin}")
	    public ResponseEntity<Page<Retour>> getAllRetoursByUserCin(@PathVariable String cin, Pageable pageable) {
	        Page<Retour> retours = retourService.getAllRetourByCin(cin, pageable);
	        return ResponseEntity.ok(retours);
	    }

	    @GetMapping
	    public ResponseEntity<Page<Retour>> getAllRetours(Pageable pageable) {
	        Page<Retour> retours = retourService.getAllRetour(pageable);
	        return ResponseEntity.ok(retours);
	    }

	    @GetMapping("/{id}")
	    public ResponseEntity<Retour> getRetourById(@PathVariable Long id) {
	        Retour retour = retourService.findById(id);
	        if (retour == null) {
	            return ResponseEntity.notFound().build();
	        }
	        return ResponseEntity.ok(retour);
	    }

	    @PostMapping("/{id}")
	    public ResponseEntity<?> handleReturn(@PathVariable("id") Long empruntId,
	                                          @RequestBody ReturnRequest returnRequest) {
	        Emprunt emprunt = empruntService.findById(empruntId);
	        if (emprunt == null) {
	            return new ResponseEntity<>("EmpruntID inexistant", HttpStatus.NOT_FOUND);
	        }

	        if (!emprunt.getStatut().equals(StatutEmprunt.EMPRUNTE)) {
	            return new ResponseEntity<>("L'emprunt doit avoir le statut 'emprunté' pour être retourné", HttpStatus.BAD_REQUEST);
	        }

	        if (returnRequest.getNote() < 1 || returnRequest.getNote() > 5) {
	            return new ResponseEntity<>("La note doit être comprise entre 1 et 5", HttpStatus.BAD_REQUEST);
	        }

	        Date dateRetourEffectif;
	        if (returnRequest.getDateRetourEffectif() == null || returnRequest.getDateRetourEffectif().isEmpty()) {
	            dateRetourEffectif = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
	        } else {
	            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	            LocalDate localDate = LocalDate.parse(returnRequest.getDateRetourEffectif(), formatter);
	            dateRetourEffectif = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	        }

	        if (dateRetourEffectif.before(emprunt.getDateEmprunt())) {
	            return new ResponseEntity<>("La date de retour effectif ne peut pas être antérieure à la date d'emprunt", HttpStatus.BAD_REQUEST);
	        }

	        CustomUserDetails authenticatedUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	        boolean isAdmin = authenticatedUser.getAuthorities().stream()
	                            .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

	        Retour retour = new Retour();
	        retour.setEmprunt(emprunt);
	        retour.setDateRetourEffectif(dateRetourEffectif);
	        retour.setNote(returnRequest.getNote());

	        if (isAdmin && dateRetourEffectif.before(emprunt.getDateRetourPrevu())) {
	            retour.setStatutRetour(StatutRetour.A_L_HEURE);
	        } else {
	            retour.setStatutRetour(StatutRetour.EN_ATTENTE);
	        }
	        retourService.save(retour);

	        emprunt.setStatut(isAdmin ? StatutEmprunt.RETOURNE : StatutEmprunt.EN_ATTENTE);
	        empruntService.save(emprunt);

	        String message = isAdmin ? "Le livre " + emprunt.getLivre().getTitle() + " a été retourné." : "Votre retour pour le livre " + emprunt.getLivre().getTitle() + " a été enregistré. Il est en attente de validation.";
	        notificationService.createNotification(emprunt.getUser(), message);

	        if (retour.getStatutRetour().equals(StatutRetour.A_L_HEURE)) {
	            Livre livre = emprunt.getLivre();
	            List<Reservation> reservations = reservationService.findByLivreOrderByDateAsc(livre.getId());
	            if (!reservations.isEmpty()) {
	                for (Reservation reservation : reservations) {
	                    User user = reservation.getUser();
	                    String messageReservation = "Le livre " + livre.getTitle() + " est maintenant disponible pour retrait.";
	                    if (!notificationService.hasUserBeenNotifiedForBook(user, livre)) {
	                        notificationService.createNotification(user, messageReservation);
	                    }
	                }
	            }

	            livre.setAvailableCopies(livre.getAvailableCopies() + emprunt.getNombreExemplaires());
	            livreService.save(livre);
	        }

	        return new ResponseEntity<>(retour, HttpStatus.CREATED);
	    }

	    @PutMapping("/validate/{id}")
	    public ResponseEntity<?> validateReturn(@PathVariable Long id) {
	        Retour retour = retourService.findById(id);
	        if (retour == null) {
	            return new ResponseEntity<>("Retour avec l'ID spécifié n'existe pas.", HttpStatus.NOT_FOUND);
	        }

	        if (!retour.getStatutRetour().equals(StatutRetour.EN_ATTENTE)) {
	            return new ResponseEntity<>("Le retour doit avoir le statut 'EN_ATTENTE' pour être validé.", HttpStatus.BAD_REQUEST);
	        }

	        if (retour.getDateRetourEffectif() == null || retour.getEmprunt() == null || retour.getEmprunt().getDateRetourPrevu() == null) {
	            return new ResponseEntity<>("La date de retour effectif ou la date de retour prévue est manquante.", HttpStatus.BAD_REQUEST);
	        }

	        if (retour.getDateRetourEffectif().before(retour.getEmprunt().getDateRetourPrevu())) {
	            retour.setStatutRetour(StatutRetour.A_L_HEURE);
	            retourService.save(retour);

	            
	            String message = "Votre retour pour le livre " + retour.getEmprunt().getLivre().getTitle() + " a été validé.";
	            notificationService.createNotification(retour.getEmprunt().getUser(), message);

	           
	            Livre livre = retour.getEmprunt().getLivre();
	            List<Reservation> reservations = reservationService.findByLivreOrderByDateAsc(livre.getId());
	            if (!reservations.isEmpty()) {
	                for (Reservation reservation : reservations) {
	                    User user = reservation.getUser();
	                    String messageReservation = "Le livre " + livre.getTitle() + " est maintenant disponible pour retrait.";
	                    if (!notificationService.hasUserBeenNotifiedForBook(user, livre)) {
	                        notificationService.createNotification(user, messageReservation);
	                    }
	                }
	            }

	            livre.setAvailableCopies(livre.getAvailableCopies() + retour.getEmprunt().getNombreExemplaires());
	            livreService.save(livre);

	            return new ResponseEntity<>(retour, HttpStatus.OK);
	        } else {
	            return new ResponseEntity<>("La date de retour effectif doit être antérieure à la date de retour prévue pour valider le retour.", HttpStatus.BAD_REQUEST);
	        }
	    }

	    @PutMapping("/penalize/{id}")
	    public ResponseEntity<?> penalizeReturn(@PathVariable Long id,
	    		@RequestBody PenaliteRequest penaliteRequest) {
	    	BigDecimal penaliteParJour = penaliteRequest.getPenaliteParJour();
	        Retour retour = retourService.findById(id);
	        if (retour == null) {
	            return new ResponseEntity<>("Retour avec l'ID spécifié n'existe pas.", HttpStatus.NOT_FOUND);
	        }

	        if (!retour.getStatutRetour().equals(StatutRetour.EN_ATTENTE)) {
	            return new ResponseEntity<>("Le retour doit avoir le statut 'EN_ATTENTE' pour être pénalisé.", HttpStatus.BAD_REQUEST);
	        }

	        if (retour.getDateRetourEffectif().after(retour.getEmprunt().getDateRetourPrevu())) {
	        
	            LocalDate dateRetourEffectif = retour.getDateRetourEffectif().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	            LocalDate dateRetourPrevu = retour.getEmprunt().getDateRetourPrevu().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	            long joursDeRetard = ChronoUnit.DAYS.between(dateRetourPrevu, dateRetourEffectif);

	            
	            BigDecimal montantPenalite = penaliteParJour.multiply(BigDecimal.valueOf(joursDeRetard));
	            Penalite penalite = new Penalite(retour, montantPenalite, StatutPenalite.NON_PAYEE);
	            penaliteService.save(penalite);
	            retour.setStatutRetour(StatutRetour.EN_RETARD);
	            retourService.save(retour);

	            String message = "Vous avez une pénalité de " + montantPenalite + " pour le retour retardé du livre " + retour.getEmprunt().getLivre().getTitle() + ".";
	            notificationService.createNotification(retour.getEmprunt().getUser(), message);

	            Livre livre = retour.getEmprunt().getLivre();
	            List<Reservation> reservations = reservationService.findByLivreOrderByDateAsc(livre.getId());
	            if (!reservations.isEmpty()) {
	                for (Reservation reservation : reservations) {
	                    User user = reservation.getUser();
	                    String messageReservation = "Le livre " + livre.getTitle() + " est maintenant disponible pour retrait.";
	                    if (!notificationService.hasUserBeenNotifiedForBook(user, livre)) {
	                        notificationService.createNotification(user, messageReservation);
	                    }
	                }
	            }

	            livre.setAvailableCopies(livre.getAvailableCopies() + retour.getEmprunt().getNombreExemplaires());
	            livreService.save(livre);

	            return new ResponseEntity<>(retour, HttpStatus.OK);
	        } else {
	            return new ResponseEntity<>("La date de retour effectif doit être supérieure à la date de retour prévue pour pénaliser le retour.", HttpStatus.BAD_REQUEST);
	        }
	    }
	    
}
