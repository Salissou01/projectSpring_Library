package com.app.biblio.restController;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.app.biblio.bean.Emprunt;
import com.app.biblio.bean.Retour;
import com.app.biblio.bean.StatutRetour;

import com.app.biblio.service.EmpruntService;
import com.app.biblio.service.NotificationService;
import com.app.biblio.service.RetourService;
import com.app.biblio.service.UserService;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class RetourRestControllerIntegrationTest {

    private MockMvc mockMvc;

    @Mock
    private EmpruntService empruntService;

    @Mock
    private UserService userService;

    @Mock
    private RetourService retourService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private RetourRestController retourRestController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(retourRestController).build();
    }

    @Test
    public void testValidateReturn_ReturnLate() throws Exception {
        // Setup
        Long retourId = 1L;
        Retour retour = new Retour();
      
        retour.setId(retourId);
        retour.setStatutRetour(StatutRetour.EN_ATTENTE);
        
        Emprunt emprunt = new Emprunt();
        emprunt.setDateRetourPrevu(Date.from(LocalDate.now().minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant())); // Date de retour prévue hier
        
        retour.setEmprunt(emprunt);
        
  
        when(retourService.findById(retourId)).thenReturn(retour);

        // Execute and Verify
        mockMvc.perform(put("/api/retours/validate/{id}", retourId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("La date de retour effectif ou la date de retour prévue est manquante.")); // Modifiez le message d'erreur attendu

        // Verify 
        verify(retourService).findById(retourId);
    }



}
