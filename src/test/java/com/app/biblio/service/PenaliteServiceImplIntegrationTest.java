package com.app.biblio.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.app.biblio.bean.Emprunt;
import com.app.biblio.bean.Penalite;
import com.app.biblio.bean.Retour;
import com.app.biblio.bean.StatutPenalite;
import com.app.biblio.bean.StatutRetour;
import com.app.biblio.repository.EmpruntRepository;
import com.app.biblio.repository.PenaliteRepository;
import com.app.biblio.repository.RetourRepository;

@SpringBootTest
@ActiveProfiles("test")
public class PenaliteServiceImplIntegrationTest {

    @Autowired
    private PenaliteServiceImpl penaliteService;

    @Autowired
    private PenaliteRepository penaliteRepository;

    @Autowired
    private RetourRepository retourRepository;
    @Autowired
    private EmpruntRepository empruntRepository;


    @Test
    public void testFindByRetour() {
        // Setup
        Emprunt emprunt = new Emprunt();
       
        empruntRepository.save(emprunt);

        Retour retour = new Retour();
        retour.setEmprunt(emprunt); 
        retour.setStatutRetour(StatutRetour.EN_RETARD); 
        retourRepository.save(retour);

        Penalite penalite = new Penalite();
   
        penalite.setRetour(retour);
        penalite.setMontant(new BigDecimal("10.0"));
        penalite.setStatutPenalite(StatutPenalite.NON_PAYEE); 
        penaliteRepository.save(penalite);

        // Execute
        Penalite foundPenalite = penaliteService.findByRetour(retour);

        // Verify
        assertNotNull(foundPenalite);
        assertEquals(penalite.getId(), foundPenalite.getId());
    }
}
