package com.app.biblio.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.app.biblio.bean.Notification;
import com.app.biblio.bean.User;
import com.app.biblio.repository.NotificationRepository;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceUnitTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @Test
    public void testCreateNotification() {
        // Given
        User user = new User();
        

        String message = "Test message";

        Notification expectedNotification = new Notification();
        expectedNotification.setUser(user);
        expectedNotification.setMessage(message);
        
        when(notificationRepository.save(any(Notification.class))).thenReturn(expectedNotification);

        // When
        Notification actualNotification = notificationService.createNotification(user, message);

        // Then
        assertThat(actualNotification).isEqualTo(expectedNotification);
        
    }
}
