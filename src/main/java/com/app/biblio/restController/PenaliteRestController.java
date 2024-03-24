package com.app.biblio.restController;
import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.biblio.bean.Penalite;
import com.app.biblio.bean.Retour;
import com.app.biblio.bean.StatutPenalite;
import com.app.biblio.bean.User;
import com.app.biblio.bean.request.PaiementRequest;
import com.app.biblio.service.NotificationService;
import com.app.biblio.service.PenaliteService;
import com.app.biblio.service.RetourService;

@RestController
@RequestMapping("/api/penalites")
public class PenaliteRestController {

    @Autowired
    private PenaliteService penaliteService;

    @Autowired
    private RetourService retourService;

    @Autowired
    private NotificationService notificationService;
    @GetMapping
    public ResponseEntity<Page<Penalite>> getAllPenalites(Pageable pageable) {
        Page<Penalite> penalites = penaliteService.getAllPenalite(pageable);
        return ResponseEntity.ok(penalites);
    }

    @GetMapping("/{cin}")
    public ResponseEntity<Page<Penalite>> getPenalitesByCin(@PathVariable String cin, Pageable pageable) {
        Page<Penalite> penalites = penaliteService.getAllPenaliteByCin(cin, pageable);
        if (penalites.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(penalites);
    }

    @PostMapping("/payer/{id}")
    public ResponseEntity<?> processPaymentConfirmation(@PathVariable Long id, @RequestBody PaiementRequest paiementRequest) {
     
        Penalite penalite = penaliteService.findById(id);
        if (penalite == null) {
            return ResponseEntity.badRequest().body("La pénalité n'existe pas .");
        }
        if (!penalite.getStatutPenalite().equals(StatutPenalite.NON_PAYEE)) {
        	return ResponseEntity.badRequest().body("La pénalité a déjà été payée.");
        	
        }
        BigDecimal montantPaiement = paiementRequest.getMontantPaiement();
        if (montantPaiement.compareTo(penalite.getMontant()) < 0) {
            return ResponseEntity.badRequest().body("Le montant du paiement ne doit pas être inférieur au montant de la pénalité.");
        }

        penalite.setStatutPenalite(StatutPenalite.PAYEE);
        penaliteService.save(penalite);

        Retour retour = retourService.findByIdWithAssociations(penalite.getRetour().getId());

        User penaliteUser = retour.getEmprunt().getUser();

        String penaliteMessage = "Votre pénalité pour le livre " + retour.getEmprunt().getLivre().getTitle() + " de " + penalite.getMontant() + " a été payée.";

        notificationService.createNotification(penaliteUser, penaliteMessage);

        return ResponseEntity.ok("La pénalité a été payée avec succès. Une notification a été envoyée à l'utilisateur.");
    }
}
