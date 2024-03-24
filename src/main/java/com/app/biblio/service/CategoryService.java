package com.app.biblio.service;
import java.util.List;

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

    }
