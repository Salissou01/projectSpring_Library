package com.app.biblio.restController;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.biblio.bean.Notification;
import com.app.biblio.bean.User;
import com.app.biblio.service.NotificationService;
import com.app.biblio.service.UserService;

@RestController
@RequestMapping("/api/notifications")
public class NotificationRestController {

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications() {
        List<Notification> notifications = notificationService.findAll();
        return ResponseEntity.ok(notifications);
    }
    @GetMapping("/unread")
    public ResponseEntity<List<Notification>> getAllUnreadNotifications() {
        List<Notification> notifications = notificationService.findAllUnreadNotifications();
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/read")
    public ResponseEntity<List<Notification>> getAllReadNotifications() {
        List<Notification> notifications = notificationService.findAllReadNotifications();
        return ResponseEntity.ok(notifications);
    }

    
    @GetMapping("/{cin}")
    public ResponseEntity<List<Notification>> getAllNotificationsByCin(@PathVariable String cin) {
        User user = userService.findByCin(cin); 
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        List<Notification> notifications = notificationService.findByUserOrderByCreatedAtDesc(user);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/{cin}/unread")
    public ResponseEntity<List<Notification>> getUnreadNotificationsByCin(@PathVariable String cin) {
        User user = userService.findByCin(cin); 
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        List<Notification> notifications = notificationService.findUnreadNotifications(user);
        return ResponseEntity.ok(notifications);
    }


    @GetMapping("/{cin}/read")
    public ResponseEntity<List<Notification>> getReadNotificationsByCin(@PathVariable String cin) {
        User user = userService.findByCin(cin); 
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        List<Notification> notifications = notificationService.findReadNotifications(user);
        return ResponseEntity.ok(notifications);
    }
    @DeleteMapping("/delete/{notificationId}")
    public ResponseEntity<?> deleteNotification(@PathVariable Long notificationId) {
        Optional<Notification> notificationOptional = notificationService.findById(notificationId);
        if (notificationOptional.isPresent()) {
            notificationService.deleteById(notificationId);
            return ResponseEntity.ok("La notification a été supprimée avec succès.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La notification avec l'ID " + notificationId + " n'existe pas.");
        }
    }


    @DeleteMapping("/delete/{cin}/{notificationId}")
    public ResponseEntity<?> deleteNotificationByCin(@PathVariable String cin, @PathVariable Long notificationId) {
        User user = userService.findByCin(cin); 
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        List<Notification> notifications = notificationService.findByUserOrderByCreatedAtDesc(user);
        Optional<Notification> notificationToDelete = notifications.stream()
                .filter(notification -> notification.getId().equals(notificationId))
                .findFirst();

        if (notificationToDelete.isPresent()) {
            notificationService.deleteById(notificationId);
            return ResponseEntity.ok("La notification a été supprimée avec succès.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La notification avec l'ID " + notificationId + " n'existe pas pour cet utilisateur.");
        }
    }

}
