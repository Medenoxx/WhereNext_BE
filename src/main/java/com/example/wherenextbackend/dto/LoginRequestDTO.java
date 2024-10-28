package com.example.wherenextbackend.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class LoginRequestDTO {

    private String username;
    private String password;

}
