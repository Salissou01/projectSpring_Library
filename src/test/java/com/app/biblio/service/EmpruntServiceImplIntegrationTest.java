package com.app.biblio.service;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import com.app.biblio.bean.Emprunt;
import com.app.biblio.bean.StatutEmprunt;
import com.app.biblio.bean.User;
import com.app.biblio.repository.EmpruntRepository;
import com.app.biblio.repository.UserRepository;

@SpringBootTest
@ActiveProfiles("test")
public class EmpruntServiceImplIntegrationTest {

    @Autowired
    private EmpruntServiceImpl empruntService;

    @Autowired
    private EmpruntRepository empruntRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Test
    public void testGetAllEmpruntByCin() {
        // Setup
        User user = new User();
        user.setCIN("12345");
        userRepository.save(user); 

        Emprunt emprunt1 = new Emprunt();
        emprunt1.setUser(user);
        Emprunt emprunt2 = new Emprunt();
        emprunt2.setUser(user);
        empruntRepository.save(emprunt1);
        empruntRepository.save(emprunt2);

        // Execute
        Pageable pageable = PageRequest.of(0, 10);
        Page<Emprunt> emprunts = empruntService.getAllEmpruntByCin("12345", pageable);

        // Verify
        assertNotNull(emprunts);
        assertEquals(2, emprunts.getTotalElements());
    }


    @Test
    public void testFindAllEmpruntsWithStatusEmprunte() {
        // Setup
        Emprunt emprunt1 = new Emprunt();
        emprunt1.setStatut(StatutEmprunt.EMPRUNTE);
        Emprunt emprunt2 = new Emprunt();
        emprunt2.setStatut(StatutEmprunt.EMPRUNTE);
        empruntRepository.save(emprunt1);
        empruntRepository.save(emprunt2);

        // Execute
        List<Emprunt> emprunts = empruntService.findAllEmpruntsWithStatusEmprunte();

        // Verify
        assertNotNull(emprunts);
        assertEquals(2, emprunts.size());
        assertTrue(emprunts.stream().allMatch(e -> e.getStatut() == StatutEmprunt.EMPRUNTE));
    }


}
