package com.example.wherenextbackend.repository;

import com.example.wherenextbackend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface CategoryCrudRepo extends JpaRepository<Category, Integer> {

}
