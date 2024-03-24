package com.app.biblio.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@Profile("test")
@Order(102)
public class TestSecurityConfig extends WebSecurityConfigurerAdapter {

    public TestSecurityConfig() {
        System.out.println("TestSecurityConfig loaded"); 
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        .authorizeRequests(authorize -> authorize
                        .antMatchers("/**").permitAll())
                		
                .csrf(csrf -> csrf.disable());
    }
}