package com.app.biblio.restController;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.app.biblio.bean.Role;
import com.app.biblio.bean.User;
import com.app.biblio.service.RoleService;
import com.app.biblio.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class UserRestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private UserRestController userRestController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userRestController).build();
    }

    @Test
    public void testCreateUser() throws Exception {
        // Préparation des données
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("testPassword");
        user.setEmail("test@example.com");
        user.setTelephone("1234567890");
        user.setCIN("123456789");
        user.setEnabled(true);

        Role defaultRole = new Role();
        defaultRole.setName("ROLE_USER");

        // Configuration des mocks
        when(roleService.findByName("ROLE_USER")).thenReturn(defaultRole);
        when(userService.save(any(User.class))).thenReturn(user);

        // Exécution de la requête POST
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(content().string("User created successfully"));
    }
}
