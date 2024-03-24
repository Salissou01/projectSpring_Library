package com.app.biblio.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.app.biblio.bean.Livre;
import com.app.biblio.repository.LivreRepository;

@SpringBootTest
@ActiveProfiles("test")
public class LivreServiceImplIntegrationTest {

    @Autowired
    private LivreService livreService;

    @Autowired
    private LivreRepository livreRepository;

    @Test
    public void testSave() {
        // Setup
        Livre livre = new Livre();
        livre.setTitle("Harry Potter");
        livre.setAuthor("J.K. Rowling");

        // Execute
        Livre savedLivre = livreService.save(livre);

        // Verify
        assertNotNull(savedLivre.getId());
        assertEquals("Harry Potter", savedLivre.getTitle());
        assertEquals("J.K. Rowling", savedLivre.getAuthor());
    }

    @Test
    public void testDelete() {
        // Setup
        Livre livre = new Livre();
        livre.setTitle("Le Seigneur des Anneaux");
        livre.setAuthor("J.R.R. Tolkien");
        Livre savedLivre = livreRepository.save(livre);

        // Execute
        livreService.delete(savedLivre);

        // Verify
        assertNull(livreRepository.findById(savedLivre.getId()).orElse(null));
    }

    @Test
    public void testFindByISBN() {
        // Setup
        Livre livre = new Livre();
        livre.setTitle("Le Petit Prince");
        livre.setAuthor("Antoine de Saint-Exupéry");
        livre.setISBN("978-3-16-148410-0");
        Livre savedLivre = livreRepository.save(livre);

        // Execute
        Livre foundLivre = livreService.findByIsbn("978-3-16-148410-0");

        // Verify
        assertNotNull(foundLivre);
        assertEquals("Le Petit Prince", foundLivre.getTitle());
        assertEquals("Antoine de Saint-Exupéry", foundLivre.getAuthor());
        assertEquals("978-3-16-148410-0", foundLivre.getISBN());
    }
}
