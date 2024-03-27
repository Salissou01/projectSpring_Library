package com.app.biblio.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.biblio.bean.User;
import com.app.biblio.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository; 

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username); 
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
@Override
public List<User> getUsersByRole(String roleName) {
    return userRepository.findByRoles_Name(roleName);
}

    @Override
    public User save(User user) {
    	 user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User update(User user) {
       
        User existingUser = userRepository.findById(user.getId()).orElse(null);

      
        if (existingUser != null) {
        	existingUser.setCIN(user.getCIN());
            existingUser.setUsername(user.getUsername());
            
            existingUser.setEmail(user.getEmail());
            existingUser.setTelephone(user.getTelephone());
            
         
            if (!existingUser.getRoles().equals(user.getRoles())) {
           
                existingUser.setRoles(user.getRoles());
            }
           
            if (!user.getPassword().isEmpty()) {
               
                existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            existingUser.setEnabled(user.isEnabled());
            return userRepository.save(existingUser);
        } else {
            
            return null;
        }
    }
    @Override
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public long count() {
        return userRepository.count();
    }

	@Override
	public User findByCin(String cin) {
		 return userRepository.findByCIN(cin); 
	}

}
