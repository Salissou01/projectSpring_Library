package com.app.biblio.controller;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.app.biblio.bean.Category;
import com.app.biblio.bean.Livre;
import com.app.biblio.service.CategoryService;
import com.app.biblio.service.LivreService;
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class LivreControllerIntegrationTest {

    @Mock
    private LivreService livreService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private Model model;

    @InjectMocks
    private LivreController livreController;

    @Test
    public void testShowEditLivreForm_LivreFound() {
        // Setup
        Long id = 1L;
        Livre livre = new Livre();
        when(livreService.findById(id)).thenReturn(livre);

        List<Category> categories = new ArrayList<>();
        when(categoryService.getAllCategories()).thenReturn(categories);

        // Execute
        String result = livreController.showEditLivreForm(id, model);

        // Verify
        verify(model).addAttribute("livre", livre);
        verify(model).addAttribute("categories", categories);
        assertEquals("editLivre", result);
    }

    @Test
    public void testShowEditLivreForm_LivreNotFound() {
        // Setup
        Long id = 1L;
        when(livreService.findById(id)).thenReturn(null);

        // Execute
        String result = livreController.showEditLivreForm(id, model);

        // Verify
        assertEquals("livreNotFound", result);
    }

    @Test
    public void testEditLivre_WithValidData() throws IOException {
        // Setup
        Long id = 1L;
        Livre existingLivre = new Livre();
        when(livreService.findById(id)).thenReturn(existingLivre);

        MultipartFile file = mock(MultipartFile.class);
        Category category = new Category();
        Long categoryId = 1L;
        when(categoryService.findById(categoryId)).thenReturn(category);

        // Execute
        String result = livreController.editLivre(id, existingLivre, file, categoryId, model);

        // Verify
        verify(livreService).save(existingLivre);
        verify(model, never()).addAttribute(eq("error"), anyString());
        assertEquals("redirect:/adminDashboard/livres", result);
    }

    
}
