package com.app.biblio.controller;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.biblio.bean.Emprunt;
import com.app.biblio.bean.Livre;
import com.app.biblio.bean.Reservation;
import com.app.biblio.bean.Retour;
import com.app.biblio.bean.Role;
import com.app.biblio.bean.StatutEmprunt;
import com.app.biblio.bean.StatutReservation;
import com.app.biblio.bean.StatutRetour;
import com.app.biblio.bean.User;
import com.app.biblio.service.EmpruntService;
import com.app.biblio.service.LivreService;
import com.app.biblio.service.NotificationService;
import com.app.biblio.service.PenaliteService;
import com.app.biblio.service.ReservationService;
import com.app.biblio.service.RetourService;
import com.app.biblio.service.RoleService;
import com.app.biblio.service.UserService;


@Controller
@RequestMapping("/adminDashboard")
public class AdminController {
	 @Autowired
	    private EmpruntService empruntService;
	 @Autowired
	    private LivreService livreService;
    @Autowired
    private UserService userService;
    @Autowired
    private RetourService retourService;
    @Autowired
    private PenaliteService penaliteService;
    @Autowired
    private RoleService roleService; 
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private ReservationService reservationService;
    @GetMapping("/users")
    public String listeUsers(Model model, @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 4); 
        Page<User> users = userService.getAllUsers(pageable);
        model.addAttribute("users", users);
        return "users";
    }
    
    @GetMapping("/emprunts")
    public String listEmprunts(@RequestParam(defaultValue = "0") int page, Model model) {
        Pageable pageable = PageRequest.of(page, 4); 
        Page<Emprunt> empruntsPage = empruntService.getAllEmprunt(pageable);
        model.addAttribute("emprunts", empruntsPage);
        return "empruntAdminDashboard"; 
    }
    @GetMapping("/retours")
    public String listRetours(@RequestParam(defaultValue = "0") int page, Model model) {
        Pageable pageable = PageRequest.of(page, 4); 
        Page<Retour> retoursPage = retourService.getAllRetour(pageable);
        model.addAttribute("retours", retoursPage);
        return "retourAdminDashboard"; 
    }
    @GetMapping("/users/add")
    public String showAddUserForm(Model model) {
        model.addAttribute("user", new User());
        
        List<Role> roles = roleService.getAllRoles();
        model.addAttribute("roles", roles);
        
        return "addUser"; 
    }
 
    @PostMapping("/users/add")
    public String addUser(@ModelAttribute("user") User user, @RequestParam("roles") Long roleId, RedirectAttributes redirectAttributes) {
    
        Role role = roleService.findById(roleId);

        user.getRoles().add(role); 

        userService.save(user);
        redirectAttributes.addFlashAttribute("successMessage", "L'utilisateur " + user.getUsername() + " a été ajouté avec succès.");
        return "redirect:/adminDashboard/users";
    }
    @GetMapping("/users/edit/{userId}")
    public String showEditUserForm(@PathVariable("userId") Long userId, Model model) {
        
        User user = userService.findById(userId);
       
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.getAllRoles());
        return "editUser"; 
    }

    @PostMapping("/users/update")
    public String updateUser(@ModelAttribute("user") User user) {
        userService.update(user);
        return "redirect:/adminDashboard/users";
    }
    @GetMapping("/users/details/{userId}")
    public String showUserInfo(@PathVariable Long userId, Model model) {
      
        User user = userService.findById(userId);
        
        
        model.addAttribute("user", user);
        
       
        return "userDetails";
    }
    @GetMapping("/users/delete/{id}")
    public String showDeleteConfirmationPage(@PathVariable Long id, Model model) {
      
        User userToDelete = userService.findById(id);
        model.addAttribute("userToDelete", userToDelete);
        return "deleteUser"; 
    }

    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
       
        userService.deleteById(id);
        
        return "redirect:/adminDashboard/users"; 
    }

    @PostMapping("/emprunts/validate/confirm")
    public String confirmValidateEmprunt(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        Emprunt emprunt = empruntService.findById(id);
        if (emprunt == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "L'emprunt avec l'ID spécifié n'existe pas.");
            return "redirect:/adminDashboard/emprunts";
        }

        Livre livre = emprunt.getLivre();
        int availableCopies = livre.getAvailableCopies();
        int requestedCopies = emprunt.getNombreExemplaires();

        if (availableCopies == 0) {
            redirectAttributes.addFlashAttribute("errorMessage", "Il n'y a plus de copies disponibles pour ce livre.");
            return "redirect:/adminDashboard/emprunts";
        }

       
        if (requestedCopies > availableCopies) {
            redirectAttributes.addFlashAttribute("errorMessage", "Il n'y a pas assez de copies disponibles pour le livre " + livre.getTitle() + ".");
            return "redirect:/adminDashboard/emprunts";
        }

        emprunt.setStatut(StatutEmprunt.EMPRUNTE);
        empruntService.save(emprunt);

        livre.setAvailableCopies(availableCopies - requestedCopies);
        livreService.save(livre);

        String message = "Votre emprunt du livre " + livre.getTitle() + " a été validé.";
        notificationService.createNotification(emprunt.getUser(), message);
        int unreadCount = notificationService.countUnreadNotifications(emprunt.getUser());
        System.out.println("Unread count after emprunt validation: " + unreadCount);

        redirectAttributes.addFlashAttribute("successMessage", "L'emprunt a été validé avec succès et le nombre de copies disponibles a été mis à jour.");

        return "redirect:/adminDashboard/emprunts";
    }


    @GetMapping("/emprunts/validate/confirm/{id}")
    public String showValidateConfirmation(@PathVariable Long id, Model model) {
        
        Emprunt emprunt = empruntService.findById(id);
       
        model.addAttribute("emprunt", emprunt);
       
        return "validateEmprunt"; 
    }

    @PostMapping("/emprunts/reject/confirm")
    public String confirmRejectEmprunt(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        
        Emprunt emprunt = empruntService.findById(id);
        emprunt.setStatut(StatutEmprunt.REJETE); 
        empruntService.save(emprunt);

        redirectAttributes.addFlashAttribute("successMessage", "L'emprunt a été rejeté avec succès.");
   
        String message = "Votre demande d'emprunt du livre " + emprunt.getLivre().getTitle() + " a été rejetée.";
        notificationService.createNotification(emprunt.getUser(), message);
        return "redirect:/adminDashboard/emprunts";
    }
    @GetMapping("/emprunts/reject/confirm/{id}")
    public String showRejectConfirmation(@PathVariable Long id, Model model) {
        
        Emprunt emprunt = empruntService.findById(id);
        
        model.addAttribute("emprunt", emprunt);
        
        return "rejetEmprunt"; 
    }

    @GetMapping("/retours/valider/confirm/{id}")
    public String showValidationConfirmation(@PathVariable("id") Long retourId, Model model) {
        Retour retour = retourService.findById(retourId);
        if (retour != null && retour.getStatutRetour().name().equals("EN_ATTENTE")) {
            model.addAttribute("retour", retour);
            return "validateRetour"; 
        } else {
           
            return "redirect:/adminDashboard/retours";
        }
    }

    @PostMapping("/retours/valider/confirm")
    public String validateRetour(@RequestParam("id") Long retourId) {
        Retour retour = retourService.findById(retourId);
        if (retour != null) {
            retour.setStatutRetour(StatutRetour.A_L_HEURE);
            Livre livre = retour.getEmprunt().getLivre();
            if (livre != null) {
                int empruntedCopies = retour.getEmprunt().getNombreExemplaires();
                livre.setAvailableCopies(livre.getAvailableCopies() + empruntedCopies);
                livreService.save(livre);
            }
            retourService.save(retour);


            List<Reservation> reservations = reservationService.findByLivreOrderByDateAsc(livre.getId());
            if (!reservations.isEmpty()) {
                for (Reservation reservation : reservations) {
                    User user = reservation.getUser();
                    String message = "Le livre " + livre.getTitle() + " est maintenant disponible pour retrait.";
                   
                    if (!notificationService.hasUserBeenNotifiedForBook(user, livre)) {
                        notificationService.createNotification(user, message);
                    }
                }
            }

            String message = "Votre retour du livre " + retour.getEmprunt().getLivre().getTitle() + " a été validé.";
            notificationService.createNotification(retour.getEmprunt().getUser(), message);
        }
       
        return "redirect:/adminDashboard/retours";
    }


    @GetMapping("/reservations")
    public String listReservations(@RequestParam(defaultValue = "0") int page, Model model) {
        Pageable pageable = PageRequest.of(page, 4);
        Page<Reservation> reservationsPage = reservationService.findAll(pageable);
        model.addAttribute("reservations", reservationsPage);
        return "reservationAdminDashboard";
    }
    @PostMapping("/reservations/validate/confirm")
    public String confirmValidateReservation(@RequestParam Long id, RedirectAttributes redirectAttributes) {
  
        Reservation reservation = reservationService.findById(id);
     
        reservation.setStatut(StatutReservation.VALIDE);
    
        reservationService.save(reservation);

   
        String message = "Votre réservation du livre " + reservation.getLivre().getTitle() + " a été validée.";
        notificationService.createNotification(reservation.getUser(), message);

        redirectAttributes.addFlashAttribute("successMessage", "La réservation a été validée avec succès.");

        return "redirect:/adminDashboard/reservations";
    }
    
    @GetMapping("/reservations/validate/confirm/{id}")
    public String showValiteConfirmation(@PathVariable Long id, Model model) {
        Reservation reservation = reservationService.findById(id);
        model.addAttribute("reservation", reservation);
        return "validateReservation";
    }


    @GetMapping("/reservations/cancel/confirm/{id}")
    public String showCancelConfirmation(@PathVariable Long id, Model model) {
        Reservation reservation = reservationService.findById(id);
        model.addAttribute("reservation", reservation);
        return "cancelReservation"; 
    }

    @PostMapping("/reservations/cancel/confirm")
    public String confirmCancelReservation(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        Reservation reservation = reservationService.findById(id);
        reservation.setStatut(StatutReservation.ANNULE); 
        reservationService.save(reservation);

        redirectAttributes.addFlashAttribute("successMessage", "La réservation a été annulée avec succès.");
       
        String message = "Votre réservation du livre " + reservation.getLivre().getTitle() + " a été annulée.";
        notificationService.createNotification(reservation.getUser(), message);
        return "redirect:/adminDashboard/reservations";
    }
    @GetMapping("/emprunts/add")
    public String showAddEmpruntForm(Model model) {
  
        List<User> users = userService.getUsersByRole("ROLE_USER");
        List<Livre> livres = livreService.getAllLivres();
        model.addAttribute("users", users);
        model.addAttribute("livres", livres);
        model.addAttribute("emprunt", new Emprunt()); 
        return "addEmprunt"; 
    }

    
    @PostMapping("/emprunts/add")
    public String addEmprunt(@ModelAttribute("emprunt") Emprunt emprunt, Model model) throws ParseException {
    	
        if (emprunt.getUser() == null) {
            model.addAttribute("errorMessage", "Veuillez sélectionner un utilisateur.");
            return "errorUser";
        }

    	 String cin = emprunt.getUser().getCIN(); 
    	    if (penaliteService.hasUnpaidPenalties(cin)) {
    	        model.addAttribute("errorMessage", cin + " a des pénalités impayées. Vous ne pouvez pas ajouter un nouvel emprunt pour lui.");
    	        return "errorUser";
    	    }
   
        Date dateEmprunt = emprunt.getDateEmprunt();
        Date dateRetourPrevu = emprunt.getDateRetourPrevu();

        if (dateRetourPrevu.before(dateEmprunt)) {
            model.addAttribute("errorMessage", "La date de retour ne peut pas être antérieure à la date d'emprunt.");
            return "errorUser";
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(dateEmprunt);
        cal.add(Calendar.DATE, 14);
        Date maxDateRetour = cal.getTime();

        if (dateRetourPrevu.after(maxDateRetour)) {
            model.addAttribute("errorMessage", "La date de retour ne peut pas dépasser la date d'emprunt de plus de 2 semaines.");
            return "errorUser";
        }

        if (emprunt.getUser() == null || emprunt.getLivre() == null) {
            model.addAttribute("errorMessage", "Veuillez sélectionner un utilisateur et un livre.");
            return "errorUser";
        }

        if (emprunt.getLivre().getAvailableCopies() <= 0) {
            model.addAttribute("errorMessage", "Le livre sélectionné n'est pas disponible pour l'emprunt.");
            return "errorUser";
        }

        if (emprunt.getNombreExemplaires() > emprunt.getLivre().getAvailableCopies()) {
            model.addAttribute("errorMessage", "Le nombre d'exemplaires demandé est supérieur au nombre de copies disponibles pour le livre sélectionné.");
            return "errorUser";
        }

       
        emprunt.getLivre().setAvailableCopies(emprunt.getLivre().getAvailableCopies() - emprunt.getNombreExemplaires());
        livreService.save(emprunt.getLivre()); 

        // Sauvegarder l'emprunt
       // empruntService.save(emprunt);


        emprunt.setStatut(StatutEmprunt.EMPRUNTE);
        empruntService.save(emprunt); 

        String message = "Votre demande d'emprunt pour le livre " + emprunt.getLivre().getTitle() + " a été validée.";
        notificationService.createNotification(emprunt.getUser(), message);

        return "redirect:/adminDashboard/emprunts";
    }

    @GetMapping("/retours/add")
    public String showReturnForm(Model model) {
   
        List<Emprunt> emprunts = empruntService.findAllEmpruntsWithStatusEmprunte();
        model.addAttribute("emprunts", emprunts);
        return "addRetour"; 
    }
    @PostMapping("/retours/add/emprunts/select")
    public ModelAndView selectEmprunt(@RequestParam("empruntId") Long empruntId) {
        Emprunt emprunt = empruntService.findById(empruntId);
        ModelAndView mav = new ModelAndView("empruntDetails");
        mav.addObject("emprunt", emprunt);
        return mav;
    }
    @PostMapping("/emprunts/return/{id}")
    public String handleReturn(@PathVariable("id") Long empruntId,
                               @RequestParam("dateRetourEffectif") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateRetourEffectif,
                               @RequestParam("note") Integer note) {
        Emprunt emprunt = empruntService.findById(empruntId);
        Retour retour = new Retour();
        retour.setEmprunt(emprunt);
        retour.setDateRetourEffectif(dateRetourEffectif);
        retour.setNote(note);

        if (dateRetourEffectif.compareTo(emprunt.getDateRetourPrevu()) <= 0) {
            retour.setStatutRetour(StatutRetour.A_L_HEURE); 
            emprunt.setStatut(StatutEmprunt.RETOURNE);

            int empruntedCopies = emprunt.getNombreExemplaires();
            emprunt.getLivre().setAvailableCopies(emprunt.getLivre().getAvailableCopies() + empruntedCopies);
            livreService.save(emprunt.getLivre()); 
        } else {
            retour.setStatutRetour(StatutRetour.EN_ATTENTE);
            emprunt.setStatut(StatutEmprunt.RETOURNE);
        }

        retourService.save(retour);
        empruntService.save(emprunt);

        String message = "Le livre " + emprunt.getLivre().getTitle() + " a été retourné.";
        notificationService.createNotification(emprunt.getUser(), message);

        List<Reservation> reservations = reservationService.findByLivreOrderByDateAsc(emprunt.getLivre().getId());
        if (!reservations.isEmpty()) {
            for (Reservation reservation : reservations) {
                User user = reservation.getUser();
                String reservationMessage = "Le livre " + emprunt.getLivre().getTitle() + " est maintenant disponible pour retrait.";
                
                if (!notificationService.hasUserBeenNotifiedForBook(user, emprunt.getLivre())) {
                    notificationService.createNotification(user, reservationMessage);
                }
            }
        }

        return "redirect:/adminDashboard/retours";
    }

    @GetMapping("/reservations/add")
    public String showAddReservationForm(Model model) {
     
        List<Livre> livres = livreService.getAllLivres();

        List<Livre> livresDisponibles = livres.stream()
                .filter(livre -> livre.getAvailableCopies() == 0)
                .collect(Collectors.toList());

        model.addAttribute("livres", livresDisponibles);

       
        List<User> users = userService.getUsersByRole("ROLE_USER"); 

       
        model.addAttribute("users", users);

      
        model.addAttribute("reservation", new Reservation());

        return "addReservation";
    }

    @PostMapping("/reservations/add")
    public String addReservation(@ModelAttribute("reservation") Reservation reservation, Model model) {
        String cin = reservation.getUser().getCIN();
        if (penaliteService.hasUnpaidPenalties(cin)) {
            model.addAttribute("errorMessage", "L'utilisateur " + reservation.getUser().getUsername() + " a des pénalités impayées. Vous ne pouvez pas ajouter une réservation pour lui.");
            return "errorUser";
        }

      
        reservation.setStatut(StatutReservation.VALIDE);

        reservation.setDate(LocalDateTime.now());

       
        reservationService.save(reservation);

        String message = "Votre réservation pour le livre " + reservation.getLivre().getTitle() + " a été enregistrée et est maintenant valide.";
        notificationService.createNotification(reservation.getUser(), message);

        return "redirect:/adminDashboard/reservations";
    }



    
}
