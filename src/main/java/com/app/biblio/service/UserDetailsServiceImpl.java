package com.app.biblio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;

import com.app.biblio.bean.CustomUserDetails;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private JdbcTemplate jdbcTemplate;



    @SuppressWarnings("deprecation")

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String sql = "SELECT * FROM t_users WHERE username = ?";
        List<UserDetails> users = jdbcTemplate.query(sql, new Object[]{username}, (rs, rowNum) -> {
            Long userId = rs.getLong("id");
            String password = rs.getString("password");
            boolean enabled = rs.getBoolean("enabled"); 
            String email = rs.getString("email"); 
            String telephone = rs.getString("telephone"); 
            String cin = rs.getString("cin"); 
            
            Set<GrantedAuthority> authorities = new HashSet<>();
            
          
            String rolesQuery = "SELECT r.name FROM role r JOIN user_roles ur ON r.id = ur.role_id WHERE ur.user_id = ?";
            jdbcTemplate.query(rolesQuery, new Object[]{userId}, (rsRoles, rowNumRoles) -> {
                String roleName = rsRoles.getString("name");
                authorities.add(new SimpleGrantedAuthority(roleName));
                return null;
            });
            
            return new CustomUserDetails(
            	
                username,
                password,
                authorities,
                email,
                telephone,
                cin,
                enabled,
                userId
            );
        });

        if (users.isEmpty()) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return users.get(0);
    }




}
