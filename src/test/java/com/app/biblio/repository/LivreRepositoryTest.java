package com.app.biblio.repository;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.app.biblio.bean.Livre;
@ExtendWith(MockitoExtension.class)

public class LivreRepositoryTest {

    @Mock
    private LivreRepository livreRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindByISBN() {
        // Préparation
       
        String isbn = "1234567890";
        Livre livre = new Livre();
        livre.setISBN(isbn);

        
        when(livreRepository.findByISBN(isbn)).thenReturn(livre);

        // Exécution
       
        Livre foundLivre = livreRepository.findByISBN(isbn);

        // Vérification
       
        assertNotNull(foundLivre);
        assertEquals(isbn, foundLivre.getISBN());

        // Vérifier que la méthode findByISBN a été appelée avec l'ISBN spécifique
        verify(livreRepository).findByISBN(isbn);
    }

}
