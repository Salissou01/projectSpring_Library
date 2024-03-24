package com.app.biblio.restController;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.app.biblio.bean.Role;
import com.app.biblio.bean.User;
import com.app.biblio.security.JwtProvider;

import com.app.biblio.security.LoginRequest;
import com.app.biblio.service.RoleService;
import com.app.biblio.service.UserDetailsServiceImpl;
import com.app.biblio.service.UserService;

@ExtendWith(MockitoExtension.class)
public class UserRestControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private UserRestController userRestController;
    @Mock
    private UserDetailsServiceImpl userDetailsService;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtProvider jwtProvider;

    private User testUser;
    private Role testRole;

    @BeforeEach
    public void setup() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testUser");
        testUser.setPassword("testPassword");
        testUser.setEnabled(true);
        testRole = new Role();
        testRole.setId(1L);
        testRole.setName("ROLE_USER");
    }

    @Test
    public void testGetAllUsers() {
        // Setup
        List<User> users = Arrays.asList(testUser);
        when(userService.findAll()).thenReturn(users);

        // Execute
        ResponseEntity<?> response = userRestController.getAllUsers();

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
    }

    @Test
    public void testGetUserById() {
        // Setup
        when(userService.findById(1L)).thenReturn(testUser);

        // Execute
        ResponseEntity<?> response = userRestController.getUserById(1L);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testUser, response.getBody()); 
    }

    @Test
    public void testCreateUser() {
        // Setup
        when(roleService.findByName("ROLE_USER")).thenReturn(testRole);
        when(userService.save(any(User.class))).thenReturn(testUser);

        // Execute
        ResponseEntity<?> response = userRestController.createUser(testUser); 

        // Verify
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("User created successfully", response.getBody());
    }
    
   

    @Test
    public void testDeleteUser() {
        // Setup
        when(userService.findById(anyLong())).thenReturn(testUser);

        // Execute
        ResponseEntity<?> response = userRestController.deleteUser(1L);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User deleted successfully", response.getBody());
    }
    
    
   /* @Test
    public void testAuthenticateUserWithValidCredentials() {
 
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testUser");
        loginRequest.setPassword("testPassword");

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
            "testUser", 
            "testPassword", 
            new ArrayList<>() 
        );

        when(userDetailsService.loadUserByUsername("testUser")).thenReturn(userDetails);
        when(passwordEncoder.matches("testPassword", userDetails.getPassword())).thenReturn(true);

        ResponseEntity<?> response = userRestController.authenticateUser(loginRequest);

     
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    */
    

    // Add more tests for other methods as needed
}
