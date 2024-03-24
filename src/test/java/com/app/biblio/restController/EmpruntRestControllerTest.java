package com.app.biblio.restController;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.app.biblio.bean.Emprunt;
import com.app.biblio.service.EmpruntService;

@ExtendWith(MockitoExtension.class)
public class EmpruntRestControllerTest {

    @Mock
    private EmpruntService empruntService;

    @InjectMocks
    private EmpruntRestController empruntRestController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllEmprunts() {
        // Setup
        List<Emprunt> emprunts = Arrays.asList(new Emprunt(), new Emprunt());
        when(empruntService.findAll()).thenReturn(emprunts);

        // Exécute
        ResponseEntity<List<Emprunt>> response = empruntRestController.getAllEmprunts();

        // Vérify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(emprunts, response.getBody());
    }
    
    @Test
    public void testGetEmpruntById() {
        // Setup
        Emprunt emprunt = new Emprunt();
        when(empruntService.findById(1L)).thenReturn(emprunt);

        // Exécute
        ResponseEntity<Emprunt> response = empruntRestController.getEmpruntById(1L);

        // Vérify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(emprunt, response.getBody());
    }
}
