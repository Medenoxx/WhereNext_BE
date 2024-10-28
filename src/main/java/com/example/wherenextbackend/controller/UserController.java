package com.example.wherenextbackend.controller;

import com.example.wherenextbackend.dto.*;
import com.example.wherenextbackend.entity.User;
import com.example.wherenextbackend.services.CommentService;
import com.example.wherenextbackend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/users")

public class UserController {
    private final UserService userService;
    private final CommentService commentService;

    @GetMapping
    public List<UserRequestDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public UserResponseDTO getOneUserById(@PathVariable Integer userId) {
        return userService.getOneUserById(userId);
    }

    @GetMapping("/details/{userId}")
    public UserDetailsResponseDTO getUserDetails(@PathVariable Integer userId) {
        return userService.getUserDetails(userId);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Integer userId, @RequestBody UserDetailsRequestDTO userDetailsRequestDTO) {
        User updatedUser = userService.updateUser(userId, userDetailsRequestDTO);
        UserResponseDTO updatedUserDTO = userService.convertUserToUserResponseDTO(updatedUser);
        return ResponseEntity.ok(updatedUserDTO);
    }

    @GetMapping("/{userId}/comments")
    public ResponseEntity<List<CommentResponseDTO>> getAllCommentsFromUser(@PathVariable Integer userId) {
        User currentUser = userService.getUserById(userId);
        List<CommentResponseDTO> comments = commentService.getAllCommentsFromUser(currentUser);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }
}
