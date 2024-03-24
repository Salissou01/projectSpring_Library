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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.app.biblio.bean.Retour;
import com.app.biblio.service.RetourService;

@ExtendWith(MockitoExtension.class)
public class RetourRestControllerTest {

    @Mock
    private RetourService retourService;

    @InjectMocks
    private RetourRestController retourRestController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllRetours() {
        // Setup
        List<Retour> retours = Arrays.asList(new Retour(), new Retour());
        Page<Retour> retourPage = new PageImpl<>(retours);
        when(retourService.getAllRetour(Pageable.unpaged())).thenReturn(retourPage);

        // Execute
        ResponseEntity<Page<Retour>> response = retourRestController.getAllRetours(Pageable.unpaged());

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(retourPage, response.getBody());
    }
    
    @Test
    public void testGetRetourById() {
        // Setup
        Retour retour = new Retour();
        when(retourService.findById(1L)).thenReturn(retour);

        // Execute
        ResponseEntity<Retour> response = retourRestController.getRetourById(1L);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(retour, response.getBody());
    }
}
