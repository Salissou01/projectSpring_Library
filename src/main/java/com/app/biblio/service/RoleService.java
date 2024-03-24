package com.app.biblio.service;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.biblio.bean.Role;
import com.app.biblio.repository.RoleRepository;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

   
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
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
