package com.app.biblio.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.biblio.bean.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByCIN(String CIN);
	List<User> findByRoles_Name(String roleName);
	boolean existsByUsername(String username);
}
