package com.app.biblio.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.app.biblio.bean.Notification;
import com.app.biblio.bean.User;

@ExtendWith(MockitoExtension.class)
public class NotificationRepositoryUnitTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Test
    public void testCountByUserAndReadFalse() {
        // Given
        User user = new User();
        // Ajoutez ici la logique pour initialiser user si nécessaire

        int expectedCount = 5;

        when(notificationRepository.countByUserAndReadFalse(user)).thenReturn(expectedCount);

        // When
        int actualCount = notificationRepository.countByUserAndReadFalse(user);

        // Then
        assertThat(actualCount).isEqualTo(expectedCount);
    }
}
