package com.app.biblio.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.app.biblio.bean.Emprunt;
import com.app.biblio.bean.Livre;
import com.app.biblio.bean.Retour;
import com.app.biblio.bean.StatutRetour;
import com.app.biblio.repository.EmpruntRepository;
import com.app.biblio.repository.LivreRepository;
import com.app.biblio.repository.RetourRepository;

@SpringBootTest
@ActiveProfiles("test")
public class RecommendationServiceImplIntegrationTest {

    @Autowired
    private RecommendationServiceImpl recommendationService;

    @Autowired
    private RetourRepository retourRepository;

    @Autowired
    private LivreRepository livreRepository;

    @Autowired
    private EmpruntRepository empruntRepository;

    @Test
    public void testRecommendBooksBasedOnRatings() {
        // Setup
        // Créer des livres et des retours pour tester
        Livre livre1 = new Livre();
        livre1.setTitle("Livre 1");
        livre1 = livreRepository.save(livre1); // Sauvegarder le livre1 dans la base de données

        Livre livre2 = new Livre();
        livre2.setTitle("Livre 2");
        livre2 = livreRepository.save(livre2); // Sauvegarder le livre2 dans la base de données

        // Créer des emprunts pour les livres
        Emprunt emprunt1 = new Emprunt();
        emprunt1.setLivre(livre1);
        emprunt1 = empruntRepository.save(emprunt1); // Sauvegarder l'emprunt1 dans la base de données

        Emprunt emprunt2 = new Emprunt();
        emprunt2.setLivre(livre2);
        emprunt2 = empruntRepository.save(emprunt2); // Sauvegarder l'emprunt2 dans la base de données

        // Créer des retours pour les livres avec des notes et statutRetour
        Retour retour1 = new Retour();
        retour1.setNote(5);
        retour1.setEmprunt(emprunt1);
        retour1.setStatutRetour(StatutRetour.A_L_HEURE); // Initialiser statutRetour avec une valeur valide
        retour1 = retourRepository.save(retour1); // Sauvegarder retour1 dans la base de données

        Retour retour2 = new Retour();
        retour2.setNote(3);
        retour2.setEmprunt(emprunt2);
        retour2.setStatutRetour(StatutRetour.A_L_HEURE); // Initialiser statutRetour avec une valeur valide
        retour2 = retourRepository.save(retour2); // Sauvegarder retour2 dans la base de données

        // Execute
        Map<Livre, Double> recommendedBooks = recommendationService.recommendBooksBasedOnRatings();

        // Verify
        assertNotNull(recommendedBooks);
        assertEquals(1, recommendedBooks.size()); // Seul le livre1 devrait être recommandé
       
    }
}
