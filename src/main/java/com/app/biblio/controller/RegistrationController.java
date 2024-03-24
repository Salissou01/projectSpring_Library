package com.app.biblio.controller;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.biblio.bean.Role;
import com.app.biblio.bean.User;
import com.app.biblio.service.RoleService;
import com.app.biblio.service.UserService;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register"; 
    }
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, @RequestParam("confirmPassword") String confirmPassword, Model model) {

        if (!user.getPassword().equals(confirmPassword)) {
            
            model.addAttribute("error", "Les mots de passe ne correspondent pas.");
         
            user.setPassword("");
          
            return "register";
        }
        
       
        Role userRole = roleService.findByName("ROLE_USER");
        user.setRoles(Collections.singleton(userRole));
      
        user.setEnabled(true);
       
        userService.save(user);
        
        return "redirect:/login";
    }


    
}
