package com.app.biblio.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.biblio.bean.CustomUserDetails;
import com.app.biblio.bean.Emprunt;
import com.app.biblio.bean.Livre;

import com.app.biblio.bean.User;
import com.app.biblio.service.EmpruntService;
import com.app.biblio.service.LivreService;
import com.app.biblio.service.NotificationService;
import com.app.biblio.service.PenaliteService;
import com.app.biblio.service.UserService;

@Controller
@RequestMapping("/userDashboard")
public class EmpruntController {

    @Autowired
    private EmpruntService empruntService;
    @Autowired
    private PenaliteService penaliteService;
    @Autowired
    private LivreService livreService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserService userService;
 @GetMapping("/emprunts")
    public String listEmprunts(@RequestParam(defaultValue = "0") int page, @RequestParam(required = false) Integer unreadCount, Model model) {
        Pageable pageable = PageRequest.of(page, 4); 
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String cin = userDetails.getCin(); 
            Page<Emprunt> empruntsPage = empruntService.getAllEmpruntByCin(cin, pageable);
            model.addAttribute("emprunts", empruntsPage);
            
         
            if (unreadCount == null) {
                User user = userService.findByCin(cin); 
                unreadCount = notificationService.countUnreadNotifications(user);
            }
            model.addAttribute("unreadCount", unreadCount);
            return "empruntUserDashboard";
        } else {
            
            return "redirect:/login";
        }
    }
   
    @GetMapping("/emprunts/add")
    public String showAddEmpruntForm(@RequestParam("livreId") Long livreId, Model model) {
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
                return "emprunt";
            } else {
                
                return "redirect:/login";
            }
        } else {
           
            return "redirect:/login";
        }
    }


    @PostMapping("/emprunts/add")
    public String addEmprunt(@RequestParam("livreId") Long livreId, @RequestParam("dateEmprunt") String dateEmpruntStr, @RequestParam("dateRetourPrevu") String dateRetourPrevuStr, @RequestParam("nombreExemplaires") int nombreExemplaires, Model model) throws ParseException {
        
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
                Livre livre = livreService.findById(livreId);

                
                if (nombreExemplaires > livre.getAvailableCopies()) {
                    model.addAttribute("errorMessage", "Le nombre d'exemplaires demandé est supérieur au nombre de copies disponibles.");
                    return "errorUser";
                }

                
                Emprunt emprunt = new Emprunt();
                emprunt.setDateEmprunt(dateEmprunt);
                emprunt.setDateRetourPrevu(dateRetourPrevu);
                emprunt.setUser(user);
                emprunt.setLivre(livre); 
                emprunt.setNombreExemplaires(nombreExemplaires); 
               
                empruntService.save(emprunt);
                
                //livre.setAvailableCopies(livre.getAvailableCopies() - nombreExemplaires);
                //livreService.save(livre);

             
                String message = "Votre demande d'emprunt pour le livre " + livre.getTitle() + "a été reçue et est en attente de validation.";
                notificationService.createNotification(user, message);
           
                int unreadCount = notificationService.countUnreadNotifications(user);
                System.out.println("Unread count after adding emprunt: " + unreadCount);

               
                return "redirect:/userDashboard/emprunts?unreadCount=" + unreadCount;
            } else {
               
                return "redirect:/login";
            }
        } else {
           
            return "redirect:/login";
        }
    }


}
