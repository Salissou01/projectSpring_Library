package com.app.biblio.restController;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.app.biblio.bean.Category;
import com.app.biblio.bean.Livre;
import com.app.biblio.bean.LivreDTO;
import com.app.biblio.service.CategoryService;
import com.app.biblio.service.LivreService;
import com.fasterxml.jackson.databind.ObjectMapper;
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")

public class LivreRestControllerIntegrationTest {

    private MockMvc mockMvc;

    @Mock
    private LivreService livreService;

    @InjectMocks
    private LivreRestController livreRestController;
    @Mock
    private CategoryService categoryService;
    @SuppressWarnings("deprecation")
	@BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(livreRestController).build();
    }

    @Test
    public void testGetLivre() throws Exception {
        // Setup
        Long id = 1L;
        Livre livre = new Livre();
        when(livreService.findById(id)).thenReturn(livre);

        // Execute and Verify
        mockMvc.perform(get("/api/livres/{id}", id))
                .andExpect(status().isOk());
    }
    

    @Test
    public void testEditLivreIntegration() throws Exception {
        // Setup
        Long id = 1L;
        LivreDTO livreDTO = new LivreDTO();
        livreDTO.setTitle("Nouveau titre");
        livreDTO.setAuthor("Nouvel auteur");
        livreDTO.setISBN("978-3-16-148410-0");
        LocalDate localDate = LocalDate.now();
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        livreDTO.setPublicationDate(date);
       
        livreDTO.setGenre("Fiction");
        livreDTO.setAvailableCopies(5);
        livreDTO.setDescription("Description du livre");
        livreDTO.setEditeur("Editeur");
        livreDTO.setNombrePages(200);
        livreDTO.setImagePath(Paths.get("C:\\Users\\LENOVO\\Downloads\\test-image.jpeg").toString());
        livreDTO.setCategoryId(1L);

        Livre existingLivre = new Livre();
        existingLivre.setId(id);
        

        Category category = new Category();
        category.setId(1L);
    
        when(livreService.findById(id)).thenReturn(existingLivre);
        when(categoryService.findById(1L)).thenReturn(category);
        when(livreService.save(any(Livre.class))).thenReturn(existingLivre);

        // Exécute
        mockMvc.perform(MockMvcRequestBuilders.put("/api/livres/edit/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(livreDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(livreDTO.getTitle()))
                .andExpect(jsonPath("$.author").value(livreDTO.getAuthor()));
                
    }
   
}
