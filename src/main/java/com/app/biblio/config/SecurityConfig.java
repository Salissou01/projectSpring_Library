/*package com.app.biblio.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.app.biblio.security.JwtAuthenticationFilter;
import com.app.biblio.security.JwtProvider;
import com.app.biblio.service.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.SerializationFeature;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)

public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> {
            builder.featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        };
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeRequests(authorize -> authorize
                .antMatchers("/public/**", "/register", "/api/login", "/css/**", "/js/**", "/images/**","/errorUser").permitAll()
                .antMatchers("/adminDashboard/**", "/api/users/**","/api/livres/add","/api/livres/edit/**","/api/livres/delete/**").hasRole("ADMIN")
                .antMatchers("/userDashboard/**").hasRole("USER")
                
                .antMatchers("/userDashboard/emprunts/add","/api/emprunts/add","/userDashboard/reservations/**").authenticated()
                .antMatchers("/api/emprunts","/api/emprunts/delete/{id}","/api/emprunts/{id}/validate","/api/emprunts/edit/{id}","/api/emprunts/{id}/rejet","/api/retours","/api/retours/validate/**",
                		"/api/retours/penalize/**","/api/reservations/validate/**","/api/reservations/cancel/**","/api/penalites/payer/**","/api/penalites",
                		"/api/reservations","/api/reservations/delete/**","/api/notifications","/api/notifications/read","/api/notifications/unread","/api/notifications/delete/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(login -> login
                .loginPage("/login")
                .successHandler(new CustomAuthenticationSuccessHandler())
                .permitAll())
            .logout(logout -> logout
                .permitAll())
            .exceptionHandling(exceptionHandling -> exceptionHandling
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                  
                }))
            .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);
    }

    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }
}*/

package com.app.biblio.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.app.biblio.security.JwtAuthenticationFilter;
import com.app.biblio.security.JwtProvider;
import com.app.biblio.service.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.SerializationFeature;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> builder.featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeRequests(authorize -> authorize
                .antMatchers(publicPaths()).permitAll()
                .antMatchers(adminPaths()).hasRole("ADMIN")
                .antMatchers(userPaths()).hasRole("USER")
                .anyRequest().authenticated()
            )
            .formLogin(login -> login
                .loginPage("/login")
                .successHandler(new CustomAuthenticationSuccessHandler())
                .permitAll())
            .logout(logout -> logout.permitAll())
            .exceptionHandling(exceptionHandling -> exceptionHandling
                .accessDeniedHandler((request, response, accessDeniedException) -> {}))
            .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);
    }

    private String[] publicPaths() {
        return new String[]{"/public/**", "/register", "/api/login", "/css/**", "/js/**", "/images/**", "/errorUser"};
    }

    private String[] adminPaths() {
        return new String[]{"/adminDashboard/**", "/api/users/**", "/api/livres/add", "/api/livres/edit/**", "/api/livres/delete/**",
        		"/api/emprunts", "/api/emprunts/delete/{id}", "/api/emprunts/{id}/validate", "/api/emprunts/edit/{id}", "/api/emprunts/{id}/rejet",
                "/api/retours", "/api/retours/validate/**", "/api/retours/penalize/**", "/api/reservations/validate/**",
                "/api/reservations/cancel/**", "/api/penalites/payer/**", "/api/penalites", "/api/reservations",
                "/api/reservations/delete/**", "/api/notifications", "/api/notifications/read", "/api/notifications/unread", "/api/notifications/delete/**"
                };
    }

    private String[] userPaths() {
        return new String[]{"/userDashboard/**"};
    }

 

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }
}

