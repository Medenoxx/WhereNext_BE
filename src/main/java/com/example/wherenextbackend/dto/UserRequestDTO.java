package com.example.wherenextbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserRequestDTO {
    private String username;
    private String email;
    private String lastName;
    private String firstName;
}



