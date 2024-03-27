package com.app.biblio.service;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.biblio.bean.Category;
import com.app.biblio.repository.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

	public Category findById(Long categoryId) {
		return categoryRepository.getById(categoryId);
	}
	 public void initializeDefaultCategories() {
	        Set<String> defaultCategories = new HashSet<>(Arrays.asList("Roman", "Littérature", "Science-fiction", "Biographie", "Histoire", "Philosophie", "Art", "Sciences", "Technologie"));
	        defaultCategories.forEach(categoryName -> {
	            if (!categoryRepository.existsByName(categoryName)) {
	                Category category = new Category(categoryName, "Description de " + categoryName);
	                categoryRepository.save(category);
	            }
	        });
	    }

    }
