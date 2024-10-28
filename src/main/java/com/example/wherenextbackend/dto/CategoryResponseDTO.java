package com.example.wherenextbackend.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class CategoryResponseDTO {
    private Integer categoryId;
    private String name;
    private String color;
}
