package com.app.biblio.controller;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class LoginController {
	

    @GetMapping("/login")
    public String loginPage() {
        return "login"; 
    }

    @GetMapping("/adminDashboard")
    public String adminDashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
       
        model.addAttribute("userFullName", username); 
       
        if (auth != null && auth.getAuthorities().stream()
            .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"))) {
        	
            return "adminDashboard";
        } else {
           
            return "redirect:/error";
        }
    }
  

}

