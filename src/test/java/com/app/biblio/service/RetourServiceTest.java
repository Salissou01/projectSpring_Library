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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.app.biblio.bean.Retour;
import com.app.biblio.repository.RetourRepository;

@ExtendWith(MockitoExtension.class)
public class RetourServiceTest {

    @Mock
    private RetourRepository retourRepository;

    @InjectMocks
    private RetourServiceImpl retourService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllRetour() {
        // Setup
        List<Retour> retours = new ArrayList<>();
        Retour retour1 = new Retour();
        Retour retour2 = new Retour();
        retours.add(retour1);
        retours.add(retour2);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Retour> expectedPage = new PageImpl<>(retours);

        when(retourRepository.findAll(pageable)).thenReturn(expectedPage);

        // Execute
        Page<Retour> result = retourService.getAllRetour(pageable);

        // Verify
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
    }

    @Test
    public void testGetAllRetourByCin() {
        // Setup
        String cin = "123456789";
        List<Retour> retours = new ArrayList<>();
        Retour retour1 = new Retour();
        retours.add(retour1);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Retour> expectedPage = new PageImpl<>(retours);

        when(retourRepository.findByEmpruntUserCIN(cin, pageable)).thenReturn(expectedPage);

        // Execute
        Page<Retour> result = retourService.getAllRetourByCin(cin, pageable);

        // Verify
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    @Test
    public void testFindByIdWithAssociations() {
        // Setup
        Long id = 1L;
        Retour retour = new Retour();
        Optional<Retour> expectedRetour = Optional.of(retour);

        when(retourRepository.findByIdWithAssociations(id)).thenReturn(expectedRetour);

        // Execute
        Retour result = retourService.findByIdWithAssociations(id);

        // Verify
        assertNotNull(result);
        assertEquals(retour, result);
    }
}
