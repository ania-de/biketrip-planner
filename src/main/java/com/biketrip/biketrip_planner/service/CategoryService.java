package com.biketrip.biketrip_planner.service;

import com.biketrip.biketrip_planner.classes.Category;
import com.biketrip.biketrip_planner.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    public Category save(Category category) {
        return categoryRepository.save(category);
    }
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }
    public Optional<Category> findByName(String name) {
        return categoryRepository.findByName(name);
    }
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }
    public Category updateDescription (Long id, String newDescription) {
        Category category = categoryRepository.findById(id).orElseThrow();
        category.setDescription(newDescription);
        return categoryRepository.save(category);
    }
}
