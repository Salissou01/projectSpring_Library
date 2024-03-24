package com.app.biblio.service;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.app.biblio.bean.Emprunt;
import com.app.biblio.bean.StatutEmprunt;
import com.app.biblio.bean.User;
import com.app.biblio.repository.EmpruntRepository;

@ExtendWith(MockitoExtension.class)
public class EmpruntServiceTest {

    @Mock
    private EmpruntRepository empruntRepository;

    @InjectMocks
    private EmpruntServiceImpl empruntService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAllEmpruntsWithStatusEmprunte() {
        // Setup
        List<Emprunt> emprunts = new ArrayList<>();
        Emprunt emprunt1 = new Emprunt();
        Emprunt emprunt2 = new Emprunt();
        emprunts.add(emprunt1);
        emprunts.add(emprunt2);

       
        when(empruntRepository.findByStatut(StatutEmprunt.EMPRUNTE)).thenReturn(emprunts);

        // Execute
        List<Emprunt> result = empruntService.findAllEmpruntsWithStatusEmprunte();

        // Vérify
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testCountByStatut() {
        // Setup
        long expectedCount = 5L;
        when(empruntRepository.countByStatut(StatutEmprunt.REJETE)).thenReturn(expectedCount);

        // Execute
        long result = empruntService.countByStatut(StatutEmprunt.REJETE);

        // Verify
        assertEquals(expectedCount, result);
    }

   
}
