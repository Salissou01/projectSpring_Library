package com.app.biblio.restController;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.biblio.bean.CustomUserDetails;
import com.app.biblio.bean.Emprunt;
import com.app.biblio.bean.EmpruntDetails;
import com.app.biblio.bean.Livre;

import com.app.biblio.bean.StatutEmprunt;
import com.app.biblio.bean.User;
import com.app.biblio.bean.request.EmpruntRequest;
import com.app.biblio.service.EmpruntService;
import com.app.biblio.service.LivreService;
import com.app.biblio.service.NotificationService;
import com.app.biblio.service.PenaliteService;
import com.app.biblio.service.UserService;

@RestController
@RequestMapping("/api")
public class EmpruntRestController {
	   @Autowired
	    private EmpruntService empruntService;
	   @Autowired
	    private LivreService livreService;
	   @Autowired
	    private UserService userService;
	   @Autowired
	    private NotificationService notificationService;
	   @Autowired
	    private PenaliteService penaliteService;
	@GetMapping("/emprunts")
    public ResponseEntity<List<Emprunt>> getAllEmprunts() {
        List<Emprunt> emprunts = empruntService.findAll();
        return new ResponseEntity<>(emprunts, HttpStatus.OK);
    }

    @GetMapping("/emprunts/{id}")
    public ResponseEntity<Emprunt> getEmpruntById(@PathVariable Long id) {
        Emprunt emprunt = empruntService.findById(id);
        return new ResponseEntity<>(emprunt, HttpStatus.OK);
    }

    @GetMapping("/emprunts/user/{cin}")
    public ResponseEntity<Page<Emprunt>> getEmpruntsByCin(@PathVariable String cin,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Emprunt> empruntsPage = empruntService.getAllEmpruntByCin(cin, pageable);
        return new ResponseEntity<>(empruntsPage, HttpStatus.OK);
    }

    @PostMapping("/emprunts/add")
    public ResponseEntity<?> createEmprunt(@RequestBody EmpruntRequest empruntRequest) {
        User user = userService.findByCin(empruntRequest.getCin());
        if (user == null) {
            return new ResponseEntity<>("L'utilisateur avec le CIN spécifié n'existe pas.", HttpStatus.BAD_REQUEST);
        }

        // Vérifier si l'utilisateur a des pénalités non payées
        if (penaliteService.hasUnpaidPenalties(user.getCIN())) {
            return new ResponseEntity<>("L'utilisateur a des pénalités impayées. Vous ne pouvez pas ajouter un emprunt pour lui.", HttpStatus.BAD_REQUEST);
        }

        Livre livre = livreService.findByIsbn(empruntRequest.getIsbn());
        if (livre == null) {
            return new ResponseEntity<>("Le livre avec l'ISBN spécifié n'existe pas.", HttpStatus.BAD_REQUEST);
        }

        if (livre.getAvailableCopies() <= 0) {
            return new ResponseEntity<>("Il n'y a plus de copies disponibles pour le livre " + livre.getTitle() + ".", HttpStatus.BAD_REQUEST);
        }
        if (livre.getAvailableCopies() < empruntRequest.getNombreExemplaires()) {
            return new ResponseEntity<>("Il n'y a pas assez de copies disponibles pour le livre " + livre.getTitle() + ".", HttpStatus.BAD_REQUEST);
        }
        
        if (empruntRequest.getDateRetourPrevu().before(empruntRequest.getDateEmprunt())) {
            return new ResponseEntity<>("La date de retour ne peut pas être antérieure à la date d'emprunt.", HttpStatus.BAD_REQUEST);
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(empruntRequest.getDateEmprunt());
        cal.add(Calendar.DATE, 14);
        Date maxDateRetour = cal.getTime();
        if (empruntRequest.getDateRetourPrevu().after(maxDateRetour)) {
            return new ResponseEntity<>("La date de retour ne peut pas dépasser la date d'emprunt de plus de 14 jours.", HttpStatus.BAD_REQUEST);
        }

    
        CustomUserDetails authenticatedUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        StatutEmprunt statut = authenticatedUser.getAuthorities().stream()
                                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN")) ? StatutEmprunt.EMPRUNTE : StatutEmprunt.EN_ATTENTE;
        Emprunt emprunt = new Emprunt();
        emprunt.setDateEmprunt(empruntRequest.getDateEmprunt());
        emprunt.setDateRetourPrevu(empruntRequest.getDateRetourPrevu());
        emprunt.setUser(user);
        emprunt.setLivre(livre);
        emprunt.setStatut(statut); 
        emprunt.setNombreExemplaires(empruntRequest.getNombreExemplaires()); 
        Emprunt savedEmprunt = empruntService.save(emprunt);
       
        if (statut == StatutEmprunt.EMPRUNTE) {
            livre.setAvailableCopies(livre.getAvailableCopies() - empruntRequest.getNombreExemplaires());
            livreService.save(livre); 
        }

        
        String message = statut == StatutEmprunt.EMPRUNTE ? "Votre demande d'emprunt pour le livre " + livre.getTitle() + " a été reçue, validée et est maintenant EMPRUNTE ." : "Votre demande d'emprunt pour le livre " + livre.getTitle() + " a été reçue et est en attente de validation.";
        notificationService.createNotification(user, message);
        
        return new ResponseEntity<>(savedEmprunt, HttpStatus.CREATED);
    }

    @PutMapping("/emprunts/edit/{id}")
    public ResponseEntity<?> updateEmprunt(@PathVariable Long id, @RequestBody Emprunt empruntDetails) {
        Emprunt emprunt = empruntService.findById(id);
        if (emprunt == null) {
            return new ResponseEntity<>("L'emprunt avec l'ID spécifié n'existe pas.", HttpStatus.NOT_FOUND);
        }

        if (!emprunt.getStatut().equals(StatutEmprunt.EMPRUNTE)) {
            return new ResponseEntity<>("Seuls les emprunts avec le statut EMPRUNTE peuvent être modifiés.", HttpStatus.BAD_REQUEST);
        }

        if (empruntDetails.getDateRetourPrevu().before(empruntDetails.getDateEmprunt())) {
            return new ResponseEntity<>("La date de retour ne peut pas être antérieure à la date d'emprunt.", HttpStatus.BAD_REQUEST);
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(empruntDetails.getDateEmprunt());
        cal.add(Calendar.DATE, 14);
        Date maxDateRetour = cal.getTime();
        if (empruntDetails.getDateRetourPrevu().after(maxDateRetour)) {
            return new ResponseEntity<>("La date de retour ne peut pas dépasser la date d'emprunt de plus de 14 jours.", HttpStatus.BAD_REQUEST);
        }

        emprunt.setDateEmprunt(empruntDetails.getDateEmprunt());
        emprunt.setDateRetourPrevu(empruntDetails.getDateRetourPrevu());

        Emprunt updatedEmprunt = empruntService.save(emprunt);

        String message = "La date d'emprunt et la date de retour de votre emprunt pour le livre " + emprunt.getLivre().getTitle() + " ont été mises à jour.";
        notificationService.createNotification(emprunt.getUser(), message);

        return new ResponseEntity<>(updatedEmprunt, HttpStatus.OK);
    }



    @DeleteMapping("/emprunts/delete/{id}")
    public ResponseEntity<HttpStatus> deleteEmprunt(@PathVariable Long id) {
        Emprunt emprunt = empruntService.findById(id);
        if (emprunt == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        empruntService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/emprunts/{id}/validate")
    public ResponseEntity<?> validateEmprunt(@PathVariable Long id) {
        Emprunt emprunt = empruntService.findById(id);
        if (emprunt == null) {
            return new ResponseEntity<>("L'emprunt avec l'ID spécifié n'existe pas.", HttpStatus.NOT_FOUND);
        }

        if (!emprunt.getStatut().equals(StatutEmprunt.EN_ATTENTE)) {
            return new ResponseEntity<>("Le statut de l'emprunt n'est pas EN_ATTENTE", HttpStatus.BAD_REQUEST);
        }

        Livre livre = emprunt.getLivre();
        int availableCopies = livre.getAvailableCopies();
        int requestedCopies = emprunt.getNombreExemplaires();

        if (availableCopies == 0) {
            return new ResponseEntity<>("Le livre " + livre.getTitle() + " n'est pas disponible pour l'emprunt.", HttpStatus.CONFLICT);
        }

        if (requestedCopies > availableCopies) {
            return new ResponseEntity<>("Il n'y a pas assez de copies disponibles pour le livre " + livre.getTitle() + ".", HttpStatus.CONFLICT);
        }

        livre.setAvailableCopies(availableCopies - requestedCopies);
        livreService.save(livre);

        emprunt.setStatut(StatutEmprunt.EMPRUNTE);
        Emprunt updatedEmprunt = empruntService.save(emprunt);

        notificationService.createNotification(emprunt.getUser(), "Votre emprunt pour le livre " + livre.getTitle() + " a été validé.");
        return new ResponseEntity<>(updatedEmprunt, HttpStatus.OK);
    }


    @PutMapping("/emprunts/{id}/reject")
    public ResponseEntity<?> rejectEmprunt(@PathVariable Long id) {
        Emprunt emprunt = empruntService.findById(id);
        if (emprunt == null) {
        	return new ResponseEntity<>("L'emprunt avec l'ID spécifié n'existe pas.", HttpStatus.NOT_FOUND);
        }

        if (!emprunt.getStatut().equals(StatutEmprunt.EN_ATTENTE)) {
          
        	return new ResponseEntity<>("Le statut de l'emprunt n'est pas EN_ATTENTE",HttpStatus.BAD_REQUEST);
        }

        emprunt.setStatut(StatutEmprunt.REJETE);
        Emprunt updatedEmprunt = empruntService.save(emprunt);
    
        notificationService.createNotification(emprunt.getUser(), "Votre demande d'emprunt pour le livre " + emprunt.getLivre().getTitle() + " a été rejetée.");
        return new ResponseEntity<>(updatedEmprunt, HttpStatus.OK);
    }
    @GetMapping("/emprunts/details/{id}")
    public ResponseEntity<?> getEmpruntDetails(@PathVariable("id") Long empruntId) {
        Emprunt emprunt = empruntService.findById(empruntId);
        if (emprunt != null) {
            EmpruntDetails details = new EmpruntDetails(emprunt);
            return ResponseEntity.ok(details);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
