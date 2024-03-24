package com.app.biblio.controller;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.biblio.bean.Emprunt;
import com.app.biblio.bean.Livre;
import com.app.biblio.bean.Reservation;
import com.app.biblio.bean.Retour;
import com.app.biblio.bean.Role;
import com.app.biblio.bean.StatutRetour;
import com.app.biblio.bean.User;
import com.app.biblio.service.EmpruntService;
import com.app.biblio.service.LivreService;
import com.app.biblio.service.NotificationService;
import com.app.biblio.service.ReservationService;
import com.app.biblio.service.RetourService;
import com.app.biblio.service.RoleService;
import com.app.biblio.service.UserService;
@ExtendWith(MockitoExtension.class)
public class AdminControllerUnitTest {

    @Mock
    private UserService userService;

    @Mock
    private RoleService roleService;
    @Mock
    private RetourService retourService;
    @Mock
    private ReservationService reservationService;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private AdminController adminController;
    @Mock
    private EmpruntService empruntService;

    @Mock
    private LivreService livreService;

    @Mock
    private NotificationService notificationService;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testShowAddUserForm() {
        // Setup
        List<Role> roles = Arrays.asList(new Role(), new Role());
        when(roleService.getAllRoles()).thenReturn(roles);

        // Execute
        String viewName = adminController.showAddUserForm(model);

        // Verify
        assertEquals("addUser", viewName);
        verify(model).addAttribute(eq("user"), any(User.class));
        verify(model).addAttribute("roles", roles);
    }
    @Test
    public void testUpdateUser() {
        // Setup
        User user = new User(); 
        user.setId(1L); 

        // Execute
        String redirectUrl = adminController.updateUser(user);

        // Verify
        assertEquals("redirect:/adminDashboard/users", redirectUrl);
        verify(userService).update(user);
    }
    @Test
    public void testConfirmValidateEmprunt() {
        // Setup
        Long id = 1L; 
        Emprunt emprunt = new Emprunt(); 
        emprunt.setId(id);
        emprunt.setNombreExemplaires(1); 

        Livre livre = new Livre(); 
        livre.setAvailableCopies(1); 
        emprunt.setLivre(livre);

        when(empruntService.findById(id)).thenReturn(emprunt);
        when(livreService.save(livre)).thenReturn(livre);

        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        // Exécute
        String redirectUrl = adminController.confirmValidateEmprunt(id, redirectAttributes);

        // Vérify
        assertEquals("redirect:/adminDashboard/emprunts", redirectUrl);
        verify(empruntService).save(emprunt);
        verify(livreService).save(livre);
        verify(notificationService).createNotification(any(), anyString());
        verify(redirectAttributes).addFlashAttribute(eq("successMessage"), anyString());
    }
    
    @Test
    public void testShowAddEmpruntForm() {
        // Setup
        List<User> users = new ArrayList<>();
        users.add(new User()); // Ajouter un utilisateur fictif
        when(userService.getUsersByRole("ROLE_USER")).thenReturn(users);

        List<Livre> livres = new ArrayList<>();
        livres.add(new Livre()); // Ajouter un livre fictif
        when(livreService.getAllLivres()).thenReturn(livres);

        // Execute
        String viewName = adminController.showAddEmpruntForm(model);

        // Verify
        assertEquals("addEmprunt", viewName);
        verify(model).addAttribute("users", users);
        verify(model).addAttribute("livres", livres);
        verify(model).addAttribute(eq("emprunt"), Mockito.any(Emprunt.class));
    }
    
    @Test
    public void testListeUsers() {
        // Préparation
        Page<User> usersPage = mock(Page.class);
        when(userService.getAllUsers(any(Pageable.class))).thenReturn(usersPage);

        // Exécution
        String viewName = adminController.listeUsers(model, 0);

        // Vérification
        assertEquals("users", viewName);
        verify(model).addAttribute("users", usersPage);
    }
    
    @Test
    public void testListEmprunts() {
        // Préparation
        Page<Emprunt> empruntsPage = mock(Page.class);
        when(empruntService.getAllEmprunt(any(Pageable.class))).thenReturn(empruntsPage);

        // Exécution
        String viewName = adminController.listEmprunts(0, model);

        // Vérification
        assertEquals("empruntAdminDashboard", viewName);
        verify(model).addAttribute("emprunts", empruntsPage);
    }
    @Test
    public void testListRetours() {
        // Préparation
        Page<Retour> retoursPage = mock(Page.class);
        when(retourService.getAllRetour(any(Pageable.class))).thenReturn(retoursPage);

        // Exécution
        String viewName = adminController.listRetours(0, model);

        // Vérification
        assertEquals("retourAdminDashboard", viewName);
        verify(model).addAttribute("retours", retoursPage);
    }
    @Test
    public void testShowEditUserForm() {
        // Préparation
        User user = new User();
        user.setId(1L);
        when(userService.findById(1L)).thenReturn(user);

        // Exécution
        String viewName = adminController.showEditUserForm(1L, model);

        // Vérification
        assertEquals("editUser", viewName);
        verify(model).addAttribute("user", user);
        verify(model).addAttribute("roles", roleService.getAllRoles());
    }
    @Test
    public void testShowUserInfo() {
        // Préparation
        User user = new User();
        user.setId(1L);
        when(userService.findById(1L)).thenReturn(user);

        // Exécution
        String viewName = adminController.showUserInfo(1L, model);

        // Vérification
        assertEquals("userDetails", viewName);
        verify(model).addAttribute("user", user);
    }

    @Test
    public void testShowDeleteConfirmationPage() {
        // Préparation
        User user = new User();
        user.setId(1L);
        when(userService.findById(1L)).thenReturn(user);

        // Exécution
        String viewName = adminController.showDeleteConfirmationPage(1L, model);

        // Vérification
        assertEquals("deleteUser", viewName);
        verify(model).addAttribute("userToDelete", user);
    }
    @Test
    public void testDeleteUser() {
        // Exécution
        String redirectUrl = adminController.deleteUser(1L);

        // Vérification
        assertEquals("redirect:/adminDashboard/users", redirectUrl);
        verify(userService).deleteById(1L);
    }
    @Test
    public void testShowValidateConfirmation() {
        // Préparation
        Emprunt emprunt = new Emprunt();
        emprunt.setId(1L);
        when(empruntService.findById(1L)).thenReturn(emprunt);

        // Exécution
        String viewName = adminController.showValidateConfirmation(1L, model);

        // Vérification
        assertEquals("validateEmprunt", viewName);
        verify(model).addAttribute("emprunt", emprunt);
    }
    @Test
    public void testShowRejectConfirmation() {
        // Préparation
        Emprunt emprunt = new Emprunt();
        emprunt.setId(1L);
        when(empruntService.findById(1L)).thenReturn(emprunt);

        // Exécution
        String viewName = adminController.showRejectConfirmation(1L, model);

        // Vérification
        assertEquals("rejetEmprunt", viewName);
        verify(model).addAttribute("emprunt", emprunt);
    }
  
    @Test
    public void testShowCancelConfirmation() {
        // Préparation
        Reservation reservation = new Reservation();
        reservation.setId(1L);
        when(reservationService.findById(1L)).thenReturn(reservation);

        // Exécution
        String viewName = adminController.showCancelConfirmation(1L, model);

        // Vérification
        assertEquals("cancelReservation", viewName);
        verify(model).addAttribute("reservation", reservation);
    }



}
