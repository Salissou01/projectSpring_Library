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

import com.app.biblio.bean.Livre;
import com.app.biblio.repository.LivreRepository;
@ExtendWith(MockitoExtension.class)
public class LivreServiceTest {

    @Mock
    private LivreRepository livreRepository;

    @InjectMocks
    private LivreServiceImpl livreService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllLivres() {
        // Préparation
        List<Livre> livres = new ArrayList<>();
        Livre livre1 = new Livre();
        Livre livre2 = new Livre();
        livres.add(livre1);
        livres.add(livre2);

        // Configuration du comportement attendu du repository
        when(livreRepository.findAll()).thenReturn(livres);

        // Exécution
        List<Livre> result = livreService.getAllLivres();

        // Vérification
        assertNotNull(result);
        assertEquals(2, result.size());
    }
    @Test
    public void testFindById() {
        // Préparation
        Long livreId = 1L;
        Livre livre = new Livre();
        livre.setId(livreId);

        // Configuration du comportement attendu du repository
        when(livreRepository.findById(livreId)).thenReturn(Optional.of(livre));

        // Exécution
        Livre result = livreService.findById(livreId);

        // Vérification
        assertNotNull(result);
        assertEquals(livreId, result.getId());
    }


   
}
