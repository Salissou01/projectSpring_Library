package com.app.biblio.restController;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.app.biblio.bean.request.EmpruntRequest;
import com.app.biblio.service.LivreService;
import com.app.biblio.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class EmpruntRestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserService userService;
    
    @Mock
    private LivreService livreService;

    @InjectMocks
    private EmpruntRestController empruntRestController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(empruntRestController).build();
    }

    @Test
    public void testCreateEmprunt() throws Exception {
        // Setup
        EmpruntRequest empruntRequest = new EmpruntRequest();
        empruntRequest.setDateEmprunt(new Date());
        empruntRequest.setDateRetourPrevu(new Date());
        empruntRequest.setCin("12345678");
        empruntRequest.setIsbn("978-3-16-148410-0");
        empruntRequest.setNombreExemplaires(1);

    
        when(userService.findByCin(empruntRequest.getCin())).thenReturn(null);
        when(livreService.findByIsbn(empruntRequest.getIsbn())).thenReturn(null);

        // Execute
        mockMvc.perform(post("/api/emprunts/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(empruntRequest)))
                .andExpect(status().isBadRequest());

        // Verify
        mockMvc.perform(post("/api/emprunts/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(empruntRequest)))
                .andExpect(status().isBadRequest());
    }
    
    
    
}
