package com.app.biblio.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.app.biblio.bean.Notification;
import com.app.biblio.bean.User;
import com.app.biblio.repository.UserRepository;

@SpringBootTest
@ActiveProfiles("test")
public class NotificationRepositoryIntegrationTest {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByUserOrderByCreatedAtDesc() {
        // Setup
        User user = new User();
        user.setUsername("testUser");
        user.setEmail("test@example.com");
        // Ajoutez ici d'autres attributs nécessaires pour initialiser user
        user = userRepository.save(user);

        // Création et sauvegarde de notifications pour cet utilisateur
        Notification notification1 = new Notification();
        notification1.setUser(user);
        notification1.setMessage("Notification 1");
        notification1.setCreatedAt(LocalDateTime.now().minusHours(1));
        notificationRepository.save(notification1);

        Notification notification2 = new Notification();
        notification2.setUser(user);
        notification2.setMessage("Notification 2");
        notification2.setCreatedAt(LocalDateTime.now());
        notificationRepository.save(notification2);

        // Execute
        List<Notification> notifications = notificationRepository.findByUserOrderByCreatedAtDesc(user);

        // Verify
        assertThat(notifications).isNotEmpty();
        assertThat(notifications.get(0).getCreatedAt()).isAfterOrEqualTo(notifications.get(1).getCreatedAt());
        // Vous pouvez ajouter des assertions supplémentaires pour vérifier l'ordre des notifications
    }
}
