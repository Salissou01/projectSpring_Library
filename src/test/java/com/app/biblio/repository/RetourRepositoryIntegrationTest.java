package com.app.biblio.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.app.biblio.bean.Emprunt;
import com.app.biblio.bean.Livre;
import com.app.biblio.bean.Retour;
import com.app.biblio.bean.StatutRetour;
import com.app.biblio.bean.User;

@SpringBootTest
@ActiveProfiles("test")
public class RetourRepositoryIntegrationTest {

    @Autowired
    private RetourRepository retourRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmpruntRepository empruntRepository;
    @Autowired
    private LivreRepository livreRepository;

    @BeforeEach
    public void setUp() {
    	
        // Insérer un utilisateur avec l'identifiant 1 dans la base de données de test
        User user = new User();
        user.setId(1L);
        userRepository.save(user);

        // Insérer un livre avec l'identifiant 1 dans la base de données de test
        Livre livre = new Livre();
        livre.setId(1L);
        livreRepository.save(livre);
    }

    @Test
    public void testSaveRetour() {
        // Préparation
        User user = new User();
        user.setId(1L);

        Livre livre = new Livre();
        livre.setId(1L);

        Emprunt emprunt = new Emprunt();
        emprunt.setUser(user);
        emprunt.setLivre(livre);
        // Enregistrer l'emprunt avant de créer le retour
        Emprunt savedEmprunt = empruntRepository.save(emprunt);
        Retour retour = new Retour();
        retour.setEmprunt(savedEmprunt);
        retour.setStatutRetour(StatutRetour.EN_RETARD);

        // Exécution
        Retour savedRetour = retourRepository.save(retour);

        // Vérification
        assertNotNull(savedRetour.getId());
    }

  


}
