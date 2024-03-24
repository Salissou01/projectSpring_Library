package com.app.biblio.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.biblio.bean.CustomUserDetails;
import com.app.biblio.bean.Emprunt;
import com.app.biblio.bean.Livre;
import com.app.biblio.bean.Reservation;
import com.app.biblio.bean.StatutEmprunt;
import com.app.biblio.bean.StatutReservation;
import com.app.biblio.bean.User;
import com.app.biblio.service.EmpruntService;
import com.app.biblio.service.LivreService;
import com.app.biblio.service.NotificationService;
import com.app.biblio.service.PenaliteService;
import com.app.biblio.service.ReservationService;
import com.app.biblio.service.UserService;

@Controller
@RequestMapping("/userDashboard")
public class ReservationController {

    @Autowired
    private LivreService livreService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private UserService userService;
    @Autowired
    private EmpruntService empruntService;
    @Autowired
    private PenaliteService penaliteService;
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/reservations/add")
    public String showAddReservationForm(@RequestParam("livreId") Long livreId, Model model) {
        Livre livre = livreService.findById(livreId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();

            User user = userService.findByUsername(username);
            if (user != null) {
               
                boolean hasUnpaidPenalties = penaliteService.hasUnpaidPenalties(user.getCIN());
                if (hasUnpaidPenalties) {
                   
                    model.addAttribute("errorMessage", "Vous avez une pénalité non payée. Veuillez la payer avant de continuer.");
                    return "errorUser";
                }

                model.addAttribute("livre", livre);
                model.addAttribute("user", user); 
                int unreadCount = notificationService.countUnreadNotifications(user);
                model.addAttribute("unreadCount", unreadCount);
                return "reservation";
            } else {
               
                return "redirect:/login";
            }
        } else {
           
            return "redirect:/login";
        }
    }

    
    @PostMapping("/reservations/add")
    public String addReservation(@RequestParam("livreId") Long livreId, Model model) {
       
        Livre livre = livreService.findById(livreId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            User user = userService.findByUsername(username);

            if (user != null && livre != null) {
         
                Reservation reservation = new Reservation();
                reservation.setLivre(livre);
                reservation.setUser(user);
                reservation.setDate(LocalDateTime.now()); 
                reservation.setStatut(StatutReservation.EN_ATTENTE); 

                reservationService.save(reservation);

                String message = "Votre réservation pour le livre " + livre.getTitle() + " a été enregistrée et est en attente de validation.";
                notificationService.createNotification(user, message);

                return "redirect:/userDashboard/livres/" + livreId;
            } else {
               
                return "redirect:/userDashboard/livres";
            }
        } else {
           
            return "redirect:/login";
        }
    }
    @GetMapping("/reservations")
    public String listReservations(@RequestParam(defaultValue = "0") int page, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String cin = userDetails.getCin(); 

            Pageable pageable = PageRequest.of(page, 1); 
            Page<Reservation> reservationsPage = reservationService.getAllReservationByCin(cin, pageable);
            model.addAttribute("reservations", reservationsPage);

            User user = userService.findByCin(cin); 
            int unreadCount = notificationService.countUnreadNotifications(user);
            model.addAttribute("unreadCount", unreadCount);

            Map<Long, Boolean> reservationValidationStatus = new HashMap<>();
            for (Reservation reservation : reservationsPage) {
                reservationValidationStatus.put(reservation.getId(), reservationService.isReservationValidated(reservation.getId()));
            }
            model.addAttribute("reservationValidationStatus", reservationValidationStatus);

            Map<Long, Boolean> reservationNotificationStatus = new HashMap<>();
            for (Reservation reservation : reservationsPage) {
                String message = "Le livre " + reservation.getLivre().getTitle() + " est maintenant disponible pour retrait.";
                reservationNotificationStatus.put(reservation.getId(), notificationService. messageExistsForUser(user, message));
            }
            model.addAttribute("reservationNotificationStatus", reservationNotificationStatus);

            return "reservationUserDashboard";
        } else {
            return "redirect:/login";
        }
    }

 
    
    @GetMapping("/reservations/retirer")
    public String showRetirerLivreForm(@RequestParam("reservationId") Long reservationId, Model model) {
        Reservation reservation = reservationService.findById(reservationId);
        if (reservation != null) {
            model.addAttribute("livre", reservation.getLivre());
         
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
                CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
                User user = userService.findByUsername(userDetails.getUsername());
                model.addAttribute("user", user);

                boolean hasUnpaidPenalties = penaliteService.hasUnpaidPenalties(user.getCIN());
                if (hasUnpaidPenalties) {
                   
                    model.addAttribute("errorMessage", "Vous avez une pénalité non payée. Veuillez la payer avant de continuer.");
                    return "errorUser";
                }
            }
            return "retirerLivre";
        } else {
            return "redirect:/userDashboard/reservations";
        }
    }


    @PostMapping("/reservations/retirer")
    public String addEmprunt(@RequestParam("ISBN") String ISBN, @RequestParam("dateEmprunt") String dateEmpruntStr, @RequestParam("dateRetourPrevu") String dateRetourPrevuStr, @RequestParam("nombreExemplaires") int nombreExemplaires, Model model) throws ParseException {
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateEmprunt = dateFormat.parse(dateEmpruntStr);
        Date dateRetourPrevu = dateFormat.parse(dateRetourPrevuStr);
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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String cin = userDetails.getCin();

            User user = userService.findByCin(cin);
            if (user != null) {
                Livre livre = livreService.findByIsbn(ISBN);

                if (livre.getAvailableCopies() >= nombreExemplaires) {
                    
                    Emprunt emprunt = new Emprunt();
                    emprunt.setDateEmprunt(dateEmprunt);
                    emprunt.setDateRetourPrevu(dateRetourPrevu);
                    emprunt.setUser(user);
                    emprunt.setLivre(livre);
                    emprunt.setStatut(StatutEmprunt.EMPRUNTE);

                    emprunt.setNombreExemplaires(nombreExemplaires); 
                    empruntService.save(emprunt);
                  
                    livre.setAvailableCopies(livre.getAvailableCopies() - nombreExemplaires);
                    livreService.save(livre);
                 
                    Reservation reservation = reservationService.findByLivreIsbnAndUserCin(ISBN, cin);
                    if (reservation != null) {
                        reservation.setStatut(StatutReservation.RETIRE);
                        reservationService.save(reservation);
                    }

                    String message = "La reservation pour le livre " + livre.getTitle() + " a été retirée." + " et le livre est a present " + emprunt.getStatut();
                    notificationService.createNotification(user, message);

                    int unreadCount = notificationService.countUnreadNotifications(user);
                    System.out.println("Unread count after adding emprunt: " + unreadCount);

                    return "redirect:/userDashboard/reservations?unreadCount=" + unreadCount;
                } else {
                    model.addAttribute("errorMessage", "Le nombre d'exemplaires demandé est supérieur au nombre de copies disponibles.");
                    return "errorUser";
                }
            } else {
                return "redirect:/login";
            }
        } else {
            return "redirect:/login";
        }
    }




}
