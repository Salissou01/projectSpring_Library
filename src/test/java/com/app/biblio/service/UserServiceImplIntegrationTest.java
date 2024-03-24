package com.app.biblio.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.app.biblio.bean.Role;
import com.app.biblio.bean.User;
import com.app.biblio.repository.UserRepository;


@SpringBootTest
@ActiveProfiles("test")
public class UserServiceImplIntegrationTest {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserRepository userRepository;
    
    

 

    @Test
    public void testSave() {
        // Setup
        User user = new User();
        user.setUsername("admin");
        user.setPassword("password");

        // Create ROLE_ADMIN
        Role adminRole = new Role();
        adminRole.setName("ROLE_ADMIN");

        // Save the Role entity
        Role savedRole = roleService.save(adminRole);

        // Associate the saved Role with the user
        Set<Role> roles = new HashSet<>();
        roles.add(savedRole);
        user.setRoles(roles);

        // Execute
        User savedUser = userService.save(user);

        // Verify
        assertNotNull(savedUser.getId());
        assertEquals("admin", savedUser.getUsername());
        assertEquals(1, savedUser.getRoles().size());
        assertEquals("ROLE_ADMIN", savedUser.getRoles().iterator().next().getName());
    }

    @Test
    public void testDeleteById() {
        // Setup
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("testPassword");
        User savedUser = userService.save(user);
        Long userId = savedUser.getId();

        // Execute
        userService.deleteById(userId);

        // Verify
        User deletedUser = userRepository.findById(userId).orElse(null);
        assertNull(deletedUser);
    }
    @Test
    public void testUpdate() {
        // Setup
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("testPassword");
        user.setEmail("test@example.com");
        user.setTelephone("1234567890");
        user.setCIN("123456789");
        user.setEnabled(true);
        User savedUser = userService.save(user);

        // Modify user
        savedUser.setEmail("updated@example.com");
        savedUser.setTelephone("0987654321");
        savedUser.setCIN("987654321");
        savedUser.setEnabled(false);

        // Execute
        User updatedUser = userService.update(savedUser);

        // Verify
        assertNotNull(updatedUser.getId());
        assertEquals("updated@example.com", updatedUser.getEmail());
        assertEquals("0987654321", updatedUser.getTelephone());
        assertEquals("987654321", updatedUser.getCIN());
        assertFalse(updatedUser.isEnabled());
    }

}
