package com.app.biblio.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.biblio.bean.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

	boolean existsByName(String categoryName);
    
}
