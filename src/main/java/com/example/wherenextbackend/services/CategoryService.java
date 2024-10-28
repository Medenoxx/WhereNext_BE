package com.example.wherenextbackend.services;

import com.example.wherenextbackend.dto.CategoryRequestDTO;
import com.example.wherenextbackend.dto.CategoryResponseDTO;
import com.example.wherenextbackend.entity.Category;
import com.example.wherenextbackend.repository.CategoryCrudRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor

public class CategoryService {

    private final CategoryCrudRepo categoryCrudRepo;
    private final ConversionService conversionService;

    public CategoryResponseDTO convertCategoryToCategoryResponseDTO(Category category) {
        return new CategoryResponseDTO(category.getCategoryId(), category.getName(), category.getColor());
    }

    public CategoryResponseDTO addCategory(CategoryRequestDTO categoryRequestDTO) {
        Category category = new Category();
        category.setName(categoryRequestDTO.getName());
        category.setColor(categoryRequestDTO.getColor());
        categoryCrudRepo.save(category);
        return convertCategoryToCategoryResponseDTO(category);
    }

    public Category getCategoryById(Integer categoryId) {
        return conversionService.getEntityFromOptional(categoryCrudRepo.findById(categoryId));
    }

    public List<CategoryResponseDTO> getAllCategories() {
        List<Category> categories = categoryCrudRepo.findAll();
        List<CategoryResponseDTO> categoriesResponseDTO = new ArrayList<>();
        for (Category category : categories) {
            categoriesResponseDTO.add(convertCategoryToCategoryResponseDTO(category));
        }
        return categoriesResponseDTO;
    }
}

