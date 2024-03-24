package com.app.biblio.controller;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.biblio.bean.CustomUserDetails;
import com.app.biblio.bean.Emprunt;
import com.app.biblio.bean.Livre;
import com.app.biblio.bean.Notification;
import com.app.biblio.bean.StatutEmprunt;
import com.app.biblio.bean.User;
import com.app.biblio.service.EmpruntService;
import com.app.biblio.service.LivreService;
import com.app.biblio.service.NotificationService;

import com.app.biblio.service.RecommendationService;

import com.app.biblio.service.UserService;

@Controller
@RequestMapping("/userDashboard")
public class UserController {

    @Autowired
    private LivreService livreService;
    @Autowired
    private UserService userService;
  
    @Autowired
    private EmpruntService empruntService;
  
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private RecommendationService recommendationService;

 
    @GetMapping("/notifications")
    public String showNotifications(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = userService.findByCin(userDetails.getCin());
            List<Notification> notifications = notificationService.findByUserOrderByCreatedAtDesc(user);
            model.addAttribute("notifications", notifications);
           
            notificationService.markAllAsRead(user);
            
            int unreadCount = notificationService.countUnreadNotifications(user);
            System.out.println("Unread count: " + unreadCount);
            model.addAttribute("unreadCount", unreadCount);
        }
        return "notifications";
    }

    @GetMapping
    public String userDashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
    
        if (auth != null && auth.getAuthorities().stream()
            .anyMatch(role -> role.getAuthority().equals("ROLE_USER"))) {
           
            User user = userService.findByUsername(username);
            model.addAttribute("user", user);
            
            checkForOverdueEmprunts(user, model);
        
            int unreadCount = notificationService.countUnreadNotifications(user);
            model.addAttribute("unreadCount", unreadCount);
        
            List<Livre> livres = livreService.getAllLivres();
            List<List<Livre>> slides = new ArrayList<>();
            for (int i = 0; i < livres.size(); i += 3) {
                slides.add(livres.subList(i, Math.min(i + 3, livres.size())));
            }
            model.addAttribute("slides", slides);
           
            Map<Livre, Double> recommendedBooks = recommendationService.recommendBooksBasedOnRatings();
            
            List<List<Map.Entry<Livre, Double>>> recommendedBooksSlides = new ArrayList<>();
            List<Map.Entry<Livre, Double>> slide = new ArrayList<>();
            int count = 0;
            for (Map.Entry<Livre, Double> entry : recommendedBooks.entrySet()) {
                slide.add(entry);
                count++;
                if (count % 3 == 0) {
                    recommendedBooksSlides.add(slide);
                    slide = new ArrayList<>();
                }
            }
            if (!slide.isEmpty()) {
                recommendedBooksSlides.add(slide);
            }
        
            model.addAttribute("recommendedBooksSlides", recommendedBooksSlides);
            
            return "userDashboard";
        } else {
           
            return "redirect:/error";
        }
    }

    private void checkForOverdueEmprunts(User user, Model model) {
        List<Emprunt> emprunts = empruntService.findByUserAndStatut(user, StatutEmprunt.EMPRUNTE);
        for (Emprunt emprunt : emprunts) {
            String message;
            if (emprunt.getDateRetourPrevu().before(new Date())) {
              
                message = "L'emprunt du livre " + emprunt.getLivre().getTitle() + " est en retard. Veuillez le retourner dès que possible.";
            } else {
               
                long diffInMillies = Math.abs(emprunt.getDateRetourPrevu().getTime() - new Date().getTime());
                long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                message = "Vous avez " + diff + " jours avant la remise du livre " + emprunt.getLivre().getTitle() + ".";
            }
          
            boolean messageExists = notificationService.messageExistsForUser(user, message);
            if (!messageExists) {
                
                notificationService.createNotification(user, message);
            }
        }
    }


    @GetMapping("/livres/image/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) throws FileNotFoundException {
        byte[] imageData = livreService.getImageContentById(id);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageData);
    }
    @GetMapping("/livres/{id}")
    public String livreDetails(@PathVariable Long id, Model model) {
        Livre livre = livreService.findById(id); 
        model.addAttribute("livre", livre);
        return "userLivreDetails"; 
    }
    @PostMapping("/notifications/delete/{id}")
    public String deleteNotification(@PathVariable Long id) {
        notificationService.deleteById(id);
      
        return "redirect:/userDashboard/notifications";
    }

    @GetMapping("/error")
    public String error(@RequestParam(value = "errorMessage", required = false) String errorMessage, Model model) {
      
        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
        }
        
        return "errorUser"; 
    }

    
}
