package com.app.biblio.bean;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserTest {

    private User user;
    private Role role1;
    private Role role2;

    @BeforeEach
    public void setUp() {
        user = new User();
        role1 = new Role();
        role1.setName("ROLE_USER");
        role2 = new Role();
        role2.setName("ROLE_ADMIN");
    }

    @Test
    public void testUsername() {
        String expectedUsername = "testUser";
        user.setUsername(expectedUsername);
        assertEquals(expectedUsername, user.getUsername());
    }

    @Test
    public void testRoles() {
        Set<Role> roles = new HashSet<>();
        roles.add(role1);
        roles.add(role2);

        user.setRoles(roles);

        assertEquals(2, user.getRoles().size());
        assertTrue(user.getRoles().contains(role1));
        assertTrue(user.getRoles().contains(role2));
    }

    }
