package com.app.biblio.restController;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.app.biblio.bean.Category;
import com.app.biblio.bean.Livre;
import com.app.biblio.bean.LivreDTO;
import com.app.biblio.service.CategoryService;
import com.app.biblio.service.LivreService;

@ExtendWith(MockitoExtension.class)
public class LivreRestControllerTest {

    @Mock
    private LivreService livreService;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private LivreRestController livreRestController;

    @Test
    public void testListLivres() {
        Livre livre1 = new Livre();
        livre1.setId(1L);
        livre1.setTitle("Livre 1");
 

        Livre livre2 = new Livre();
        livre2.setId(2L);
        livre2.setTitle("Livre 2");
    

        List<Livre> livres = Arrays.asList(livre1, livre2);

        when(livreService.getAllLivres()).thenReturn(livres);

        ResponseEntity<List<Livre>> response = livreRestController.listeLivres();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(livres, response.getBody());
    }

    @Test
    public void testAddLivre() throws IOException {
        LivreDTO livreDTO = new LivreDTO();
        livreDTO.setTitle("Livre Test");
        livreDTO.setAuthor("Auteur Test");
        livreDTO.setISBN("1234567890");
        livreDTO.setPublicationDate(new Date());
        livreDTO.setGenre("Genre Test");
        livreDTO.setAvailableCopies(10);
        livreDTO.setImagePath("C:/Users/LENOVO/Desktop/petit.png");
        livreDTO.setCategoryId(1L);
        livreDTO.setDescription("Description Test");
        livreDTO.setEditeur("Editeur Test");
        livreDTO.setNombrePages(200);

        Livre livre = new Livre();
        livre.setTitle("Livre Test");
        livre.setAuthor("Auteur Test");
        livre.setISBN("1234567890");
        livre.setPublicationDate(new Date());
        livre.setGenre("Genre Test");
        livre.setAvailableCopies(10);
        livre.setImageContent(new byte[0]); 
        Category category = new Category();
        category.setId(1L);
        livre.setCategory(category);
        livre.setDescription("Description Test");
        livre.setEditeur("Editeur Test");
        livre.setNombrePages(200);

        when(livreService.save(any(Livre.class))).thenReturn(livre);

        ResponseEntity<?> response = livreRestController.addLivre(livreDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void testEditLivre() throws IOException {
        LivreDTO livreDTO = new LivreDTO();
        livreDTO.setTitle("Livre Modifié");
        livreDTO.setAuthor("Auteur Modifié");
        livreDTO.setISBN("1234567890");
        livreDTO.setPublicationDate(new Date());
        livreDTO.setGenre("Genre Modifié");
        livreDTO.setAvailableCopies(20);
        livreDTO.setImagePath("C:/Users/LENOVO/Desktop/petit.png");
        livreDTO.setCategoryId(2L);
        livreDTO.setDescription("Description Modifiée");
        livreDTO.setEditeur("Editeur Modifié");
        livreDTO.setNombrePages(300);

        Livre existingLivre = new Livre();
        existingLivre.setId(1L);
        existingLivre.setTitle("Livre Existant");
        existingLivre.setAuthor("Auteur Existant");
        existingLivre.setISBN("0987654321");
        existingLivre.setPublicationDate(new Date());
        existingLivre.setGenre("Genre Existant");
        existingLivre.setAvailableCopies(10);
        existingLivre.setImageContent(new byte[0]); 
        Category existingCategory = new Category();
        existingCategory.setId(1L);
        existingLivre.setCategory(existingCategory);
        existingLivre.setDescription("Description Existant");
        existingLivre.setEditeur("Editeur Existant");
        existingLivre.setNombrePages(250);

        when(livreService.findById(1L)).thenReturn(existingLivre);
        when(livreService.save(any(Livre.class))).thenReturn(existingLivre);

        ResponseEntity<?> response = livreRestController.editLivre(1L, livreDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetLivre() {
        Livre livre = new Livre();
        livre.setId(1L);
        livre.setTitle("Livre 1");
    

        when(livreService.findById(1L)).thenReturn(livre);

        ResponseEntity<Livre> response = livreRestController.getLivre(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(livre, response.getBody());
    }


    @Test
    public void testDeleteLivre() {
        Livre livre = new Livre();
        livre.setId(1L);
        livre.setTitle("Livre 1");
       

        when(livreService.findById(1L)).thenReturn(livre);

        ResponseEntity<?> response = livreRestController.deleteLivre(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
