package com.app.biblio.service;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.biblio.bean.Role;
import com.app.biblio.bean.User;
import com.app.biblio.repository.RoleRepository;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserService userService;

   
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
    public void initializeDefaultRoles() {
        Set<String> defaultRoles = new HashSet<>(Arrays.asList("ROLE_USER", "ROLE_ADMIN"));
        defaultRoles.forEach(roleName -> {
            if (!roleRepository.existsByName(roleName)) {
                Role role = new Role(roleName);
                roleRepository.save(role);
            }
        });


        if (!userService.existsByUsername("issouf")) {
           
            User user = new User();
            user.setUsername("issouf");
            user.setPassword("issouf");
            user.setRoles(new HashSet<>(Arrays.asList(roleRepository.findByName("ROLE_ADMIN"))));
            userService.save(user);
        }
    }
	public Role findById(Long roleId) {
		// TODO Auto-generated method stub
		return roleRepository.getById(roleId);
	}
	
    public Role findByName(String roleName) {
        return roleRepository.findByName(roleName);
    }
    public Set<Role> convertRoleNamesToRoles(Set<String> roleNames) {
        Set<Role> roles = new HashSet<>();
        if (roleNames == null) {
            return roles;
        }
        for (String roleName : roleNames) {
            roles.add(findByName(roleName));
        }
        return roles;
    }

	public Role save(Role adminRole) {
		// TODO Auto-generated method stub
		return roleRepository.save(adminRole);
	}

    
}
