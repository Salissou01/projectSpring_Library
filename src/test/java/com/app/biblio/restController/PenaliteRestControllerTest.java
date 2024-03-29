package com.app.biblio.restController;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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

import com.app.biblio.bean.Emprunt;
import com.app.biblio.bean.Livre;
import com.app.biblio.bean.Penalite;
import com.app.biblio.bean.Retour;
import com.app.biblio.bean.StatutPenalite;
import com.app.biblio.bean.request.PaiementRequest;
import com.app.biblio.service.NotificationService;
import com.app.biblio.service.PenaliteService;
import com.app.biblio.service.RetourService;

@ExtendWith(MockitoExtension.class)
public class PenaliteRestControllerTest {

    @Mock
    private PenaliteService penaliteService;

    @Mock
    private RetourService retourService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private PenaliteRestController penaliteRestController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllPenalites() {
        // Setup
        List<Penalite> penalites = new ArrayList<>();
        Page<Penalite> penalitePage = new PageImpl<>(penalites);
        when(penaliteService.getAllPenalite(Pageable.unpaged())).thenReturn(penalitePage);

        // Execute
        ResponseEntity<Page<Penalite>> response = penaliteRestController.getAllPenalites(Pageable.unpaged());

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(penalitePage, response.getBody());
    }
    @Test
    public void testGetPenalitesByCin() {
        // Setup
        String cin = "123456789";
        Penalite penalite = new Penalite();
    
        List<Penalite> penalites = Arrays.asList(penalite);
        Page<Penalite> penalitePage = new PageImpl<>(penalites);
        when(penaliteService.getAllPenaliteByCin(cin, Pageable.unpaged())).thenReturn(penalitePage);

        // Execute
        ResponseEntity<Page<Penalite>> response = penaliteRestController.getPenalitesByCin(cin, Pageable.unpaged());

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(penalitePage, response.getBody());
    }


    @Test
    public void testProcessPaymentConfirmation() {
        // Setup
        Long penaliteId = 1L;
        Penalite penalite = new Penalite();
        penalite.setId(penaliteId);
        penalite.setStatutPenalite(StatutPenalite.NON_PAYEE);
        BigDecimal montant = BigDecimal.valueOf(50.00);
        penalite.setMontant(montant);

        // Configurez l'objet Retour
        Retour retour = new Retour();
        retour.setId(2L); // Assurez-vous de configurer un identifiant pour Retour
        // Configurez l'objet Emprunt
        Emprunt emprunt = new Emprunt();
        // Configurez l'objet Livre
        Livre livre = new Livre();
        livre.setTitle("Titre du livre"); // Assurez-vous de configurer un titre pour Livre
        emprunt.setLivre(livre); // Associez Livre à Emprunt
        retour.setEmprunt(emprunt);

        // Associez Retour à Penalite
        penalite.setRetour(retour); // Utilisez la méthode appropriée pour associer Retour à Penalite

        when(penaliteService.findById(penaliteId)).thenReturn(penalite);
        when(retourService.findByIdWithAssociations(any(Long.class))).thenReturn(retour); // Utilisez Mockito.any() pour n'importe quel argument

        PaiementRequest paiementRequest = new PaiementRequest();
        paiementRequest.setMontantPaiement(BigDecimal.valueOf(50.00));

        // Execute
        ResponseEntity<?> response = penaliteRestController.processPaymentConfirmation(penaliteId, paiementRequest);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("La pénalité a été payée avec succès. Une notification a été envoyée à l'utilisateur.", response.getBody());

        // Assert that the penalite status is changed
        assertEquals(StatutPenalite.PAYEE, penalite.getStatutPenalite());
    }



}
