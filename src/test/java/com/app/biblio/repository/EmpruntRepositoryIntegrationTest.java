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
import com.app.biblio.bean.StatutEmprunt;
import com.app.biblio.bean.User;

@SpringBootTest
@ActiveProfiles("test")
public class EmpruntRepositoryIntegrationTest {

    @Autowired
    private EmpruntRepository empruntRepository;
    @Autowired
    private UserRepository userRepository;
    @BeforeEach
    public void setUp() {
      
        User user = new User();
        user.setId(1L);
        userRepository.save(user);
    }
   
    @Test
    public void testCountByStatut() {
        // Préparation
        StatutEmprunt statut = StatutEmprunt.EN_ATTENTE;
        Emprunt emprunt1 = new Emprunt();
        emprunt1.setStatut(statut);
        Emprunt emprunt2 = new Emprunt();
        emprunt2.setStatut(statut);

        // Exécution
        empruntRepository.save(emprunt1);
        empruntRepository.save(emprunt2);
        long count = empruntRepository.countByStatut(statut);

        // Vérification
        assertEquals(2, count);
    }
    @Test
    public void testFindByStatut() {
        // Préparation
        StatutEmprunt statut = StatutEmprunt.REJETE;
        Emprunt emprunt1 = new Emprunt();
        emprunt1.setStatut(statut);
        Emprunt emprunt2 = new Emprunt();
        emprunt2.setStatut(statut);

        // Exécution
        empruntRepository.save(emprunt1);
        empruntRepository.save(emprunt2);
        List<Emprunt> emprunts = empruntRepository.findByStatut(statut);

        // Vérification
        assertNotNull(emprunts);
        assertEquals(2, emprunts.size());
        assertEquals(statut, emprunts.get(0).getStatut());
        assertEquals(statut, emprunts.get(1).getStatut());
    }

}
