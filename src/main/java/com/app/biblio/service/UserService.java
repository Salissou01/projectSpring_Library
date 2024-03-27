package com.app.biblio.service;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import com.app.biblio.bean.User;

public interface UserService {

    User findById(Long id);

    User findByUsername(String username);

    List<User> findAll();

    User save(User user);
    User update(User user);

    void deleteById(Long id);
    Page<User> getAllUsers(Pageable pageable);

	long count();

	User findByCin(String cin);

	List<User> getUsersByRole(String roleName);

	boolean existsByUsername(String username);
    
}
