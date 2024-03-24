package com.app.biblio.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.app.biblio.bean.User;
import com.app.biblio.repository.UserRepository;
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindById() {
        // Setup
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Execute
        User foundUser = userService.findById(userId);

        // Verify
        assertNotNull(foundUser);
        assertEquals(userId, foundUser.getId());
    }

    @Test
    public void testFindByUsername() {
        // Setup
        String username = "testUser";
        User user = new User();
        user.setUsername(username);
        when(userRepository.findByUsername(username)).thenReturn(user);

        // Execute
        User foundUser = userService.findByUsername(username);

        // Verify
        assertNotNull(foundUser);
        assertEquals(username, foundUser.getUsername());
    }

    @Test
    public void testFindAll() {
        // Setup
        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());
        when(userRepository.findAll()).thenReturn(users);

        // Execute
        List<User> foundUsers = userService.findAll();

        // Verify
        assertNotNull(foundUsers);
        assertEquals(users.size(), foundUsers.size());
    }
    @Test
    public void testSave() {
        // Setup
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("testPassword");

        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);

        // Execute
        User savedUser = userService.save(user);

        // Verify
        assertNotNull(savedUser);
        assertEquals("testUser", savedUser.getUsername());
        assertEquals("encodedPassword", savedUser.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testDeleteById() {
       
        Long userId = 1L;

        
        userService.deleteById(userId);

       
        verify(userRepository, times(1)).deleteById(userId);
    }
  
}
