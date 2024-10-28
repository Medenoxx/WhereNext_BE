package com.example.wherenextbackend.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class CategoryRequestDTO {
    private String name;
    private String color;
}
