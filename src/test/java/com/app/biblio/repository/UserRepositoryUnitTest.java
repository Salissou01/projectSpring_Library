package com.app.biblio.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.app.biblio.bean.User;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryUnitTest {

    @Mock
    private UserRepository userRepository;

    @Test
    public void testFindByUsername() {
        // Given
        String username = "testUser";
        User expectedUser = new User();
        expectedUser.setUsername(username);
        // Ajoutez ici la logique pour initialiser expectedUser si nécessaire

        when(userRepository.findByUsername(username)).thenReturn(expectedUser);

        // When
        User actualUser = userRepository.findByUsername(username);

        // Then
        assertThat(actualUser).isEqualTo(expectedUser);
    }
}
