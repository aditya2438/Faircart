package com.faircart.service;

import com.faircart.dto.category.CategoryRequest;
import com.faircart.dto.category.CategoryResponse;
import com.faircart.entity.Category;
import com.faircart.exception.ResourceNotFoundException;
import com.faircart.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryResponse createCategory(CategoryRequest request) {
        // Generate slug from name
        String slug = generateSlug(request.getName());
        
        // Check if slug already exists
        if (categoryRepository.existsBySlug(slug)) {
            throw new IllegalArgumentException("Category with similar name already exists");
        }

        Category category = Category.builder()
                .name(request.getName())
                .slug(slug)
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .displayOrder(request.getDisplayOrder())
                .active(true)
                .build();

        Category savedCategory = categoryRepository.save(category);
        return CategoryResponse.from(savedCategory);
    }

    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        String newSlug = generateSlug(request.getName());
        if (!category.getSlug().equals(newSlug) && categoryRepository.existsBySlug(newSlug)) {
            throw new IllegalArgumentException("Category with similar name already exists");
        }

        category.setName(request.getName());
        category.setSlug(newSlug);
        category.setDescription(request.getDescription());
        category.setImageUrl(request.getImageUrl());
        category.setDisplayOrder(request.getDisplayOrder());

        Category savedCategory = categoryRepository.save(category);
        return CategoryResponse.from(savedCategory);
    }

    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        return CategoryResponse.from(category);
    }

    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return CategoryResponse.from(categories);
    }

    public List<CategoryResponse> getActiveCategories() {
        List<Category> categories = categoryRepository.findByActiveTrueOrderByDisplayOrderAsc();
        return CategoryResponse.from(categories);
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        
        // Soft delete - mark as inactive
        category.setActive(false);
        categoryRepository.save(category);
    }

    @Transactional
    public void activateCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        category.setActive(true);
        categoryRepository.save(category);
    }

    private String generateSlug(String name) {
        return name.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .trim();
    }
}