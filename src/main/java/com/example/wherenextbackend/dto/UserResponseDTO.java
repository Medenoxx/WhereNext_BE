package com.example.wherenextbackend.dto;

import com.example.wherenextbackend.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserResponseDTO {
    private Integer userId;
    private String username;
    private String email;
    private String lastName;
    private String firstName;
    private UserRole role;
}



