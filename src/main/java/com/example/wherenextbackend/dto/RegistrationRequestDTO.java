package com.example.wherenextbackend.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class RegistrationRequestDTO {
    @NotEmpty(message = "First name is required")
    private String firstName;

    @NotEmpty(message = "Last name is required")
    private String lastName;

    @NotEmpty(message = "Username is required")
    private String username;

    @Pattern(regexp = ".+@.+", message = "Email must be valid")
    private String email;

    @NotEmpty(message = "Password is required")
    @Size(min = 1, message = "Password must be at least 1 characters")
    private String password;

    @NotEmpty(message = "Avatar Url is required")
    private String avatarUrl;

}
