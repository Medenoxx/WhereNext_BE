package com.example.wherenextbackend.controller;

import com.example.wherenextbackend.dto.CategoryRequestDTO;
import com.example.wherenextbackend.dto.CategoryResponseDTO;
import com.example.wherenextbackend.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/categories")
@RequiredArgsConstructor

public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public CategoryResponseDTO addCategory(@RequestBody CategoryRequestDTO categoryRequestDTO) {
        return categoryService.addCategory(categoryRequestDTO);
    }

    @GetMapping
    public List<CategoryResponseDTO> getAllCategories() {
        return categoryService.getAllCategories();
    }

}