package com.app.biblio.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.biblio.bean.CustomUserDetails;
import com.app.biblio.bean.Livre;
import com.app.biblio.bean.Penalite;
import com.app.biblio.bean.Reservation;
import com.app.biblio.bean.Retour;
import com.app.biblio.bean.StatutPenalite;
import com.app.biblio.bean.StatutRetour;
import com.app.biblio.bean.User;
import com.app.biblio.service.LivreService;
import com.app.biblio.service.NotificationService;
import com.app.biblio.service.PenaliteService;
import com.app.biblio.service.ReservationService;
import com.app.biblio.service.RetourService;
import com.app.biblio.service.UserService;

@Controller
public class PenaltieController {
    @Autowired
    private PenaliteService penaliteService;

    @Autowired
    private RetourService retourService;

    @Autowired
    private LivreService livreService;

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private ReservationService reservationService;

    @Autowired
    private UserService userService;
    
    @PostMapping("/adminDashboard/retours/penalites/save")
    public String savePenalite(@RequestParam("retourId") Long retourId,
                               @RequestParam("penaliteParJour") BigDecimal penaliteParJour,
                               @RequestParam("montantPenalite") BigDecimal montantPenalite,
                               @RequestParam("joursDeRetard") int joursDeRetard, Model model) {
        Retour retour = retourService.findById(retourId);
        if (retour != null) {
            BigDecimal montantCalculé = penaliteParJour.multiply(BigDecimal.valueOf(joursDeRetard));
            Penalite penalite = new Penalite(retour, montantCalculé, StatutPenalite.NON_PAYEE);
            penaliteService.save(penalite);
            retour.setStatutRetour(StatutRetour.EN_RETARD);
            retourService.save(retour);
        
            String messageRetour = "Vous avez une pénalité de " + montantCalculé + " pour le retour retardé du livre " + retour.getEmprunt().getLivre().getTitle() + ".";
            notificationService.createNotification(retour.getEmprunt().getUser(), messageRetour);
            
            Livre livre = retour.getEmprunt().getLivre();
            
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
            
            if (livre != null) {
              
                int empruntedCopies = retour.getEmprunt().getNombreExemplaires(); 
                livre.setAvailableCopies(livre.getAvailableCopies() + empruntedCopies);
                livreService.save(livre);
            }
        }
        return "redirect:/adminDashboard/retours";
    }


    @GetMapping("/adminDashboard/retours/penalites/{id}")
    public String showPenaltieForm(@PathVariable("id") Long retourId, Model model) {
        Retour retour = retourService.findById(retourId);
        if (retour != null) {
            LocalDate dateRetourEffectif = retour.getDateRetourEffectif().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate dateRetourPrevu = retour.getEmprunt().getDateRetourPrevu().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            long joursDeRetard = ChronoUnit.DAYS.between(dateRetourPrevu, dateRetourEffectif);
            model.addAttribute("retour", retour);
            model.addAttribute("joursDeRetard", joursDeRetard);
        }
        return "penalite";
    }

    @GetMapping("/userDashboard/penalites")
    public String listPenalites(@RequestParam(defaultValue = "0") int page, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String cin = userDetails.getCin();
            Pageable pageable = PageRequest.of(page, 4);
            Page<Penalite> penalitesPage = penaliteService.getAllPenaliteByCin(cin, pageable);
            model.addAttribute("penalites", penalitesPage);
            User user = userService.findByCin(cin);
            int unreadCount = notificationService.countUnreadNotifications(user);
            model.addAttribute("unreadCount", unreadCount);
            return "penaliteUserDashboard";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/userDashboard/penalites/details")
    public String showPenaliteDetails(@RequestParam("id") Long id, Model model) {
        Penalite penalite = penaliteService.findById(id);
        model.addAttribute("penalite", penalite);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = userService.findByCin(userDetails.getCin());
            int unreadCount = notificationService.countUnreadNotifications(user);
            model.addAttribute("unreadCount", unreadCount);
        }
        return "penaliteDetails";
    }

    @GetMapping("/adminDashboard/penalites")
    public String listPenalitesAdmin(@RequestParam(defaultValue = "0") int page, Model model) {
        Pageable pageable = PageRequest.of(page, 4);
        Page<Penalite> penalitesPage = penaliteService.getAllPenalite(pageable);
        model.addAttribute("penalites", penalitesPage);
        return "penaliteAdminDashboard";
    }

    @GetMapping("/adminDashboard/penalites/payer")
    public String showPaymentConfirmationForm(@RequestParam("id") Long id, Model model) {
        Penalite penalite = penaliteService.findById(id);
        model.addAttribute("penalite", penalite);
        return "penaliteConfirmPaiement";
    }
    @PostMapping("/adminDashboard/penalites/payer")
    public String processPaymentConfirmation(@ModelAttribute("penalite") Penalite penalite, @RequestParam("montantPaiement") BigDecimal montantPaiement, Model model) {
        if (montantPaiement.compareTo(penalite.getMontant()) >= 0) {
            penalite.setStatutPenalite(StatutPenalite.PAYEE);
            penaliteService.save(penalite);
           
            Retour retour = retourService.findByIdWithAssociations(penalite.getRetour().getId());
        
            User penaliteUser = retour.getEmprunt().getUser();
            
            String penaliteMessage = "Votre pénalité pour le livre " + retour.getEmprunt().getLivre().getTitle() + " de " + penalite.getMontant()  + " a été payée.";
            
            notificationService.createNotification(penaliteUser, penaliteMessage);
           
            return "redirect:/adminDashboard/penalites";
        } else {
            model.addAttribute("errorMessage", "Le montant du paiement ne doit pas être inférieur au montant de la pénalité.");
            return "errorUser";
        }
    }


}
