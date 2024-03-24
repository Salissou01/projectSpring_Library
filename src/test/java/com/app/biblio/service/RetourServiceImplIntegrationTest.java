package com.app.biblio.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import com.app.biblio.bean.Emprunt;
import com.app.biblio.bean.Retour;
import com.app.biblio.bean.StatutRetour;
import com.app.biblio.bean.User;
import com.app.biblio.repository.EmpruntRepository;
import com.app.biblio.repository.RetourRepository;
import com.app.biblio.repository.UserRepository;

@SpringBootTest
@ActiveProfiles("test")
public class RetourServiceImplIntegrationTest {

    @Autowired
    private RetourServiceImpl retourService;

    @Autowired
    private RetourRepository retourRepository;

    @Autowired
    private EmpruntRepository empruntRepository;

    @Autowired
    private UserRepository userRepository;
    @BeforeEach
    public void cleanUp() {
        retourRepository.deleteAll();
        empruntRepository.deleteAll();
        userRepository.deleteAll();
    }
    @Test
    public void testGetAllRetourByCin() {
        // Setup
        User user = new User();
        user.setCIN("12345");
        userRepository.save(user);

        Emprunt emprunt1 = new Emprunt();
        emprunt1.setUser(user);
        empruntRepository.save(emprunt1);

        Emprunt emprunt2 = new Emprunt();
        emprunt2.setUser(user);
        empruntRepository.save(emprunt2);

        Retour retour1 = new Retour();
        retour1.setEmprunt(emprunt1); // Assurez-vous que emprunt1 est sauvegardé avant
        retour1.setStatutRetour(StatutRetour.EN_ATTENTE); // Initialisez statutRetour
        retourRepository.save(retour1);

        Retour retour2 = new Retour();
        retour2.setEmprunt(emprunt2); // Assurez-vous que emprunt2 est sauvegardé avant
        retour2.setStatutRetour(StatutRetour.A_L_HEURE); // Initialisez statutRetour
        retourRepository.save(retour2);

        // Execute
        Pageable pageable = PageRequest.of(0, 10);
        Page<Retour> retours = retourService.getAllRetourByCin("12345", pageable);

        // Verify
        assertNotNull(retours);
        assertEquals(2, retours.getTotalElements());
    }

    @Test
    public void testGetAllRetour() {
        // Setup
        Emprunt emprunt1 = new Emprunt();
        empruntRepository.save(emprunt1); // Assurez-vous que emprunt1 est sauvegardé avant

        Retour retour1 = new Retour();
        retour1.setEmprunt(emprunt1); // Initialisez emprunt
        retour1.setStatutRetour(StatutRetour.A_L_HEURE); // Initialisez statutRetour
        retourRepository.save(retour1);

        Emprunt emprunt2 = new Emprunt();
        empruntRepository.save(emprunt2); // Assurez-vous que emprunt2 est sauvegardé avant

        Retour retour2 = new Retour();
        retour2.setEmprunt(emprunt2); // Initialisez emprunt
        retour2.setStatutRetour(StatutRetour.A_L_HEURE); // Initialisez statutRetour
        retourRepository.save(retour2);

        // Execute
        Pageable pageable = PageRequest.of(0, 10);
        Page<Retour> retours = retourService.getAllRetour(pageable);

        // Verify
        assertNotNull(retours);
        assertEquals(2, retours.getTotalElements());
    }
    
 





}
