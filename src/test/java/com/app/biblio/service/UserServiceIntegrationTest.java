package com.app.biblio.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.app.biblio.bean.User;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceIntegrationTest {

    @Autowired
    private UserServiceImpl userService;

    @Test
    public void testFindById() {
        // Setup
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("testPassword");
        userService.save(user);

        // Execute
        User foundUser = userService.findById(user.getId());

        // Verify
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getId()).isEqualTo(user.getId());
    }
    
    @Test
    public void testUpdateUser() {
        // Setup
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("testPassword");
        userService.save(user); // Sauvegardez l'utilisateur pour obtenir un ID

        // Mise à jour des informations de l'utilisateur
        user.setUsername("updatedUsername");
        user.setPassword("updatedPassword");
        user.setEmail("updated@example.com");
        user.setTelephone("1234567890");
        // Ajoutez ici la logique pour initialiser d'autres attributs si nécessaire

        // Execute
        User updatedUser = userService.update(user);

        // Verify
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getUsername()).isEqualTo("updatedUsername");
        assertThat(updatedUser.getEmail()).isEqualTo("updated@example.com");
        assertThat(updatedUser.getTelephone()).isEqualTo("1234567890");
    }
}
