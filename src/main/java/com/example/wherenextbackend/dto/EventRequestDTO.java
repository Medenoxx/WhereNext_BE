package com.example.wherenextbackend.dto;

import lombok.*;

import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EventRequestDTO {
    private String name;
    private String description;
    private Integer categoryId;
    private LocalDate date;
    private String time;
    private Boolean isPrivate;
    private Integer maxParticipants;
    private Double latitude;
    private Double longitude;
    private String location;
    private Boolean isOutdoor;
}


