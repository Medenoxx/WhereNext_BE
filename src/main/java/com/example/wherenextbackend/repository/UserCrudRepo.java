package com.example.wherenextbackend.repository;

import com.example.wherenextbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface UserCrudRepo extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
}
