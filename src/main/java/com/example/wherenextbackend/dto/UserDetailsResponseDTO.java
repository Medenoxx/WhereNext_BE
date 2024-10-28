package com.example.wherenextbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserDetailsResponseDTO {
    private Integer userId;
    private String username;
    private String email;
    private String lastName;
    private String firstName;
    private String aboutMe;
    private String avatarUrl;
    private String[] hobbies;
    private String[] interests;
}



