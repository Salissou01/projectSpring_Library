package com.app.biblio.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.biblio.bean.CustomUserDetails;
import com.app.biblio.bean.Emprunt;
import com.app.biblio.bean.Retour;
import com.app.biblio.bean.StatutEmprunt;
import com.app.biblio.bean.StatutRetour;
import com.app.biblio.bean.User;
import com.app.biblio.service.EmpruntService;
import com.app.biblio.service.NotificationService;
import com.app.biblio.service.RetourService;
import com.app.biblio.service.UserService;

@Controller
public class RetourController {

    @Autowired
    private EmpruntService empruntService;
    @Autowired
    private UserService userService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private RetourService retourService;
   
    @GetMapping("/userDashboard/emprunts/return/{id}")
    public String showReturnPage(@PathVariable("id") Long empruntId, Model model) {
    
        Emprunt emprunt = empruntService.findById(empruntId);
        
        model.addAttribute("emprunt", emprunt);
       
        int unreadCount = notificationService.countUnreadNotifications(emprunt.getUser());
        model.addAttribute("unreadCount", unreadCount);
        
        return "retour";
    }
    @GetMapping("/userDashboard/retours")
    public String listRetours(@RequestParam(defaultValue = "0") int page, Model model) {
        Pageable pageable = PageRequest.of(page, 4);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String cin = userDetails.getCin(); 
            Page<Retour> retoursPage = retourService.getAllRetourByCin(cin, pageable);
            model.addAttribute("retours", retoursPage);
         
            User user = userService.findByCin(cin); 
            int unreadCount = notificationService.countUnreadNotifications(user);
            model.addAttribute("unreadCount", unreadCount);
            
            return "retourUserDashboard";
        } else {
            
            return "redirect:/login";
        }
    }

    @PostMapping("/userDashboard/emprunts/return/{id}")
    public String handleReturn(@PathVariable("id") Long empruntId,
                               @RequestParam("dateRetourEffectif")@DateTimeFormat(pattern = "yyyy-MM-dd")  Date dateRetourEffectif,
                               @RequestParam("note") Integer note) {
        Emprunt emprunt = empruntService.findById(empruntId);
        Retour retour = new Retour();
        retour.setEmprunt(emprunt);
        retour.setDateRetourEffectif(dateRetourEffectif);
        retour.setNote(note);
        retour.setStatutRetour(StatutRetour.EN_ATTENTE); 
        retourService.save(retour);
        emprunt.setStatut(StatutEmprunt.RETOURNE);
        empruntService.save(emprunt);
    
        String message = "Le livre " + emprunt.getLivre().getTitle() + " a été retourné.";
        notificationService.createNotification(emprunt.getUser(), message);

        
        int unreadCount = notificationService.countUnreadNotifications(emprunt.getUser());
        

        return "redirect:/userDashboard/emprunts?unreadCount=" + unreadCount;

    }
}
