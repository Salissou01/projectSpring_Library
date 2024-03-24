package com.app.biblio.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.app.biblio.bean.Notification;
import com.app.biblio.bean.User;
import com.app.biblio.repository.NotificationRepository;
import com.app.biblio.repository.UserRepository; // Assurez-vous d'importer UserRepository

@SpringBootTest
@ActiveProfiles("test")
public class NotificationServiceImplIntegrationTest {

    @Autowired
    private NotificationServiceImpl notificationService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository; // Injectez UserRepository

    @Test
    public void testCreateNotification() {
        // Setup
        User user = new User();
        // Ajoutez ici la logique pour initialiser user si nécessaire
        user = userRepository.save(user); // Sauvegardez l'utilisateur avant de créer la notification

        String message = "Test message";

        // Execute
        Notification notification = notificationService.createNotification(user, message);

        // Verify
        assertNotNull(notification);
        assertThat(notification.getMessage()).isEqualTo(message);
        assertThat(notification.getUser()).isEqualTo(user);

        // Vous pouvez ajouter des assertions supplémentaires pour vérifier d'autres attributs de notification
    }
}
