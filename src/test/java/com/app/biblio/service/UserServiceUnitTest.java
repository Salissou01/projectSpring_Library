package com.app.biblio.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.app.biblio.bean.User;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {

    @Mock
    private UserService userService;

    @Test
    public void testFindById() {
        // Given
        Long id = 1L;
        User expectedUser = new User();
        expectedUser.setId(id);
        

        when(userService.findById(id)).thenReturn(expectedUser);

        // When
        User actualUser = userService.findById(id);

        // Then
        assertThat(actualUser).isEqualTo(expectedUser);
    }
}
