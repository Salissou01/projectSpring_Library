package com.app.biblio.restController;


import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.biblio.bean.CustomUserDetails;
import com.app.biblio.bean.Role;
import com.app.biblio.bean.User;
import com.app.biblio.bean.UserDTO;

import com.app.biblio.service.RoleService;

import com.app.biblio.service.UserService;

@RestController
@RequestMapping("/api")

public class UserRestController {

    @Autowired
    private UserService userService;
 
    @Autowired
    private RoleService roleService;
   
    @GetMapping("/user/info")
    public CustomUserDetails getUserInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return userDetails;
    }

 
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

   
    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        User user = userService.findById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

  
    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody User user) {
    	
        if (user.getUsername() == null || user.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username and password are required");
        }
       
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            
            Role defaultRole = roleService.findByName("ROLE_USER"); 
            if (defaultRole != null) {
              
                user.setRoles(Collections.singleton(defaultRole)); 
            } else {
                
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Default role not found");
            }
        }
        
        userService.save(user);
        
        
        return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
    }


    @PutMapping("user/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        User user = userService.findById(id);
        if (user == null) {
        	  return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        user.setTelephone(userDTO.getTelephone());
        user.setCIN(userDTO.getCIN());
        user.setEnabled(userDTO.isEnabled());
      
        user.setRoles(roleService.convertRoleNamesToRoles(userDTO.getRoles()));

        userService.save(user);
        return ResponseEntity.ok("L'utilisateur a été mis à jour avec succès.");
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        User existingUser = userService.findById(id);
        if (existingUser != null) {
            userService.deleteById(id);
            return ResponseEntity.ok("User deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }


}
