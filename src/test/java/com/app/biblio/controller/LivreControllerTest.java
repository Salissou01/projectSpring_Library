package com.app.biblio.controller;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.app.biblio.bean.Category;
import com.app.biblio.bean.Livre;
import com.app.biblio.service.CategoryService;
import com.app.biblio.service.LivreService;
@ExtendWith(MockitoExtension.class)
public class LivreControllerTest {

    @Mock
    private LivreService livreService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private Model model;

    @InjectMocks
    private LivreController livreController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testListeLivres() {
        // Setup
        List<Livre> livres = new ArrayList<>();
        livres.add(new Livre());
        Page<Livre> livresPage = new PageImpl<>(livres);
        Pageable pageable = PageRequest.of(0, 4);
        when(livreService.getAllLivres(any(Pageable.class))).thenReturn(livresPage);

        // Execute
        String viewName = livreController.listeLivres(model, 0);

        // Verify
        assertEquals("livre", viewName);
        verify(model).addAttribute("livres", livresPage); 
    }
    @Test
    public void testGetImage() throws FileNotFoundException {
        // Setup
        Long livreId = 1L;
        byte[] imageData = {  };
        when(livreService.getImageContentById(livreId)).thenReturn(imageData);

        // Execute
        ResponseEntity<byte[]> responseEntity = livreController.getImage(livreId);

        // Verify
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(MediaType.IMAGE_JPEG, responseEntity.getHeaders().getContentType());
        assertArrayEquals(imageData, responseEntity.getBody());
    }
    
    @Test
    public void testAddLivre_WithValidImage() throws IOException {
        // Setup
        Livre livre = new Livre();
        MultipartFile file = mock(MultipartFile.class);
        Category category = new Category();
        category.setId(1L);
        Long categoryId = 1L;
        
        when(file.isEmpty()).thenReturn(false);
        when(file.getBytes()).thenReturn(new byte[] {  });
        when(categoryService.findById(categoryId)).thenReturn(category);

        // Execute
        String result = livreController.addLivre(livre, file, categoryId, model);

        // Verify
        verify(livreService).save(livre);
        verify(model, never()).addAttribute(eq("error"), anyString());
        assertEquals("redirect:/adminDashboard/livres", result);
    }

    @Test
    public void testAddLivre_WithInvalidImage() throws IOException {
        // Setup
        Livre livre = new Livre();
        MultipartFile file = mock(MultipartFile.class);
        Long categoryId = 1L;

        when(file.isEmpty()).thenReturn(true);

        // Execute
        String result = livreController.addLivre(livre, file, categoryId, model);

        // Verify
        verify(livreService, never()).save(livre);
        verify(model).addAttribute("error", "Veuillez sélectionner une image pour le livre.");
        assertEquals("redirect:/adminDashboard/livres/add", result);
    }

    @Test
    public void testAddLivre_WithIOException() throws IOException {
        // Setup
        Livre livre = new Livre();
        MultipartFile file = mock(MultipartFile.class);
        Long categoryId = 1L;

        when(file.isEmpty()).thenReturn(false);
        when(file.getBytes()).thenThrow(new IOException());

        // Execute
        String result = livreController.addLivre(livre, file, categoryId, model);

        // Verify
        verify(livreService, never()).save(livre);
        verify(model).addAttribute("error", "Une erreur s'est produite lors de l'enregistrement de l'image.");
        assertEquals("redirect:/adminDashboard/livres/add", result);
    }



}
