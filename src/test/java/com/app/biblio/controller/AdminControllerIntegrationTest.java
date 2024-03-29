package com.app.biblio.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import org.springframework.ui.Model;

import com.app.biblio.bean.Emprunt;
import com.app.biblio.bean.Livre;

import com.app.biblio.bean.User;
import com.app.biblio.service.EmpruntService;
import com.app.biblio.service.LivreService;
import com.app.biblio.service.NotificationService;
import com.app.biblio.service.PenaliteService;
import com.app.biblio.service.ReservationService;
import com.app.biblio.service.UserService;

import net.minidev.json.parser.ParseException;
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AdminControllerIntegrationTest {
	
    @Mock
    private EmpruntService empruntService;

    @Mock
    private LivreService livreService;
    @Mock
    private UserService userService;
    @Mock
    private NotificationService notificationService;

    @Mock
    private PenaliteService penaliteService;
    
    @Mock
    private ReservationService reservationService;


    @InjectMocks
    private AdminController adminController;



    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddEmprunt() throws ParseException, java.text.ParseException {
        

        // Préparation des données simulées
        Livre livre = new Livre();
        livre.setAvailableCopies(1);

        User user = new User();
        user.setCIN("123456789");

        Emprunt emprunt = new Emprunt();
        emprunt.setUser(user);
        emprunt.setLivre(livre);
        emprunt.setNombreExemplaires(1);
        emprunt.setDateEmprunt(new Date());
        emprunt.setDateRetourPrevu(new Date());

        // Stubbing des services
        when(penaliteService.hasUnpaidPenalties(anyString())).thenReturn(false);
        when(livreService.save(any(Livre.class))).thenReturn(livre);
        when(empruntService.save(any(Emprunt.class))).thenReturn(emprunt);

        // Appel de la méthode du contrôleur
        Model model = mock(Model.class);
       // RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        String result = adminController.addEmprunt(emprunt, model);

        // Vérification des résultats
        assertEquals("redirect:/adminDashboard/emprunts", result);
        verify(empruntService, times(1)).save(emprunt);
        verify(livreService, times(1)).save(livre);
        verify(notificationService, times(1)).createNotification(user, "Votre demande d'emprunt pour le livre " + livre.getTitle() + " a été validée.");
    }
    
  




}
