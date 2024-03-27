package com.app.biblio.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.app.biblio.bean.Category;
import com.app.biblio.service.CategoryService;
import com.app.biblio.service.RoleService;

@Configuration
public class InitializationConfiguration {

    @Autowired
    private RoleService roleService;
    @Autowired
    private CategoryService categoryService;
    @Bean
    public CommandLineRunner init() {
        return args -> {
            roleService.initializeDefaultRoles();
            categoryService.initializeDefaultCategories();
        };
    }
    
   
}
